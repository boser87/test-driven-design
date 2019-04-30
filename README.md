# test-driven-development
Test driven development (TDD) exercise inspired by the book Growing Object Oriented Software Guided By Tests.

Openfire version is 3.6 (https://github.com/igniterealtime/Openfire/releases/tag/v3.6.0).

In order for the end to end tests to work on localhost, correct XMPP hostname (hostname to connect to local Openfire)
 must be set in the code.
 
-    public static final String XMPP_HOSTNAME = "192.168.1.207";
+    public static final String XMPP_HOSTNAME = "Stefanos-MacBook-Pro.local";

Probably best idea is to set Openfire's hostname to localhost during installation and let XMPP_HOSTNAME be always 
localhost in the code. 
