#!groovy

package stage

class StageCheckout extends Stage
{
    ArrayList<String> repos

    // Constructor
    StageCheckout(String name = 'CHECKOUT', ArrayList<String> repos = [])
    {
        this.name = name
        this.repos = repos
    }

    boolean body()
    {
        print YAY
        return false
    }
}
