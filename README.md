ds2cc
=====

Instructions for setting up a local dev version:

Install Eclipse 4.3 Kepler

* Install the Google Plugin: https://dl.google.com/eclipse/plugin/4.3
* Install the IvyDE plugin and "Apache Ivy Ant Tasks": https://www.apache.org/dist/ant/ivyde/updatesite.

Download GWT 2.6.1 (2.6.0 will probably not work) and unzip it somewhere

Go to Window -> Preferences -> Google -> Web Toolkit in eclipse, select add and then navigate to where the GWT was unzipped.

Import the cloned project into your Eclipse workspace.

Make sure you are using a Java 7 JDK. GWT is not yet compatible with Java 8.

Verify that the /war/WEB-INF/lib directory contains the JARs. If it doesn't, try switching between different GWT versions...?

Right click on the project and go to Google -> GWT Compile. Accept the defaults and compile.

Right click on the build.xml file and select Run As -> Ant Build.

Run the project as a Google Web Application. Follow the link in the console to open it in a browser. You may also need the GWT dev plugin in the browser.

Installing Data
===
Once the dev version is running on your machine, go to http://127.0.0.1:8888/importer/xml

Select the rawdata/data1.xml file and select "Submit"
