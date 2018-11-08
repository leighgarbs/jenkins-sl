#!groovy

def call(stages)
{
  stageNames = []
  for (i = 0; i < stages.size(); i++)
  {
    stageNames.plus(stages[i].name)
  }

  return stageNames
}
