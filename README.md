**[jenkins-sl](https://github.com/leighgarbs/jenkins-sl)** -
  Jenkins shared library for continuous integration and testing
===============================================================

For personal project use.  A Jenkins shared library defining continuous integration and testing scripts used by other projects.

## Features ##
* Designed for use in [pipelines](https://jenkins.io/doc/book/pipeline/)
* Supported stages:
  * Checkout (Git only)
  * Build (release or debug)
  * Test ([CTest](https://gitlab.kitware.com/cmake/community/wikis/home#ctest) only)
  * [Cppcheck](http://cppcheck.sourceforge.net/)
  * Valgrind (via [CTest memcheck](https://cmake.org/cmake/help/latest/manual/ctest.1.html#ctest-memcheck-step))
  * [Clang static analyzer](https://clang-analyzer.llvm.org/)
  * Build warnings

## Goals ##
* Windows support
* Parallel stage execution

## Style ##
* [Groovy-Emacs-Modes](https://github.com/Groovy-Emacs-Modes/groovy-emacs-modes) Emacs major mode for Groovy
