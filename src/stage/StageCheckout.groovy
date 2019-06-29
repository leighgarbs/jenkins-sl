#!groovy

package stage

class StageCheckout extends Stage
{
    ArrayList<String> repos

    // Constructor
    StageCheckout(jenkinsfileContext,
                  String name = 'CHECKOUT',
                  ArrayList<String> repos = [])
    {
        // Satisfy the parent constructor
        super(jenkinsfileContext, name)

        this.repos = repos
    }

    boolean body()
    {
        print YAY
        return false
    }
}
