#!groovy

def call(args)
{
  runResourceScript('cleanUp')
  runResourceScript('stageClangStaticAnalysis')
}
