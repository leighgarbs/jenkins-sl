#!groovy

def call(args)
{
    // Go back to a pristine checkout
    runResourceScript('cleanUp')

    def cppcheck_args = args[0]
    if (cppcheck_args == null)
    {
        cppcheck_args = ""
    }

    // stageCppcheck script is written to work out of the current directory, and
    // all the code is in STAGE_DIRECTORY, so go there
    dir(STAGE_DIRECTORY)
    {
        withEnv(['CPPCHECK_ARGS=' + cppcheck_args])
        {
            // Purposefully ignore the error code.  Only the analysis of the
            // results performed by publishCppcheck should affect build status.
            runResourceScript('stageCppcheck')
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

    // The publishCppcheck action above will take care of failing the build if
    // the build should fail due to discovered issues.
    return 0
}
