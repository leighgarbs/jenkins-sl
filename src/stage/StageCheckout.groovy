#!groovy

package stage

class StageCheckout extends Stage
{
    ArrayList<String> repos

    // Constructor
    StageCheckout(def               wfc,
                  ArrayList<String> repos,
                  String            name = 'CHECKOUT')
    {
        // Satisfy the parent constructor
        super(wfc, name)

        this.repos = repos
    }

    boolean body()
    {
        return true
    }
}
