#!groovy

import stage.Stage

class PipelineBranch
{
    // Reference to the workflow context (wfc) the Jenkinsfile content runs in.
    // I don't know how to statically define this yet so "def" will have to do.
    protected def wfc

    // This pipeline will run on this platform, identified by string.  This gets
    // passed to the node() Jenkins pipeline construct
    String platform

    private ArrayList<Stage> stages

    PipelineBranch(def wfc, String platform, ArrayList<Stage> stages)
    {
        this.wfc = wfc
        this.platform = platform
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
            // Allocate a node for this whole pipeline.  Entire pipelines get
            // assigned to nodes this way.  It is possible to assign workflow to
            // nodes at an increased resolution (such as assigning individual
            // stages to nodes) but this seems sufficient for now.

            // This assumes nodes are labeled with the name of the platform they
            // run.  Labeling nodes can be done using the Jenkins GUI under
            // "Manage Jenkins -> Manage Nodes".

            wfc.node(platform)
            {
                wfc.timestamps
                {
                    for (stage in stages)
                    {
                        print 'Running stage ' + stage.name + ' on ' +
                            platform

                        // This does return a usable error code but runStage()
                        // should have dealt with handling the error already.
                        stage.run()

                        // Doing this causes this build to stop at the first
                        // stage that is unstable or has outright failed.
                        // Otherwise the stage will be marked as unstable or
                        // failed and the build will continue.
                        if (wfc.currentBuild.result == 'UNSTABLE' ||
                            wfc.currentBuild.result == 'FAILURE')
                        {
                            print 'Current build result is ' +
                                wfc.currentBuild.result + ', exiting early'

                            break
                        }
                    }
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