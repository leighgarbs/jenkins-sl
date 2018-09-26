#!groovy

def call(args)
{
  detectWarnings("GNU Make + GNU C Compiler (gcc)")
  detectWarnings("Clang (LLVM based)")
}
