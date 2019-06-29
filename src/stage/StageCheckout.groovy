#!groovy

import groovy.lang.String

class StageCheckout extends Stage
{
    ArrayList<String> repos

    boolean body()
    {
        return false
    }
}
