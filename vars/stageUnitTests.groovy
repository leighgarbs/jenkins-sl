#!groovy

def call(args)
{
  sh 'bin/run-unittests workdir/unittests/*.ut'
}
