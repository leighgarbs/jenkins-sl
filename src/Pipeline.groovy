#!groovy

import stage.Stage

class Pipeline
{
    // Reference to the workflow context (wfc) the Jenkinsfile content runs in.  I don't know
    // how to statically define this yet so "def" will have to do.
    protected def wfc

    private ArrayList<Stage> stages

    Pipeline(def wfc, ArrayList<Stage> stages)
    {
        this.wfc = wfc
        this.stages = stages
    }

    void run()
    {
        // We have to have a list of all the stage names before we run any of the stages.  This
        // is a little awkward since it forces us to loop over the same list of stages twice.
        ArrayList<String> stageNames = []
        for (stage in stages)
        {
            stageNames.plus(stage.name)
        }

        wfc.properties(
            [[$class: 'GitLabConnectionProperty',
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
                // We don't allocate nodes for anything here.  The stages allocate nodes for
                // themselves to run on (in their run() function.  The stages run serially and
                // allocate nodes for all supported platforms in parallel.

                for (stage in stages)
                {
                    // Errors are handled inside this run function.  Errors don't make their
                    // way out here.
                    stage.run()
                }
            }
        }
    }
}
