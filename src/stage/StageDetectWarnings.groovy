#!groovy

package stage

class StageDetectWarnings extends Stage
{
    // Constructor
    StageDetectWarnings(def     wfc,
                        String  name = 'DETECT WARNINGS',
                        Boolean cleanWorkspace = false)
    {
        // Satisfy the parent constructor
        super(wfc, name, cleanWorkspace)
    }

    boolean body()
    {
        wfc.recordIssues enabledForFailure: true,
                         qualityGates: [[threshold: 1,
                                         type: 'TOTAL',
                                         unstable: true]],
                         tools: [gcc(), clang()]

        // This stage should not fail for build warnings (the only
        // failures would be internal to the analysis tools)
        return true
    }
}
