#!groovy

def call(args)
{
  detectWarnings("GNU Make + GNU C Compiler (gcc)")
  detectWarnings("Clang (LLVM based)")

  // This stage can't normally fail
  return 0
}
