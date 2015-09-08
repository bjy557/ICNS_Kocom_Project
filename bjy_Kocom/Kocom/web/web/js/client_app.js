/*
 * Copyright 2011-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

function Kocom($scope, $filter) {

  $scope.username = '';
  $scope.password = '';
  $scope.loggedIn = false;
  $scope.tgid = '';
  $scope.publicAddress = 'openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?numOfRows=1&pageNo=1&stationName=%EC%86%A1%ED%8C%8C%EA%B5%AC&dataTerm=DAILY&';
  $scope.APIkey = 'g2PYYeRkm4XwNs5SkT%2BEm6ZWuLXQCBNLJ4jdEH43rTuU0WjKjo%2B2IBtyAr1EJmS2QqsImnnT3RCr5RNBZ0d25A%3D%3D';
  $scope.DataName = '대기정보데이터';
$scope.sn = 1;
  $scope.period = '1';
  $scope.message = '';
  $scope.topic = '';
  $scope.collection = 'airinfo';
   $scope.pdata= new Array();


  var eb = new vertx.EventBus(window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/eventbus');

 
  eb.onopen = function() {
    };

  eb.onclose = function() {
    eb = null;
  };

  $scope.tgregister = function() {
    if ($scope.tgid.trim() != '') {
      eb.send('mqttclient', {"action" : "registor", "tgID" : $scope.tgid, "secureNum" : Number($scope.sn)});
    } else {
      alert('invalid Thin-gateway id');
    }
  };

  $scope.eplt1 = JSON.parse('{"type":"EPL", "event_num":"E01", "event":"Select Light from NODE01 where Light >2.0","listenerType":"actuator","actuator_id":"act1","action":"on"}');
  $scope.eplt2 = JSON.parse('{"type":"EPL", "event_num":"E02", "event":"Select Light from NODE01 where Light <1.2","listenerType":"actuator","actuator_id":"act1","action":"off"}');
  $scope.actn = JSON.parse('{"type":"act","actuator_id":"act2","action":"on"}');
  $scope.actf = JSON.parse('{"type":"act","actuator_id":"act1","action":"off"}');


  $scope.epl1 = function() {
      eb.send('mqttclient',{
        "action" : "publish",
        "topic" : "EPL",
        "document" : $scope.eplt1
      });
  };
  $scope.epl2 = function() {
      eb.send('mqttclient',{
        "action" : "publish",
        "topic" : "EPL",
        "document" : $scope.eplt2
      });
  };

  $scope.acton = function() {
      eb.send('mqttclient', {
        "action" : "publish",
        "topic" : "ACT",
        "document" : $scope.actn
      });
  };

  $scope.actoff = function() {
      eb.send('mqttclient', {
        "action" : "publish",
        "topic" : "ACT",
        "document" : $scope.actf
      });
  };

  $scope.push = function() {
      eb.send('mqttclient', {
        "action" : "publish",
        "topic" : $scope.topic,
        "document" : JSON.parse($scope.message)
      });
  };

  $scope.pdregister = function() {
    if ($scope.publicAddress.trim() != '' &&$scope.APIkey.trim() != '' && $scope.period.trim() != '') {
      eb.send("pdCollector", {
            "url" : $scope.publicAddress,
            "apikey" : $scope.APIkey,
            "collection" : $scope.DataName,
            "dbconfig" : {
              "host" : "localhost",
              "port" : 30000,
              "db_name" : "publicdata"
            },
            "period" : $scope.period
      });
    } else {
      //alert('invalid publicdata infomation');
    }
  };

  $scope.login = function() {
    if ($scope.username.trim() != '' && $scope.password.trim() != '') {
      eb.send('vertx.basicauthmanager.login', {username: $scope.username, password: $scope.password}, function (reply) {
        if (reply.status === 'ok') {
          $scope.loggedIn = true;
          $scope.$apply();
        } else {
          //alert('invalid login');
        }
      });
    }
  };
  $scope.dbrefresh = function() {
      if($scope.collection === undefined)
	 $scope.collection = 'airinfo';
      eb.send('mongo-persistor', { action: 'find', db_name: 'publicdata', collection : $scope.collection , limit: 20, matcher: {}, sort: { datatime: -1 } },
	function(reply){
	if(reply.status === 'ok') {
		$scope.pdata=reply.results;
		$scope.$apply();
	} else {
		//alert('Failed to retrieve data : ' + reply.message);
	}
     });
  };
}