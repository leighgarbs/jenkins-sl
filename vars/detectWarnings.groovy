#!groovy

def call(parser)
{
  warnings canComputeNew:           true,
           canResolveRelativePaths: false,
           categoriesPattern:       '',
           consoleParsers:          [[parserName: parser]],
           defaultEncoding:         '',
           excludePattern:          '',
           healthy:                 '',
           includePattern:          '',
           messagesPattern:         '',
           unHealthy:               ''

  // This can't normally fail
  return 0
}
