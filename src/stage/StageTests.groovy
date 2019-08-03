#!groovy

package stage

class StageTests extends Stage
{
    // Constructor
    StageTests(def wfc,
               String name,
               boolean cleanWorkspace = false,
               boolean runOnLinux = true,
               boolean runOnWindows = true)
    {
        // Satisfy the parent constructor
        super(wfc, name, cleanWorkspace, runOnLinux, runOnWindows)
    }

    boolean runLinux()
    {
        return wfc.runResourceScript(wfc, 'linux/stageTests') == 0
    }

    boolean runWindows()
    {
        return true
    }
}
