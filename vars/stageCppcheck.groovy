#!groovy

def call(args)
{
    // Go back to a pristine checkout
    runResourceScript('cleanUp')

    // Return code from the stageCppcheck script will be written into here
    def returnCode = 0

    def cppcheck_args = args[0]
    if (cppcheck_args == null)
    {
        cppcheck_args = ""
    }

    // stageCppcheck script is written to work out of the current directory, and
    // all the code is in STAGE_DIR, so go there
    dir(STAGE_DIR)
    {
        withEnv(['CPPCHECK_ARGS=' + cppcheck_args])
        {
            returnCode = runResourceScript('stageCppcheck')
        }
    }

    // The cppcheck stage returns non-zero if it finds an issue with a severity
    // of either warning or error
    if(returnCode == 1)
    {
        print('Setting build result to UNSTABLE')
        currentBuild.result = 'UNSTABLE'
    }

    // Publish all the results to the Jenkins plugin
    publishCppcheck displayAllErrors: false,
    displayErrorSeverity:             true,
    displayNoCategorySeverity:        true,
    displayPerformanceSeverity:       true,
    displayPortabilitySeverity:       true,
    displayStyleSeverity:             true,
    displayWarningSeverity:           true,
    pattern:                          STAGE_DIR + '/cppcheck.xml',
    severityNoCategory:               false

    return returnCode
}
