var vertx = require('vertx');
var container = require('vertx/container');
var eb = vertx.eventBus;

var dbconfig = {
    'host' : '163.180.117.72',
    'port' : 50000,
    'db_name' : 'test'
};
collection = 'data';
url = 'openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?numOfRows=1&pageNo=1&stationName=%EC%86%A1%ED%8C%8C%EA%B5%AC&dataTerm=DAILY&';
apiKey = 'g2PYYeRkm4XwNs5SkT%2BEm6ZWuLXQCBNLJ4jdEH43rTuU0WjKjo%2B2IBtyAr1EJmS2QqsImnnT3RCr5RNBZ0d25A%3D%3D';
period = 0.005 // hours
// test data

container.deployModule('icns.kocom~publicdatacollector~1.0', 1, function(err,deployID) { // deploy public data collector module
    if(err!=null){
        err.printStackTrace(); // error print;
    }
    else{
        eb.send('pdCollector', {
            "url" : url,
            "apikey" : apiKey,
            "collection" : collection,
            "dbconfig" : dbconfig,
            "period" : period
        }); // send data to public data collector by event bus
    }
});
