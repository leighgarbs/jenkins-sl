#!groovy

class Checkout extends Stage
{
    boolean body(ArrayList arguments)
    {
        print binding.currentBuild.result
    }
}
