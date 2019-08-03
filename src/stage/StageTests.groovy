#!groovy

package stage

class StageTests extends Stage
{
    // Constructor
    StageTests(def wfc,
               String name,
               boolean runOnLinux = true,
               boolean runOnWindows = true,
               boolean cleanWorkspace = false)
    {
        // Satisfy the parent constructor
        super(wfc, name, runOnLinux, runOnWindows, cleanWorkspace)
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
