#!groovy

package stage

class StageCppcheck extends Stage
{
    String arguments

    // Constructor
    StageCppcheck(def wfc,
                  String arguments = '',
                  boolean cleanWorkspace = false,
                  boolean runOnLinux = true,
                  boolean runOnWindows = false)
    {
        // Satisfy the parent constructor
        super(wfc, 'CPPCHECK', cleanWorkspace, runOnLinux, runOnWindows)

        this.arguments = arguments
    }

    boolean runLinux()
    {
        def returnCode = 0

        wfc.withEnv(['CPPCHECK_ARGS=' + arguments])
        {
            returnCode = wfc.runResourceScript(wfc, 'linux/stageCppcheck')

            // Publish Cppcheck issues
            wfc.publishCppcheck displayAllErrors: false,
            displayErrorSeverity: true,
            displayPerformanceSeverity: true,
            displayPortabilitySeverity: true,
            displayStyleSeverity: true,
            displayWarningSeverity: true,
            failureThreshold: '0',
            pattern: 'cppcheck.xml',
            severityError: true,
            severityInformation: false,
            severityNoCategory: false,
            severityPerformance: false,
            severityPortability: false,
            severityStyle: false,
            severityWarning: true
        }

        return returnCode == 0
    }

    boolean runWindows()
    {
        return true
    }
}
