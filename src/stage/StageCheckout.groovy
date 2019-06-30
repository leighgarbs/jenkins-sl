#!groovy

package stage

class StageCheckout extends Stage
{
    ArrayList<String> repos

    // Constructor
    StageCheckout(def wfscript,
                  ArrayList<String> repos,
                  String name = 'CHECKOUT')
    {
        // Satisfy the parent constructor
        super(wfscript, name)

        this.repos = repos
    }

    boolean body()
    {
        print 'YAY'
        return true
    }
}
