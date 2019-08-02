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

    boolean runLinux()
    {
        // Use environment variables to get data into the resource script
        wfc.withEnv(['BUILD_TYPE=' + buildType, 'TARGET=' + target])
        {
            def returnCode = wfc.runResourceScript(wfc, WORKSPACE + '/linux/stageBuild')

            // Make how the build warnings display in the GUI a bit prettier
            def displayName = 'GNU C Compiler (gcc) (' + buildType + ')'
            if (buildType == 'debug')
            {
                displayName = 'Debug Build'
            }
            else if (buildType == 'release')
            {
                displayName = 'Release Build'
            }

            // Report any build warnings.  This should fail the build if any
            // are discovered.
            wfc.recordIssues enabledForFailure: true,
            qualityGates: [[threshold: 1,
                            type: 'TOTAL',
                            unstable: false]],
            tools: [wfc.gcc(id: 'gcc-' + buildType,
                            name: displayName,
                            pattern: 'make.' + buildType + '.out')]

            return returnCode == 0
        }
    }

    boolean runWindows()
    {
        return true
    }
}
