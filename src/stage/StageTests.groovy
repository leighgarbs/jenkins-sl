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

    boolean runLinux()
    {
        return wfc.runResourceScript('stageTests') == 0
    }

    boolean runWindows()
    {
        return true
    }
}
