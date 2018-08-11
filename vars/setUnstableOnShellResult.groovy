#!groovy

def setUnstableOnShellResult =
{
  resultShell, resultUnstable ->
  if(resultShell == resultUnstable)
  {
    currentBuild.result = 'UNSTABLE'
  }
}
