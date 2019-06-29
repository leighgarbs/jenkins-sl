#!groovy

class Checkout extends Stage
{
    groovy.lang.Binding binding

    boolean body(ArrayList arguments)
    {
        print binding.currentBuild.result
    }
}
