#!groovy

package stage

class StageCppcheck extends Stage
{
    String arguments

    // Constructor
    StageCppcheck(def wfc, String arguments = '', String name = 'CPPCHECK')
    {
        // Satisfy the parent constructor
        super(wfc, name)

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
            severityInformation: false,
            pattern: 'cppcheck.xml',
            severityNoCategory: false,
            severityPerformance: false,
            severityPortability: false,
            severityStyle: false
        }

        return returnCode == 0
    }

    boolean runWindows()
    {
        return true
    }
}
