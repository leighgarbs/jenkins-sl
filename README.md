**[jenkins-sl](https://github.com/leighgarbs/jenkins-sl)** -
  Jenkins shared library for continuous integration and testing
===============================================================

For personal project use.  A Jenkins shared library defining continuous integration and testing scripts used by other projects.

## Features ##
* Designed for use in pipelines
* Supported stages:
  * Checkout (Git only)
  * Build (release or debug)
  * Test ([CTest](https://gitlab.kitware.com/cmake/community/wikis/home#ctest) only)
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
