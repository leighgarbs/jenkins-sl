#!groovy

// Jenkins sets the following environment variables:
// CLEARCASE_DYNAMICVIEW
// CLEARCASE_DYNAMICVIEW_NAME
// WORKSPACE

def call(scriptName)
{
    def returnCode = 0

    // Grab a text copy of the script.  libraryResource knows where the resource
    // directory is.  There doesn't seem to be a particularly brilliant way to
    // just run the script in-place, wherever it is
    def resourceScript = libraryResource scriptName

    // Now we need to know if we should run this script in a ClearCase dynamic
    // view
    if (CLEARCASE_DYNAMICVIEW == 'true')
    {
        // We can't immediately execute this script by bracketing
        // "resourceScript" in a cleartool setview -exec command because there
        // might be single quotes in it.  As a workaround, write the file to the
        // workspace and then execute it from there.
        writeFile file: scriptName, text: resourceScript

        withEnv(['SCRIPT_NAME=' + scriptName])
        {
            if (isUnix())
            {
                returnCode = sh returnStatus: true, script: '''
                    #!/bin/bash
                    cd $WORKSPACE
                    chmod +x $SCRIPT_NAME
                    cleartool setview -exec ./$SCRIPT_NAME $CLEARCASE_DYNAMICVIEW_NAME_LINUX
                '''
            }
            else
            {
                returnCode = bat returnStatus: true, script: '''
                    @cleartool startview %CLEARCASE_DYNAMICVIEW_NAME_WINDOWS%
                    @cleartool mount -all > nul
                    @cd /D %WORKSPACE%
                    @call "%VCVARSALL_DIR%\\vcvarsall.bat"
                    @call "%SCRIPT_NAME%"
                '''
            }
        }
    }
    else
    {
        // We don't need to run in a ClearCase dynamic view, so this is easy,
        // just directly run the script text we have

        if (isUnix())
        {
            returnCode = sh returnStatus: true, script: resourceScript
        }
        else
        {
            // Have to set up Visual studio development environment
            resourceScript =
                '@call "%VCVARSALL_DIR%\\vcvarsall.bat" & ' + resourceScript

            returnCode = bat returnStatus: true, script: resourceScript
        }
    }

    return returnCode
}
