#!groovy

package stage

class StageCheckout extends Stage
{
    ArrayList<String> repos

    // Constructor
    StageCheckout(name = 'CHECKOUT', repos = [])
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
