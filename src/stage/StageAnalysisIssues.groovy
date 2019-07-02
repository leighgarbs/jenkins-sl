#!groovy

package stage

class StageAnalysisIssues extends Stage
{
    // Constructor
    StageAnalysisIssues(def wfc, String name = 'ANALYSIS ISSUES')
    {
        // Satisfy the parent constructor
        super(wfc, name)
    }

    boolean body()
    {
        // All of these analyses report failure by setting the build result to
        // FAILURE

        // Publish Valgrind issues
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

        // Publish GCC and Clang warnings
        wfc.recordIssues enabledForFailure: true,
                         qualityGates: [[threshold: 1,
                                         type: 'TOTAL',
                                         unstable: false]],
                         tools: [wfc.gcc(), wfc.clang()]

        // Publish Cppcheck issues
        wfc.dir(wfc.STAGE_DIR)
        {
            wfc.publishCppcheck displayAllErrors: false,
                                displayErrorSeverity: true,
                                displayPerformanceSeverity: true,
                                displayPortabilitySeverity: true,
                                displayStyleSeverity: true,
                                displayWarningSeverity: true,
                                failureThreshold: '0',
                                severityInformation: false,
                                pattern: 'cppcheck.xml',
                                severityNoCategory: false,
                                severityPerformance: false,
                                severityPortability: false,
                                severityStyle: false
        }

        return !(wfc.currentBuild.result == 'UNSTABLE' ||
                 wfc.currentBuild.result == 'FAILURE')
    }
}
