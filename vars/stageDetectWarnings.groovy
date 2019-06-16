#!groovy

def call(args)
{
    recordIssues qualityGates: [[threshold: 1, type: 'TOTAL', unstable: true]],
                 tools: [gcc(), clang()]

    // This stage should not fail for build warnings (the only
    // failures would be internal to the analysis tools)
    return 0
}
