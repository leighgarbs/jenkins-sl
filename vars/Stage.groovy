xf#!groovy

// A Stage has three properties: name, body, args
// name - The name of the stage, shows up in the header row of Stage View
// body - Jenkins Groovy code defining what the stage does
// args - List of string arguments to passed to the body when it runs

class Stage
{
    Stage(name, body, args = [])
    {
        this.name = name
        this.body = body
        this.args = args
    }

    // Name of the stage; will show up in the header row of the pipeline stages
    // chart
    String name

    // A closure defining what should happen when the stage is run.  At a high
    // level a closure is something like a function.  This body closure can be
    // executed by executing it's "call" function (ex. body.call()).  Arguments
    // to the body function are passed in just as arguments would be
    // traditionally.
    org.jenkinsci.plugins.workflow.cps.CpsScript body

    // List of string arguments given to the "body" closure when it runs
    ArrayList args
}
