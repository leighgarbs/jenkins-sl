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

    boolean body()
    {
        def returnCode = 0

        wfc.withEnv(['CPPCHECK_ARGS=' + arguments])
        {
            // Purposefully ignore the error code.  Only the analysis of the
            // results performed by publishCppcheck should affect build status.
            returnCode = wfc.runResourceScript('stageCppcheck')

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
}
