#!groovy

package stage

class StageCheckout extends Stage
{
    StageCheckout(name = 'CHECKOUT', repos = [])
    {
        this.name = name
        this.repos = repos
    }

    ArrayList<String> repos

    boolean body()
    {
        print YAY
        return false
    }
}
