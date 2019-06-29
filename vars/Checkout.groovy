#!groovy

class Checkout extends Stage
{
    boolean body(ArrayList arguments)
    {
        print new Binding().getProperty('result')
    }
}
