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

    boolean runLinux()
    {
        def returnCode = wfc.runResourceScript(wfc, 'linux/stageValgrind')

        // Publish any discovered issues
        wfc.step([$class: 'ValgrindPublisher',
                  failBuildOnInvalidReports: true,
                  failBuildOnMissingReports: true,
                  failThresholdDefinitelyLost: '0',
                  failThresholdInvalidReadWrite: '0',
                  failThresholdTotal: '0',
                  pattern: 'valgrind.*.xml',
                  publishResultsForAbortedBuilds: false,
                  publishResultsForFailedBuilds: true,
                  sourceSubstitutionPaths: ''])

        return returnCode == 0
    }

    boolean runWindows()
    {
        return true
    }
}
