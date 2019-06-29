#!groovy

def call(pipeline)
{
    // Will be filled in with stage names
    stageNames = []

    for (stage in pipeline)
    {
        stageNames.plus(stage.name)
    }

    return stageNames
}
