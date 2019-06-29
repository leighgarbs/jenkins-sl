#!groovy

class Checkout extends Stage
{
    boolean body(ArrayList arguments)
    {
        print currentBuild.result
    }
}
