#!groovy

def call(args)
{
  cleanUp()

  dir('workdir')
  {
    withEnv(['CPPCHECK_ARGS=' + args[0]])
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
