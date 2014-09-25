Let's Go Phishing!
=============

Two Android applications and a small web-server created for the 2014 Capstone project in CSC3003S at UCT. The goal of the 'Let's Go Phishing' (abbreviated 'LEAP' for some reason) project was to come up with an attack model based on [phishing](http://en.wikipedia.org/wiki/Phishing "Wikipedia on phishing") and then a countermeasure to said model. The full report can be found [here](/path/to/img.jpg "Capstone Project LEAP Final Report").

### Modules

**Phishlight**
The attacking model takes base in a simple utility application. To restrict our scope, we decided to focus on the exploiting functionality, rather than the utility functionality. This led us to the concept of a simple flashlight application, that captured and extracted data from the victim’s cell phone.

**Phishlight Server**
To extract and store the information gathered by the Phishlight application externally, we decided to set up a web server.

**Phishguard**
The countermeasure to the attack model was an application that could automatically detect and warn users about outgoing network traffic on the device.

### Dependencies

**Android Applications:**
- Google gson
- Android 

**Server**
- Node
- Express
- MongoDB

Please contact edxrik001 in case you want to set up the web server for yourself and don't know how to. Instructions on how to do so have intentionally been left out of this document.
