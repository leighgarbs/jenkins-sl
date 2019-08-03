#!groovy

package stage

class StageClangStaticAnalysis extends Stage
{
    // Constructor
    StageClangStaticAnalysis(def wfc,
                             String name = 'CLANG STATIC ANALYSIS',
                             boolean cleanWorkspace = false,
                             boolean runOnLinux = true,
                             boolean runOnWindows = false)
    {
        // Satisfy the parent constructor
        super(wfc, name, cleanWorkspace, runOnLinux, runOnWindows)
    }

    boolean runLinux()
    {
        // This writes clang.debug.out to the workspace so the recordIssues step
        // below can ingest it.
        def returnCode =
            wfc.runResourceScript(wfc, 'linux/stageClangStaticAnalysis')

        // Publish build warnings
        wfc.recordIssues enabledForFailure: true,
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
