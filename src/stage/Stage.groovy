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

    protected boolean cleanWorkspace

    // Each stage knows how to run itself on these platforms
    abstract boolean runLinux()
    abstract boolean runWindows()

    // Constructor
    Stage(def wfc, String name, boolean cleanWorkspace = false)
    {
        this.wfc = wfc
        this.name = name
        this.cleanWorkspace = cleanWorkspace
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
            // detected).

            // If the stage body returns unsuccessfully then mark the build as
            // failed.  There isn't a return code for unstable, but at this
            // point currentBuild.result will be set to unstable if the stage is
            // unstable.  By potentially setting the build result to failed here
            // we may override an unstable build result with a failed build
            // result.  That seems fine since we naturally want the build result
            // to represent the worst outcome.

            wfc.gitlabCommitStatus(
                connection: wfc.gitLabConnection('gitlab.dmz'),
                name:       name)
            {
                def returnCodeLinux   = 0
                def returnCodeWindows = 0

                wfc.parallel Linux: {

                    // Run the Linux dimension of this stage on an available
                    // Linux platform
                    wfc.node('Linux')
                    {
                        if (cleanWorkspace)
                        {
                            wfc.cleanWs()
                        }

                        returnCodeLinux = runLinux()
                    }

                    wfc.error('asdfasdfasdfasdf')

                }, Windows: {

                    // Run the Windows dimension of this stage on an available
                    // Windows platform
                    wfc.node('Windows')
                    {
                        if (cleanWorkspace)
                        {
                            wfc.cleanWs()
                        }

                        returnCodeWindows = runWindows()
                    }

                }

                // If the body fails outright or caused the current build to go
                // unstable or fail
                if (!returnCodeLinux ||
                    !returnCodeWindows ||
                    wfc.currentBuild.result == 'UNSTABLE' ||
                    wfc.currentBuild.result == 'FAILURE')
                {
                    // Gitlab doesn't have a commit status for unstable
                    wfc.updateGitlabCommitStatus(name:  name,
                                                 state: 'failed')

                    wfc.error('Stage ' + name + ' failed')
                }
            }

            wfc.error('HEYYYY')

            wfc.echo 'Stage ' + name + ' complete'
        }
    }
}
