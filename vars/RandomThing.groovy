#!groovy

class RandomThing //extends Stage
{
    ArrayList repos

    boolean run()
    {
        print binding.currentBuild.result
        print repos
    }
}
