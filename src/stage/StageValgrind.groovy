#!groovy

package stage

class StageValgrind extends Stage
{
    // Constructor
    StageValgrind(def wfc,
                  boolean cleanWorkspace = false,
                  boolean runOnLinux = true,
                  boolean runOnWindows = false)
    {
        // Satisfy the parent constructor
        super(wfc, 'VALGRIND', cleanWorkspace, runOnLinux, runOnWindows)
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
