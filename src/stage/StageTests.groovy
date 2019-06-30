#!groovy

package stage

class StageTests extends Stage
{
    // Constructor
    StageTests(def wfc, String name)
    {
        // Satisfy the parent constructor
        super(wfc, name)
    }

    boolean body()
    {
        return wfc.runResourceScript('stageTests') == 0
    }
}
