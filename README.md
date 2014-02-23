Soko++ - Example Solver plugin
============================

Soko++ for Android supports Solver plug-ins. Here you can find an example plug-in.


Description
-----------

This example app contains all solutions to Soko++'s built in levels. The solutions are simply read from a resource file.


Intent parameters
-----------------

Soko++ calls the plug-ins with a specially defined intent action; `nl.joriswit.sokosolver.SOLVE`. This intent gets one extra string parameter called `LEVEL`. This is the level in XSB format. When you find a solution, set `RESULT_OK` as the activity result, and add a extra parameter called `SOLUTION`, with the UDLR string.


Try it
------

This plug-in is also available on [Google Play][]

[Google Play]: https://play.google.com/store/apps/details?id=nl.joriswit.soko.builtinlevelsolutions
