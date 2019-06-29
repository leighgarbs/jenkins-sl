#!groovy

abstract class Stage
{
    String    name
    ArrayList arguments

    abstract boolean body(ArrayList arguments)

    boolean run()
    {
        return body(arguments)
    }
}
