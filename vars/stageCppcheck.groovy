#!groovy

def call(args)
{
  cleanUp()

  dir('workdir')
  {
    def shellReturnStatus = sh returnStatus: true, script: '''
      ../bin/run-cppcheck -J --suppress=unusedFunction .
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

  saveArtifacts()
}