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
        def returnCode = 0

        // Use environment variables to get data into the resource script
        wfc.withEnv(['BUILD_TYPE=' + buildType, 'TARGET=' + target])
        {
            returnCode = wfc.runResourceScript(wfc, 'linux/stageBuild')
        }

        // Make how the build warnings display in the GUI a bit prettier
        def displayName = 'Linux'
        if (buildType == 'debug')
        {
            displayName += ' Debug'
        }
        else if (buildType == 'release')
        {
            displayName += ' Release'
        }
        displayName += ' Build'

        // Report any build warnings.  This should fail the build if any
        // are discovered.
        wfc.recordIssues enabledForFailure: true,
        qualityGates: [[threshold: 1,
                        type: 'TOTAL',
                        unstable: false]],
        tools: [wfc.gcc(id: 'gcc-' + buildType,
                        name: displayName,
                        pattern: 'build.' + buildType + '.out')]

        return returnCode == 0
    }

    boolean runWindows()
    {
        def returnCode = 0

        // Use environment variables to get data into the resource script
        wfc.withEnv(['BUILD_TYPE=' + buildType, 'TARGET=' + target])
        {
            returnCode = wfc.runResourceScript(wfc, 'windows/stageBuild.bat')
        }

        // Make how the build warnings display in the GUI a bit prettier
        def displayName = 'Windows'
        if (buildType == 'debug')
        {
            displayName += ' Debug'
        }
        else if (buildType == 'release')
        {
            displayName += ' Release'
        }
        displayName += ' Build'

        // Report any build warnings.  This should fail the build if any
        // are discovered.

        // Include only warnings originating from code in the workspace.
        // Assumes we don't care about anything else.
        def excludeFilter = ''
        //def includeFilter = wfc.env.WORKSPACE.replace('\\', '\\\\') + '\\.*'
        //def excludeFilter = ''
        def includeFilter = 'C:\\\Users.*'

        wfc.recordIssues filters: [wfc.excludeFile(excludeFilter),
                                   wfc.includeFile(includeFilter)],
        enabledForFailure: true,
        qualityGates: [[threshold: 1,
                        type: 'TOTAL',
                        unstable: false]],
        tools: [wfc.msBuild(id: 'msbuild-' + buildType,
                            name: displayName,
                            pattern: 'build.' + buildType + '.out')]

        return returnCode == 0
    }
}
