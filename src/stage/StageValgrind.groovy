#!groovy

package stage

class StageValgrind extends Stage
{
    // Constructor
    StageValgrind(def wfc, String name = 'VALGRIND')
    {
        // Satisfy the parent constructor
        super(wfc, name)
    }

    boolean body()
    {
        return wfc.runResourceScript('stageValgrind') == 0
    }
}
