#!groovy

package stage

class StageTests extends Stage
{
    // Constructor
    StageTests(def     wfc,
               String  name,
               Boolean cleanWorkspace = false)
    {
        // Satisfy the parent constructor
        super(wfc, name, cleanWorkspace)
    }

    boolean body()
    {
        return wfc.runResourceScript('stageTests') == 0
    }
}
