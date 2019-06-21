#!groovy                                                                            

def call(pipeline, platform)
{
    // Allocate a node for this whole pipeline.  Entire pipelines get assigned      
    // to nodes this way.  It is possible to assign workflow to nodes at an         
    // increased resolution (such as assigning individual stages to nodes) but      
    // this seems sufficient for now.                                               

    // This assumes nodes are labeled with the name of the platform they run.       
    // Labeling nodes can be done using the Jenkins GUI under "Manage Jenkins ->    
    // Manage Nodes".                                                               

    node(platform)
    {
        timestamps
        {
            for (def i = 0; i < pipeline.size(); i++)
            {
                print 'Running stage ' + pipeline[i].name + ' on ' + platform

                // This does return a usable error code but runStage() should       
                // have dealt with handling the error already.                      
                runStage(pipeline[i])

                // Doing this causes this build to stop at the first stage that     
                // is unstable or has outright failed.  Otherwise the stage will    
                // be marked as unstable or failed and the build will continue.     
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