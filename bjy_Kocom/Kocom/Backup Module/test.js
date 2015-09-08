var vertx = require('vertx');
var container = require('vertx/container');
var eb = vertx.eventBus;
var console = require('vertx/console');

var dbconfig = {
    "host" : "163.180.117.178",
    "port" : 30000,
    "db_name" : "test"
};

container.deployModule('icns.kocom~mongo-persistor~1.0', 1, dbconfig, function(err,deployID) { // deploy public data collector module
    if(err!=null){
        err.printStackTrace(); // error print;
    }
    else{
		console.log("test");
		eb.send('vertx.kocom~mongo-persisotr~1.0',{
			action: 'save',
			db_name : 'test',
			collection: 'test',
			document: {"datatime":"2015-05-15 16:00","so2value":0.006,"covalue":0.7,"o3value":0.061,"no2value":0.023,"pm10value":106,"khaivalue":136,"khaigrade":3,"so2grade":1,"cograde":1,"o3grade":2,"no2grade":1,"pm10grade":3}
		},
		function(reply){
			console.log(reply + "test2");
		});
		eb.send('vertx.kocom~mongo-persisotr~1.0',{
			action : 'find',
			db_name : 'test',
			collection : 'test',
			matcher: {} },
		function(reply){
			console.log(JSON.stringify(reply));
		});
    }
});