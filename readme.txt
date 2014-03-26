How to build LomPad
-------------------

To compile and build LomPad, you must have Maven (http://maven.apache.org/)

To build the application, at the command line, in current directory, type :

mvn package 

This action produce an executable Jar (target/lompad-1.0-jar-with-dependencies.jar) that can be launched on different OS.



How to make a .exe file (only for Windows)
------------------------------------------

Copy the file lompad-1.0-jar-with-dependencies.jar on directory /misc/windows and launch makeExe.bat. 
LomPad.exe will be produced.
This module is based on Jelude (http://www.sfu.ca/~tyuen/jelude/)
