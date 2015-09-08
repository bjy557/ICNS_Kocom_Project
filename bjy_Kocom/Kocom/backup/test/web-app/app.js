/*
This verticle contains the configuration for our application and co-ordinates
start-up of the verticles that make up the application.
 */


var vertx = require('vertx');
var container = require('vertx/container');
var eb = vertx.eventBus;

// Our application config - you can maintain it here or alternatively you could
// stick it in a conf.json text file and specify that on the command line when
// starting this verticle

// Configuration for the web server
var webServerConf = {

  // Normal web server stuff

  port: 30001,
  host: '163.180.117.178',
  //ssl: true,

  // Configuration for the event bus client side bridge
  // This bridges messages from the client side to the server side event bus
  bridge: true,

  // This defines which messages from the client we will let through
  // to the server side
  inbound_permitted: [
    // Allow calls to login and authorise
    {
      address: 'vertx.basicauthmanager.login'
    },
    {
      address: 'mqttclient'
    },
    {
      address: 'pdCollector'
    },
    // Allow calls to get static album data from the persistor
    {
      address : 'vertx.mongopersistor',
      match : {
        action : 'find',
        collection : 'albums'
      }
    },
    // And to place orders
    {
      address : 'vertx.mongopersistor',
      requires_auth : true,  // User must be logged in to send let these through
      match : {
        action : 'save',
        collection : 'orders'
      }
    }
  ],

  // This defines which messages from the server we will let through to the client
  outbound_permitted: [
    {}
  ]
};

// Now we deploy the modules that we need

// Deploy a MongoDB persistor module

var dbconfig = {
    "host" : "163.180.117.178",
    "port" : 30000,
    "dbname" : "scconfig"
    };
var key = "asdf12";

var mqttconf={
  host : '163.180.117.178',
  port : 1883,
  key : key,
  clientID : "collector",
  dbConfig : dbconfig
};

container.deployModule('icns.kocom~mongo-persistor~1.0',dbconfig, function() {

  // And when it's deployed run a script to load it with some reference
  // data for the demo
  load('static_data.js');
});

// Deploy an auth manager to handle the authentication

container.deployModule('io.vertx~mod-auth-mgr~2.0.0-final');


container.deployModule('icns.kocom~publicdatacollector~1.0', function(err) { // deploy public data collector module
    if(err!=null){
        err.printStackTrace(); // error print;
    }
});

container.deployModule('icns.kocom~mqtt-client~0.1', mqttconf, 1, function(err) { // deploy public data collector module
    if(err!=null){
        err.printStackTrace(); // error print;
    }
    else
    {
      eb.send("mqttclient",{
        "action" : "subscribe",
        "topic" : "TGdata"
      });
      eb.send("mqttclient",{
        "action" : "subscribe",
        "topic" : "LOG"
      });
    }
});


// Start the web server, with the config we defined above

container.deployModule('io.vertx~mod-web-server~2.0.0-final', webServerConf);
