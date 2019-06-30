#!groovy

package stage

class StageValgrind extends Stage
{
    // Constructor
    StageValgrind(def     wfc,
                  String  name = 'VALGRIND',
                  Boolean cleanWorkspace = false)
    {
        // Satisfy the parent constructor
        super(wfc, name, cleanWorkspace)
    }

    boolean body()
    {
        // Purposefully don't pay attention to the valgrind return code here.
        // The ValgrindPublisher step below will take care of failing the build
        // if it's necessary to do so.
        wfc.runResourceScript('stageValgrind')

        // Post the Valgrind analysis results to Jenkins.  This will fail the
        // build if there are no Valgrind reports or if the reports are
        // "invalid", whatever that means.
        wfc.step([$class: 'ValgrindPublisher',
                  failBuildOnInvalidReports: true,
                  failBuildOnMissingReports: true,
                  failThresholdDefinitelyLost: '',
                  failThresholdInvalidReadWrite: '',
                  failThresholdTotal: '',
                  pattern: wfc.STAGE_DIR + '/valgrind.*.xml',
                  publishResultsForAbortedBuilds: false,
                  publishResultsForFailedBuilds: false,
                  sourceSubstitutionPaths: '',
                  unstableThresholdDefinitelyLost: '0',
                  unstableThresholdInvalidReadWrite: '0',
                  unstableThresholdTotal: '0'])

        // This stage fails by setting currentBuild, not by return code
        return true
    }
}
