#!groovy

def call(args)
{
  cleanUp()

  dir('workdir')
  {
    cppcheck_args = args[0]
    if (cppcheck_args == null)
    {
      cppcheck_args = ""
    }

    withEnv(['CPPCHECK_ARGS=' + cppcheck_args])
    {
      def shellReturnStatus = sh returnStatus: true, script: '''
        ../bin/run-cppcheck -J $CPPCHECK_ARGS .
      '''

      setUnstableOnShellResult(shellReturnStatus, 1)

      publishCppcheck displayAllErrors: false,
                      displayErrorSeverity: true,
                      displayNoCategorySeverity: true,
                      displayPerformanceSeverity: true,
                      displayPortabilitySeverity: true,
                      displayStyleSeverity: true,
                      displayWarningSeverity: true,
                      pattern: 'cppcheck-result.xml',
                      severityNoCategory: false
    }
  }
}
