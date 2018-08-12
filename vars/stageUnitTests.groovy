#!groovy

def call(args)
{
  sh '''
    cd workdir/unittests
    ../../bin/run-unittests *.ut
  '''

  saveArtifacts()
}
