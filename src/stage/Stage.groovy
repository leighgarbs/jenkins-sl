#!groovy

package stage

// Stages are not specific to platforms.  At a high level each stage is aware of
// of the platform it's running on and adjusts itself accordingly.
abstract class Stage
{
    // Reference to the workflow context (wfc) the Jenkinsfile content runs in.
    // I don't know how to statically define this yet so "def" will have to do.
    protected def wfc

    // All stages have names.  This gets displayed in the Jenkins pipeline GUI.
    protected String name

    // If true this stage will clean the workspace before it runs
    protected boolean cleanWorkspace

    // This stage will run on these platforms if these flags are set
    protected boolean runOnLinux
    protected boolean runOnWindows

    // Each stage knows how to run itself on these platforms
    abstract boolean runLinux()
    abstract boolean runWindows()

    // Constructor
    Stage(def wfc,
          String name,
          boolean cleanWorkspace = false,
          boolean runOnLinux = true,
          boolean runOnWindows = true)
    {
        this.wfc = wfc
        this.name = name
        this.cleanWorkspace = cleanWorkspace
        this.runOnLinux = runOnLinux
        this.runOnWindows = runOnWindows
    }

    // Runs the body in the appropriate workflow code context
    void run()
    {
        // This is where the stage name that shows up in the Jenkins GUI
        // pipeline widget is actually set.  Stage names should be unique.  It
        // is possible to give multiple stages the same name but the Jenkins GUI
        // pipeline widget will bug out if this is done.
        wfc.stage(name)
        {
            wfc.echo 'Starting stage ' + name

            // We don't use only the return code from the stage to determine
            // stage success.  Jenkins tools like the Cppcheck publisher and
            // Valgrind publisher fail or unstable builds in a way that shows up
            // in "currentBuild.result".  They might do this if they're
            // configured to fail or unstable builds that were unacceptable to
            // them in some way (for example, too many static analysis issues
            // detected).  The "checkForFailure" function is designed to look
            // for all the different ways a stage can fail and then do the right
            // thing when any of those ways happen.

            wfc.gitlabCommitStatus(
                connection: wfc.gitLabConnection('gitlab.dmz'),
                name:       name)
            {
                // It's at this point where we introduce parallelization.
                // Besides this "parallel" construct the whole pipeline runs
                // serially.  This stage runs on all supported platforms in
                // parallel here.

                wfc.parallel Linux: {

                    if (runOnLinux)
                    {
                        // Run the Linux dimension of this stage on an available
                        // Linux platform
                        wfc.node('Linux')
                        {
                            if (cleanWorkspace)
                            {
                                wfc.cleanWs()
                            }

                            // runLinux() is where the Linux-specific bit of
                            // this stage runs.  checkForFailure() fails the
                            // stage if anything went wrong.
                            checkForFailure(wfc, !runLinux())
                        }
                    }

                }, Windows: {

                    if (runOnWindows)
                    {
                        // Run the Windows dimension of this stage on an
                        // available Windows platform
                        wfc.node('Windows')
                        {
                            if (cleanWorkspace)
                            {
                                wfc.cleanWs()
                            }

                            // runWindows() is where the Windows-specific bit of
                            // this stage runs.  checkForFailure() fails the
                            // stage if anything went wrong.
                            checkForFailure(wfc, !runWindows())
                        }
                    }

                }, failFast: false

                // If other platforms were supported they would be added after
                // the Windows bracket in their own bracket, before the failFast
            }

            wfc.echo 'Stage ' + name + ' complete'
        }
    }

    void checkForFailure(def wfc, boolean failed)
    {
        if (failed ||
            wfc.currentBuild.result == 'UNSTABLE' ||
            wfc.currentBuild.result == 'FAILURE')
        {
            // Gitlab doesn't have a commit status for unstable
            wfc.updateGitlabCommitStatus(name: name, state: 'failed')

            wfc.error('Stage ' + name + ' failed')
        }
    }
}
