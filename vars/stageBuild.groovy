#!groovy

def call(args)
{
  cleanUp()

  withEnv(['BUILD_TYPE=' + args[0], 'TARGET=' + args[1]])
  {
    sh '''
      cd workdir
      ../bin/run-cmake --$BUILD_TYPE .
      make -B $TARGET
    '''
  }

  saveArtifacts()
}
