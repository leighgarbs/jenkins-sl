#!groovy

package stage

class StageBuild extends Stage
{
    // Release, debug, etc.
    String buildType

    // Make target
    String target

    // Should this stage publish build warnings?  Build warnings can only be
    // published once per pipeline.  Trying to publish them more than once will
    // crash the pipeline.
    boolean publishIssues

    // Constructor
    StageBuild(def     wfc,
               String  name,
               String  buildType,
               String  target,
               boolean publishIssues)
    {
        // Satisfy the parent constructor
        super(wfc, name)

        this.buildType = buildType
        this.target = target
        this.publishIssues = publishIssues
    }

    boolean body()
    {
        // Use environment variables to get data into the resource script
        wfc.withEnv(['BUILD_TYPE=' + buildType, 'TARGET=' + target])
        {
            def returnCode = wfc.runResourceScript('stageBuild')

            def scanResult = wfc.scanForIssues tool: wfc.gcc(
                pattern: 'make.' + buildType + '.out'),
            id: 'gcc-' + buildType

            //if (publishIssues)
            //{
                // Publish build warnings
            wfc.publishIssues issues: [scanResult],
            enabledForFailure: true,
            qualityGates: [[threshold: 1,
                            type: 'TOTAL',
                            unstable: false]]
            //}

            return returnCode == 0
        }
    }
}
