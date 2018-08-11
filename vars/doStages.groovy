#!groovy

def call(stages)
{
  stageNames = getStageNames(stages)

  properties([[$class: 'GitLabConnectionProperty',
              gitLabConnection: 'gitlab.dmz'],
              pipelineTriggers([[$class: 'GitLabPushTrigger',
                                triggerOnPush: true,
                                triggerOnMergeRequest: true,
                                skipWorkInProgressMergeRequest: true,
                                pendingBuildName: stageNames[0]]])])

  gitlabBuilds(builds: stageNames)
  {
    node ()
    {
      for (i = 0; i < stages.size(); i++)
      {
        doStage(stages[i].name, stages[i].body, stages[i].args)
      }
    }
  }
}