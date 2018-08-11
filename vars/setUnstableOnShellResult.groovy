#!groovy

def call(resultShell, resultUnstable)
{
  if(resultShell == resultUnstable)
  {
    currentBuild.result = 'UNSTABLE'
  }
}
