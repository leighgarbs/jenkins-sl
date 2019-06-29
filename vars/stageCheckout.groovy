#!groovy

class StageCheckout extends Stage
{
    ArrayList repos

    boolean run()
    {
        print binding.currentBuild.result
        print repos
    }
}
