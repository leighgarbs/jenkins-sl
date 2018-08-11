#!groovy

def call(args)
{
  step([$class: 'ValgrindBuilder',
    childSilentAfterFork: true,
    excludePattern: '',
    generateSuppressions: false,
    ignoreExitCode: false,
    includePattern: 'workdir/unittests/*.ut',
    outputDirectory: 'workdir/unittests',
    outputFileEnding: '.valgrind.xml',
    programOptions: '',
    removeOldReports: false,
    suppressionFiles: '',
    tool: [$class: 'ValgrindToolMemcheck',
          leakCheckLevel: 'full',
          showReachable: false,
          trackOrigins: true,
          undefinedValueErrors: true],
    traceChildren: false,
    valgrindExecutable: '',
    valgrindOptions: '',
    workingDirectory: 'workdir/unittests'])

  step([$class: 'ValgrindPublisher',
    failBuildOnInvalidReports: true,
    failBuildOnMissingReports: true,
    failThresholdDefinitelyLost: '',
    failThresholdInvalidReadWrite: '',
    failThresholdTotal: '',
    pattern: 'workdir/unittests/*.valgrind.xml',
    publishResultsForAbortedBuilds: false,
    publishResultsForFailedBuilds: false,
    sourceSubstitutionPaths: '',
    unstableThresholdDefinitelyLost: '0',
    unstableThresholdInvalidReadWrite: '0',
    unstableThresholdTotal: '0'])

  saveArtifacts()
}
