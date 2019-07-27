#!groovy

package stage

class StageBuild extends Stage
{
    // Release, debug, etc.
    String buildType

    // Make target
    String target

    // Should this stage record build warnings?  Build warnings can only be
    // recorded once per build.  Trying to record them more than once will crash
    // the pipeline.
    boolean recordIssues

    // Constructor
    StageBuild(def     wfc,
               String  name,
               String  buildType,
               String  target,
               boolean recordIssues)
    {
        // Satisfy the parent constructor
        super(wfc, name)

        this.buildType = buildType
        this.target = target
        this.recordIssues = recordIssues
    }

    boolean body()
    {
        // Use environment variables to get data into the resource script
        wfc.withEnv(['BUILD_TYPE=' + buildType, 'TARGET=' + target])
        {
            def returnCode = wfc.runResourceScript('stageBuild')

            // Stash the make output, otherwise it may be deleted by another
            // stage before the conditional analysis below runs.
            //wfc.stash includes: 'make.*.out',
            //name: 'Build',
            //useDefaultExcludes: false

            //if (recordIssues)
            //{
            //    wfc.unstash 'Build'

                // Publish build warnings
                wfc.scanForIssues tool: wfc.gcc(pattern: 'make.*.out')
            //}

            return returnCode == 0
        }
    }
}
