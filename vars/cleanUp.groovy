#!groovy

def call()
{
  sh '''
    cd workdir
    git clean -x -d -f
  '''
}
