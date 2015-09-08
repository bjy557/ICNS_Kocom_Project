var vertx = require('vertx');
var container = require('vertx/container');

var eb = vertx.eventBus;
var key = "1a12axz125a";

var mqttconf={
	host : 'localhost',
	port : 1883,
    	topic : key,
	clientID : "collector"
};

container.deployModule('icns.hk~AES-MQTT~0.1', mqttconf, 1, function(err) { // deploy public data collector module
    if(err!=null){
        err.printStackTrace(); // error print;
    }
    eb.send("MQTTnAES",{
        "action" : "registor",
        "tgID" : "TG01",
	"secureNum" : 123456
    });
});
