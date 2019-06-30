#!groovy

package stage

class StageClangStaticAnalysis extends Stage
{
    // Constructor
    StageClangStaticAnalysis(def     wfc,
                             String  name,
                             Boolean cleanWorkspace = false)
    {
        // Satisfy the parent constructor
        super(wfc, name, cleanWorkspace)
    }

    boolean body()
    {
        return wfc.runResourceScript('stageClangStaticAnalysis') == 0
    }
}
