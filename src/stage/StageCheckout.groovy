#!groovy

package stage

class StageCheckout extends Stage
{
    ArrayList<String> repos

    boolean body()
    {
        print YAY
        return false
    }
}
