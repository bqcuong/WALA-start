WALA Starter Kit
=======

### Introduction

This is a small example project to help in getting started with the
[WALA](https://github.com/wala/WALA) program analysis framework.  You
can clone and build this project to get WALA installed, and then
modify it to suit your own needs.

### Requirements

Requirements are:

  * Java 11
### Installation

Clone the repository, and then run:

    ./gradlew compileJava
    
This will pull in the WALA jars and build the sample code.

### Perform dataflow analysis for the code in `example` directory
```
./gradlew -PmainClass=com.ibm.wala.examples.drivers.CSReachingDefsDriver run --args="-scopeFile ./example/scope.txt -mainClass LFoo"
```

### Example analyses

  * [Variants of a simple dataflow analysis](https://github.com/msridhar/WALA-start/tree/master/src/main/java/com/ibm/wala/examples/analysis/dataflow), including an [example driver](https://github.com/msridhar/WALA-start/blob/master/src/main/java/com/ibm/wala/examples/drivers/CSReachingDefsDriver.java)
  * [Simple driver](https://github.com/msridhar/WALA-start/blob/master/src/main/java/com/ibm/wala/examples/drivers/ScopeFileCallGraph.java) for building a [call graph](http://wala.sourceforge.net/wiki/index.php/UserGuide:CallGraph) from a [scope file](http://wala.sourceforge.net/wiki/index.php/UserGuide:AnalysisScope)
  * [`SourceDirCallGraph` Driver](https://github.com/wala/WALA-start/blob/master/src/main/java/com/ibm/wala/examples/drivers/SourceDirCallGraph.java) for constructing a call graph from a directory of source code.
  * See the [`drivers` folder](https://github.com/wala/WALA-start/tree/master/src/main/java/com/ibm/wala/examples/drivers) for other examples.

License
-------

All code is available under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html).
