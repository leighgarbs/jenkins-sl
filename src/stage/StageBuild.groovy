#!groovy

package stage

class StageBuild extends Stage
{
    // Release, debug, etc.
    String buildType

    // Make target
    String target

    // Constructor
    StageBuild(def wfc,
               String name,
               String buildType,
               String target,
               boolean cleanWorkspace = false,
               boolean runOnLinux = true,
               boolean runOnWindows = true)
    {
        // Satisfy the parent constructor
        super(wfc, name, cleanWorkspace, runOnLinux, runOnWindows)

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

        // Include only warnings originating from code in the workspace.  The
        // workspace pattern has to be matched case insensitive because Jenkins
        // stores the pattern in all lowercase for some reason.  This seems like
        // something a user could maybe want to configure but for now just
        // hardcode it.
        wfc.recordIssues filters:
            [wfc.includeFile('(?i)^' + wfc.env.WORKSPACE)],
        enabledForFailure: true,
        tools: [wfc.gcc(id: 'gcc-' + buildType,
                        name: displayName,
                        pattern: 'buildlog.' + buildType + '.txt')]

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

        // Include only warnings originating from code in the workspace.  The
        // workspace pattern has to be matched case insensitive because Jenkins
        // stores the pattern in all lowercase for some reason.  This seems like
        // something a user could maybe want to configure but for now just
        // hardcode it.
        wfc.recordIssues filters:
            [wfc.includeFile('(?i)^' + wfc.env.WORKSPACE.replace('\\', '/'))],
        enabledForFailure: true,
        tools: [wfc.msBuild(id: 'msbuild-' + buildType,
                            name: displayName,
                            pattern: 'buildlog.' + buildType + '.txt')]

        return returnCode == 0
    }
}
