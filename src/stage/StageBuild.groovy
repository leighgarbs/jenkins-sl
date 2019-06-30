#!groovy

package stage

class StageBuild extends Stage
{
    // Release, debug, etc.
    String buildType

    // Make target
    String target

    // Constructor
    StageBuild(def     wfc,
               String  name,
               String  buildType,
               String  target,
               Boolean cleanWorkspace = false)
    {
        // Satisfy the parent constructor
        super(wfc, name, cleanWorkspace)

        this.buildType = buildType
        this.target = target
    }

    boolean body()
    {
        // Use environment variables to get data into the resource script
        wfc.withEnv(['BUILD_TYPE=' + buildType, 'TARGET=' + target])
        {
            return wfc.runResourceScript('stageBuild') == 0
        }
    }
}