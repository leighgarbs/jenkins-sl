#!groovy

import stage.Stage

class Pipeline
{
    // Reference to the workflow context (wfc) the Jenkinsfile content runs in.
    // I don't know how to statically define this yet so "def" will have to do.
    protected def wfc

    private ArrayList<Stage> stages

    Pipeline(def wfc, ArrayList<Stage> stages)
    {
        this.wfc = wfc
        this.stages = stages
    }

    void run()
    {
        // We have to have a list of all the stage names before we run any of
        // the stages.  This is a little awkward since it forces us to loop over
        // the same list of stages twice.
        ArrayList<String> stageNames = getStageNames()

        wfc.properties([[$class: 'GitLabConnectionProperty',
                         gitLabConnection: 'gitlab.dmz'],
                        wfc.pipelineTriggers([[$class: 'GitLabPushTrigger',
                                           triggerOnPush: true,
                                           triggerOnMergeRequest: true,
                                           skipWorkInProgressMergeRequest: true,
                                           pendingBuildName: stageNames[0]]])])

        // This tells Gitlab the names of the stages we'll be running
        wfc.gitlabBuilds(builds: stageNames)
        {
            wfc.timestamps
            {
                for (stage in stages)
                {
                    // Any errors in this function are handled within it
                    stage.run()
                }
            }
        }
    }

    private ArrayList<String> getStageNames()
    {
        // Will be filled in with stage names
        def stageNames = []

        for (stage in stages)
        {
            stageNames.plus(stage.name)
        }

        return stageNames
    }
}
