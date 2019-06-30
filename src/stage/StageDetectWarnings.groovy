#!groovy

package stage

class StageDetectWarnings extends Stage
{
    // Constructor
    StageDetectWarnings(def wfc, String name = 'DETECT WARNINGS')
    {
        // Satisfy the parent constructor
        super(wfc, name)
    }

    boolean body()
    {
        wfc.recordIssues enabledForFailure: true,
                         qualityGates: [[threshold: 1,
                                         type: 'TOTAL',
                                         unstable: true]],
                         tools: [wfc.gcc(), wfc.clang()]

        // This stage should not fail for build warnings (the only
        // failures would be internal to the analysis tools)
        return true
    }
}
