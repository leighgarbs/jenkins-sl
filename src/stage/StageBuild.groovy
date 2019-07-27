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
            // Actually run the build.  This tees output to a file that is used
            // by the reporting step below
            def returnCode = wfc.runResourceScript('stageBuild')

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
            wfc.recordIssues qualityGates: [[threshold: 1,
                                             type: 'TOTAL',
                                             unstable: false]],
            tools: [wfc.gcc(id: 'gcc-' + buildType,
                            name: displayName,
                            pattern: 'make.' + buildType + '.out')]

            return returnCode == 0
        }
    }
}
