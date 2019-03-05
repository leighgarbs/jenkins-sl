**[jenkins-sl](https://github.com/leighgarbs/jenkins-sl)** -
  Jenkins continuous integration and testing shared library
===========================================================

For personal project use.  A Jenkins shared library defining continuous integration and testing scripts used by other projects.  **Some bits contain content specific to my personal environment.**

## Features ##
* Designed for use in pipelines
* Supported stages:
  * Checkout (Git only)
  * Build (release or debug)
  * Test (CTest only)
  * Cppcheck
  * Valgrind (via CTest memcheck)
  * Clang static analyzer
  * Build warnings

## Goals ##
* Windows support
* Parallel stage execution

## Style ##
* [Groovy-Emacs-Modes](
  https://github.com/Groovy-Emacs-Modes/groovy-emacs-modes)
  Emacs major mode for Groovy
