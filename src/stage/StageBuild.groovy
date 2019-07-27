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

            //def scanResult = wfc.scanForIssues tool: wfc.gcc(
            //    pattern: 'make.' + buildType + '.out'),
            //id: 'gcc-' + buildType

            // Publish build warnings
            //wfc.recordIssues issues: [scanResult],
            //id: 'gcc-' + buildType,
            //enabledForFailure: true,
            //qualityGates: [[threshold: 1,
            //                type: 'TOTAL',
            //                unstable: false]]

            wfc.recordIssues aggregatingResults: true,
            qualityGates: [[threshold: 1,
                            type: 'TOTAL',
                            unstable: false]],
            tools: [gcc(id: 'gcc-' + buildType,
                        name: 'Hello' + buildType,
                        pattern: 'make.' + buildType + '.out')]

            return returnCode == 0
        }
    }
}
