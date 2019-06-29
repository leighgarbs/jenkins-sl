#!groovy

def call(pipeline, platform, cleanWorkspace)
{
    // We have to have a list of all the stage names before we run any of the
    // stages.  This is a little awkward since it forces us to loop
    // over the same list of stages twice.
    stageNames = getStageNames(pipeline)

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
        // Allocate a node for this whole pipeline.  Entire pipelines get
        // assigned to nodes this way.  It is possible to assign workflow to
        // nodes at an increased resolution (such as assigning individual stages
        // to nodes) but this seems sufficient for now.

        // This assumes nodes are labeled with the name of the platform they
        // run.  Labeling nodes can be done using the Jenkins GUI under "Manage
        // Jenkins -> Manage Nodes".

        node(platform)
        {
            timestamps
            {
                // Clean the workspace if that's what the user wants
                if (cleanWorkspace)
                {
                    cleanWs()
                }

                for (stage in pipeline)
                {
                    print 'Running stage ' + stage.name + ' on ' +
                        platform

                    // This does return a usable error code but runStage()
                    // should have dealt with handling the error already.
                    stage.run()

                    // Doing this causes this build to stop at the first stage
                    // that is unstable or has outright failed.  Otherwise the
                    // stage will be marked as unstable or failed and the build
                    // will continue.
                    if (currentBuild.result == 'UNSTABLE' ||
                        currentBuild.result == 'FAILURE')
                    {
                        print 'Current build result is ' + currentBuild.result +
                            ', exiting early'

                        break
                    }
                }
            }
        }
    }
}
