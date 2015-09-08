var vertx = require('vertx');
var container = require('vertx/container');

var eb = vertx.eventBus;
var key = "g2PYYeRkm4XwNs5SkT%2BEm6ZWuLXQCBNLJ4jdEH43rTuU0WjKjo";

var mqttconf={
    host : 'localhost',
    port : 1883,
    clientID : 'collector',
    key : key,
	dbConfig : {
			    'host' : 'localhost',
			    'port' : 30000
	}
};

container.deployModule('icns.kocom~mqtt-client~0.1', mqttconf, 1, function(err) { // deploy public data collector module
    if(err!=null){
        err.printStackTrace(); // error print;
    }

    eb.send("mqttclient",{
        "action" : "publish",
        "topic" : "EPL",
        "document" : {"Type":"EPL", "Event_Num":"E01", "Event":"Select temperature from Lack2 where temperature >24 and light >2 ","ListenerType":"Actuator","Act_type":"window"} 
    });
});
