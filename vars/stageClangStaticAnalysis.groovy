#!groovy

def call(args)
{
  cleanUp()

  sh '''
    cd workdir
    scan-build ../bin/run-cmake --debug .
    scan-build -o clangScanBuildReports -v -v --use-cc clang \
      --use-analyzer=/usr/bin/clang make -B
  '''
}
