#!groovy

def call(args)
{
    recordIssues aggregatingResults: true, tools: [gcc(), clang()]

    // This stage should not fail for build warnings (the only
    // failures would be internal to the analysis tools)
    return 0
}
