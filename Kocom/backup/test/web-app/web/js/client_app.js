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

function CartController($scope, $filter) {

  $scope.items = [];
  $scope.orderSubmitted = false;
  $scope.username = '';
  $scope.password = '';
  $scope.loggedIn = false;
  $scope.tgid = '';
  $scope.publicAddress = '';
  $scope.APIkey = '';
  $scope.period = '';
  $scope.message = '';
  $scope.topic = '';

  var eb = new vertx.EventBus(window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/eventbus');

  eb.onopen = function() {

  };

  eb.onclose = function() {
    eb = null;
  };

  $scope.tgregister = function() {
    if ($scope.tgid.trim() != '') {
      eb.send('mqttclient', {"action" : "registor", "tgID" : $scope.tgid});
    } else {
      alert('invalid Thin-gateway id');
    }
  };

  $scope.push = function() {
    if ($scope.topic.trim() != '' && $scope.message.trim() != '') {
      eb.send('mqttclient', {
        "action" : "publish",
        "topic" : $scope.topic,
        "document" : $scope.message
      });
    } else {
      alert('invalid data');
    }
  };

  $scope.pdregister = function() {
    if ($scope.publicAddress.trim() != '' &&$scope.APIkey.trim() != '' && $scope.period.trim() != '') {
      eb.send("pdCollector", {
            "url" : $scope.publicAddress,
            "apikey" : $scope.APIkey,
            "collection" : "test",
            "dbconfig" : {
              "host" : "localhost",
              "port" : 30000,
              "db_name" : "publicdata"
            },
            "period" : $scope.period
      });
      alert($scope.publicAddress + " " + $scope.APIkey + " " + $scope.period);
    } else {
      alert('invalid publicdata infomation');
    }
  };

  $scope.login = function() {
    if ($scope.username.trim() != '' && $scope.password.trim() != '') {
      eb.send('vertx.basicauthmanager.login', {username: $scope.username, password: $scope.password}, function (reply) {
        if (reply.status === 'ok') {
          $scope.loggedIn = true;
          $scope.$apply();
        } else {
          alert('invalid login');
        }
      });
    }
  };
}