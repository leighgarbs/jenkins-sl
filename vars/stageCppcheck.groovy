#!groovy

def call(args)
{
  runResourceScript('cleanUp')

  def returnCode = 0

  dir('workdir')
  {
    cppcheck_args = args[0]
    if (cppcheck_args == null)
    {
      cppcheck_args = ""
    }

    withEnv(['CPPCHECK_ARGS=' + cppcheck_args])
    {
      returnCode = runResourceScript('stageCppcheck')

      if(returnCode == 1)
      {
        currentBuild.result = 'UNSTABLE'
      }

      publishCppcheck displayAllErrors:           false,
                      displayErrorSeverity:       true,
                      displayNoCategorySeverity:  true,
                      displayPerformanceSeverity: true,
                      displayPortabilitySeverity: true,
                      displayStyleSeverity:       true,
                      displayWarningSeverity:     true,
                      pattern:                    'cppcheck-result.xml',
                      severityNoCategory:         false
    }
  }

  return returnCode
}
