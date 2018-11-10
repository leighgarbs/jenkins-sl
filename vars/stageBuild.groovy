#!groovy

def call(args)
{
  runResourceScript('cleanUp')

  withEnv(['BUILD_TYPE=' + args[0], 'TARGET=' + args[1]])
  {
    return runResourceScript('stageBuild')
  }
}
