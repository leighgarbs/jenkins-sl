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
        def returnCode = wfc.runResourceScript('stageValgrind') == 0

        // Publish any discoveredissues
        wfc.step([$class: 'ValgrindPublisher',
                  failBuildOnInvalidReports: true,
                  failBuildOnMissingReports: true,
                  failThresholdDefinitelyLost: '0',
                  failThresholdInvalidReadWrite: '0',
                  failThresholdTotal: '0',
                  pattern: wfc.STAGE_DIR + '/valgrind.*.xml',
                  publishResultsForAbortedBuilds: false,
                  publishResultsForFailedBuilds: true,
                  sourceSubstitutionPaths: ''])

        return returnCode
    }
}
