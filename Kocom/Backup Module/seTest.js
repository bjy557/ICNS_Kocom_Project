var vertx = require('vertx');
var container = require('vertx/container');

var eb = vertx.eventBus;
var key = "g2PYYeRkm4XwNs5SkT%2BEm6ZWuLXQCBNLJ4jdEH43rTuU0WjKjo";

var mqttconf={
	host : 'localhost',
	port : 1883,
    key : key,
	clientID : "collector",
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
        "action" : "registor",
        "tgID" : "TG01"
    });

    eb.send("mqttclient",{
        "action" : "subscribe",
        "topic" : "TGdata"
    });
});
