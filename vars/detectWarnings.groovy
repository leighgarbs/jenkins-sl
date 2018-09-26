#!groovy

def call(parser)
{
  warnings canComputeNew: false,
           canResolveRelativePaths: false,
           categoriesPattern: '',
           consoleParsers: [[parserName: parser]],
           defaultEncoding: '',
           excludePattern: '',
           healthy: '',
           includePattern: '',
           messagesPattern: '',
           unHealthy: ''
}
