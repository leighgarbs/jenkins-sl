#!groovy

package stage

class StageCheckout extends Stage
{
    ArrayList<String> repos

    // Constructor
    StageCheckout(WorkflowScript wfscript,
                  String name = 'CHECKOUT',
                  ArrayList<String> repos = [])
    {
        // Satisfy the parent constructor
        super(wfscript, name)

        this.repos = repos
    }

    boolean body()
    {
        print YAY
        return false
    }
}
