#!groovy

def call(args)
{
  // Run Valgrind.  Not sure if this is any better than just running Valgrind in
  // the shell.
  step([$class: 'ValgrindBuilder',
    childSilentAfterFork: true,
    excludePattern:       '',
    generateSuppressions: false,
    ignoreExitCode:       true,
    includePattern:       'workdir/tests/*_test',
    outputDirectory:      'workdir/tests',
    outputFileEnding:     '.valgrind.xml',
    programOptions:       '',
    removeOldReports:     false,
    suppressionFiles:     '',
    tool: [$class: 'ValgrindToolMemcheck',
          leakCheckLevel:       'full',
          showReachable:        false,
          trackOrigins:         true,
          undefinedValueErrors: true],
    traceChildren:      false,
    valgrindExecutable: '',
    valgrindOptions:    '',
    workingDirectory:   'workdir/tests'])

  // Post the Valgrind analysis results to Jenkins
  step([$class: 'ValgrindPublisher',
    failBuildOnInvalidReports:         true,
    failBuildOnMissingReports:         true,
    failThresholdDefinitelyLost:       '',
    failThresholdInvalidReadWrite:     '',
    failThresholdTotal:                '',
    pattern:                           'workdir/tests/*.valgrind.xml',
    publishResultsForAbortedBuilds:    false,
    publishResultsForFailedBuilds:     false,
    sourceSubstitutionPaths:           '',
    unstableThresholdDefinitelyLost:   '0',
    unstableThresholdInvalidReadWrite: '0',
    unstableThresholdTotal:            '0'])
}
