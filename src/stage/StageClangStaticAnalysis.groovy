#!groovy

package stage

class StageClangStaticAnalysis extends Stage
{
    // Constructor
    StageClangStaticAnalysis(def wfc, String name = 'CLANG STATIC ANALYSIS')
    {
        // Satisfy the parent constructor
        super(wfc, name)
    }

    boolean body()
    {
        def asdf = wfc.runResourceScript('stageClangStaticAnalysis')
        wfc.echo(asdf)
        return asdf == 0
    }
}
