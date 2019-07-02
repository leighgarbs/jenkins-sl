#!groovy

def call(String ignorePathsString)
{
    // The ignore paths come in the BUILD_WARNINGS_IGNOREPATHS_LINUX environment
    // variable, separated by whitespace.  This splits the ignore paths up on
    // whitespace into a list of ignore paths, where each element of the list is
    // a single ignore path.
    def ignorePaths = ignorePathsString.tokenize()

    // Now we add all the ignore paths to another list of whatever excludeFile
    // returns.  This is the form recordIssues takes filters in.
    def excludeFiles = []
    for (def i = 0; i < ignorePaths.size(); i++)
    {
        excludeFiles << excludeFile(ignorePaths[i])
    }

    return excludeFiles
}
