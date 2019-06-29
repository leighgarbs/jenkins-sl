#!groovy

class Checkout extends Stage
{
    Checkout(binding)
    {
        this.binding = binding
    }

    boolean body(ArrayList arguments)
    {
        print binding.currentBuild.result
    }
}
