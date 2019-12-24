#!groovy

package stage

class StageClangStaticAnalysis extends Stage
{
    // Constructor
    StageClangStaticAnalysis(def wfc,
                             boolean cleanWorkspace = false,
                             boolean runOnLinux = true,
                             boolean runOnWindows = false)
    {
        // Satisfy the parent constructor
        super(wfc,
              'CLANG STATIC ANALYSIS',
              cleanWorkspace,
              runOnLinux,
              runOnWindows)
    }

    boolean runLinux()
    {
        // This writes clang.debug.out to the workspace so the recordIssues step
        // below can ingest it.
        def returnCode =
            wfc.runResourceScript(wfc, 'linux/stageClangStaticAnalysis')

        // Include only warnings originating from code in the workspace.  The
        // workspace pattern has to be matched case insensitive because Jenkins
        // stores the pattern in all lowercase for some reason.  This seems like
        // something a user could maybe want to configure but for now just
        // hardcode it.
        wfc.recordIssues filters:
            [wfc.includeFile('(?i)^' + wfc.env.WORKSPACE)],
        enabledForFailure: true,
        qualityGates: [[threshold: 1,
                        type: 'TOTAL',
                        unstable: false]],
        tools: [wfc.clang(name: 'Clang Static Analysis',
                          pattern: 'clang.debug.out')]

        return returnCode == 0
    }

    boolean runWindows()
    {
        return true
    }
}
