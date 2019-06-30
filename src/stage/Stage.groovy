#!groovy

package stage

abstract class Stage
{
    // Reference to the workflow context (wfc) the Jenkinsfile content runs in.
    // I don't know how to statically define this yet so "def" will have to do.
    protected def wfc

    protected String name
    Boolean cleanWorkspace

    // What each stage does specifically is defined in derived classes
    abstract boolean body()

    // Constructor
    Stage(def wfc, String name, Boolean cleanWorkspace = false)
    {
        this.wfc = wfc
        this.name = name
        this.cleanWorkspace = cleanWorkspace
    }

    // Runs the body in the appropriate workflow code context
    boolean run()
    {
        def platformName = ''

        if (wfc.isUnix())
        {
            // MacOS will also cause isUnix() to return true, but we don't
            // support automated MacOS builds yet
            platformName = 'Linux'
        }
        else
        {
            // The only other platform we support automated builds for is
            // Windows
            platformName = 'Windows'
        }

        // This is where the stage name that shows up in the Jenkins GUI
        // pipeline widget is actually set.  Stage names should be unique.  It
        // is possible to give multiple stages the same name but the Jenkins GUI
        // pipeline widget will bug out if this is done.
        wfc.stage(name + ' (' + platformName + ')')
        {
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
                // Clear the workspace before doing anything meaningful, if
                // that's what the user wants
                if (cleanWorkspace)
                {
                    wfc.cleanWs()
                }

                // Do derived stage stuff
                if (!body())
                {
                    // Gitlab doesn't have a commit status for unstable
                    wfc.updateGitlabCommitStatus(name:  name, state: 'failed')

                    wfc.error('Stage ' + name + ' failed on ' + platformName)
                }

                return true
            }
        }
    }
}
