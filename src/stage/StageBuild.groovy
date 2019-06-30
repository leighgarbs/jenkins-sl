#!groovy

package stage

class StageBuild extends Stage
{
    String buildType
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
        withEnv(['BUILD_TYPE=' + buildType, 'TARGET=' + target])
        {
            return runResourceScript('stageBuild')
        }
    }
}
