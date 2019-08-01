#!groovy

def call(wfc, scriptName)
{
    // Looks in the resources directory for script "scriptName"
    def script = wfc.libraryResource scriptName

    def returnCode = 0

    // Jenkins DSL should really deal with this itself
    if (wfc.isUnix())
    {
        returnCode = sh returnStatus: true, script: script
    }
    else
    {
        returnCode = bat returnStatus: true, script: script
    }

    return returnCode
}
