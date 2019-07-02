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
        // Purposefully don't pay attention to the valgrind return code here.
        // The ValgrindPublisher step below will take care of failing the build
        // if it's necessary to do so.
        wfc.runResourceScript('stageValgrind')

        wfc.echo 'BEFORE'
        wfc.echo wfc.currentBuild.result

        // Post the Valgrind analysis results to Jenkins.  This will fail the
        // build if there are no Valgrind reports or if the reports are
        // "invalid", whatever that means.
        wfc.step([$class: 'ValgrindPublisher',
                  failBuildOnInvalidReports: true,
                  failBuildOnMissingReports: true,
                  failThresholdDefinitelyLost: '0',
                  failThresholdInvalidReadWrite: '0',
                  failThresholdTotal: '0',
                  pattern: wfc.STAGE_DIR + '/valgrind.*.xml',
                  publishResultsForAbortedBuilds: false,
                  publishResultsForFailedBuilds: false,
                  sourceSubstitutionPaths: ''])

        wfc.echo 'AFTER'
        wfc.echo wfc.currentBuild.result

        // This stage fails by setting currentBuild, not by return code
        return true
    }
}
