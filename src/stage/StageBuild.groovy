#!groovy

package stage

class StageBuild extends Stage
{
    // Release, debug, etc.
    String buildType

    // Make target
    String target

    // Constructor
    StageBuild(def wfc, String name, String buildType, String target)
    {
        // Satisfy the parent constructor
        super(wfc, name)

        this.buildType = buildType
        this.target = target
    }

    boolean body()
    {
        // Use environment variables to get data into the resource script
        wfc.withEnv(['BUILD_TYPE=' + buildType, 'TARGET=' + target])
        {
            def returnCode = wfc.runResourceScript('stageBuild')

            // Publish build warnings
            wfc.recordIssues enabledForFailure: true,
            qualityGates: [[threshold: 1,
                            type: 'TOTAL',
                            unstable: false]],
            tools: [wfc.gcc(pattern: 'make.' + buildType + '.out')]

            return returnCode == 0
        }
    }
}
