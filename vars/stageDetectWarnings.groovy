#!groovy

def call(args)
{
  warnings canComputeNew: false,
           canResolveRelativePaths: false,
           categoriesPattern: '',
           consoleParsers: [[parserName: args[0]]],
           defaultEncoding: '',
           excludePattern: '',
           healthy: '',
           includePattern: '',
           messagesPattern: '',
           unHealthy: ''
}
