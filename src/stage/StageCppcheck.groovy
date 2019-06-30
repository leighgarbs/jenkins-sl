#!groovy

package stage

class StageCppcheck extends Stage
{
    // Constructor
    StageCppcheck(def     wfc,
                  String  arguments = '',
                  String  name = 'CPPCHECK',
                  Boolean cleanWorkspace = false)
    {
        // Satisfy the parent constructor
        super(wfc, name, cleanWorkspace)
    }

    boolean body()
    {
        // stageCppcheck script is written to work out of the current directory,
        // and all the code is in STAGE_DIR, so go there
        wfc.dir(wfc.STAGE_DIR)
        {
            wfc.withEnv(['CPPCHECK_ARGS=' + arguments])
            {
                // Purposefully ignore the error code.  Only the analysis of the
                // results performed by publishCppcheck should affect build
                // status.
                wfc.runResourceScript('stageCppcheck')
            }

            // Publish all the results to the Jenkins plugin.
            publishCppcheck displayAllErrors: false,
            displayErrorSeverity:             true,
            displayPerformanceSeverity:       true,
            displayPortabilitySeverity:       true,
            displayStyleSeverity:             true,
            displayWarningSeverity:           true,
            failureThreshold:                 '0',
            severityInformation:              false,
            pattern:                          'cppcheck.xml',
            severityNoCategory:               false,
            severityPerformance:              false,
            severityPortability:              false,
            severityStyle:                    false
        }

        // The publishCppcheck action above will take care of failing the build
        // if the build should fail due to discovered issues.
        return true
    }
}
