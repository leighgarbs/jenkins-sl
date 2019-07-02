#!groovy

// Jenkins sets the following environment variables:
// BUILD_WARNINGS_IGNOREPATHS_WINDOWS

def call(args)
{
    def returnCode = runResourceScript('windows/stageBuild.bat')

    // Look for warnings to report in the GUI.  The excludeFile filter isn't
    // working and I'm not sure why.
    recordIssues tool:    msBuild(),
                 filters: generateExcludeFilesFilters(
                              BUILD_WARNINGS_IGNOREPATHS_WINDOWS)

    return returnCode
}
