#!groovy

abstract class Stage
{
    String name

    // Runs the body in the appropriate workflow code context
    boolean run()
    {
        return false
    }

    abstract boolean body()
}
