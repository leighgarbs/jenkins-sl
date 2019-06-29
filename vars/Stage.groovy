#!groovy

abstract class Stage
{
    // Intended to store a reference to an implicitly-defined binding object
    // when this class is used from a script
    def binding

    String    name
    ArrayList arguments

    abstract boolean body(ArrayList arguments)

    boolean run()
    {
        return body(arguments)
    }
}
