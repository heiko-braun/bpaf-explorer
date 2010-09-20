
How to run the examples
=======================

[Database Configuration]

Currently hibernate is configured to work with a MySQL database.
The configuration can be found here:

    ./model/src/main/resources/hibernate.cfg.xml

The most simple approach would be to install MySQL.
Otherwise you'd need to reconfigure hibernate to work
with a different DB.

[Test Data]

Once that is done you need to populate the tables with
some test data, that will be used to drive the UI demo.
There is an Emulation class, that runs to process definitions
and creates the corresponding history records:

    ./emu/src/main/java/org/jboss/bpm/monitor/emu/Emulation.java

Simply run from your IDE for several minutes and you are done.

[Prototype UI]

You can find the prototype UI within 'gui/war'.
Launch it with 'mvn gwt:run' and you are done.
Once the GWT hosted mode comes up, point your browser to


    http://localhost:8888/App.html?gwt.codesvr=192.168.0.10:9997

Login with admin:admin


    


    


