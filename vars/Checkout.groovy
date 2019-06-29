#!groovy

class Checkout extends Stage
{
    Checkout(name = 'CHECKOUT', arguments = [])
    {
        this.name = name
        this.arguments = arguments
    }

    boolean body(ArrayList arguments)
    {
    }
}
