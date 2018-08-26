#!groovy

def call(args)
{
  sh 'cd workdir; ctest .'
}
