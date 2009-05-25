How to build LomPad
-------------------

To compile and build LomPad, you must have Apache Ant build tool (http://ant.apache.org/)
Once ant is installed, make a build.properties file from sample-build.properties.
If needed, you can change some property values.
To build the application, at the command line, in current directory, type :

ant 

This action produce an executable Jar (LomPad.jar) that can be launched on different OS.



How to make a .exe file (only for Windows)
------------------------------------------

Copy the file LomPad.jar on directory /misc/windows and launch makeExe.bat. 
LomPad.exe will be produced.
This module is based on Jelude (http://www.sfu.ca/~tyuen/jelude/)
