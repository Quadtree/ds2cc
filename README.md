ds2cc
=====

Instructions for setting up a local dev version:

Install Eclipse 4.3 Kepler
Install the Google Plugin https://dl.google.com/eclipse/plugin/4.3

Download GWT 2.6.1 and unzip it somewhere

Go to Window -> Preferences -> Google -> Web Toolkit in eclipse, select add and then navigate to where the GWT was unzipped.

Import the cloned project into your Eclipse workspace.

Make sure you are using a Java 7 JDK. GWT is not yet compatible with Java 8.

Verify that the /war/WEB-INF/lib directory contains the JARs. If it doesn't, try switching between different GWT versions...?

Right click on the project and go to Google -> GWT Compile. Accept the defaults and compile.

Run the project as a Google Web Application. Follow the link in the console to open it in a browser. You may also need the GWT dev plugin in the browser.

Installing Data
===
Once the dev version is running on your machine, go to http://127.0.0.1:8888/TSVImporter.html?gwt.codesvr=127.0.0.1:9997

Copy the contents of rawdata/data1.tsv into the box and select "Begin Upload"
