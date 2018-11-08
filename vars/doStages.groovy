#!groovy

def call(stages)
{
  // We have to have a list of all the stage names before we run any of the
  // stages.  This is a little awkward since it forces us to loop over the same
  // list of stages twice.
  stageNames = getStageNames(stages)

  properties([[$class: 'GitLabConnectionProperty',
              gitLabConnection: 'gitlab.dmz'],
              pipelineTriggers([[$class: 'GitLabPushTrigger',
                                triggerOnPush: true,
                                triggerOnMergeRequest: true,
                                skipWorkInProgressMergeRequest: true,
                                pendingBuildName: stageNames[0]]])])

  // This tells Gitlab the names of the stages we'll be running
  gitlabBuilds(builds: stageNames)
  {
    node ()
    {
      // Wipe out everything in the workspace before this pipeline is run
      deleteDir()

      for (i = 0; i < stages.size(); i++)
      {
        doStage(stages[i].name, stages[i].body, stages[i].args)
      }
    }
  }
}