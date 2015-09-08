var eb = new vertx.EventBus(window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/eventbus');

function HeaderController($scope) {

    $scope.dbrefresh = function () {
        alert('called!');
        eb.send('icns.kocom~mongo-persistor~1.0', {
            action: 'find',
            db_name: 'userdata',
            collection: 'group1',
            limit: 1,
            matcher: {}},
        function (reply) {
            if (reply.status === 'ok') {
                $scope.pdata = reply.results;
                alert(JSON.stringify(reply.results));
                //scope.$apply();
            } else {
                console.error(reply);
                alert("error");
            }
        });
        alert('called!2');
    };

}

var JqMap = function () {
    this.map = new Object();
}

JqMap.prototype = {
    put: function (key, value) {
        this.map[key] = value;
    },
    get: function (key) {
        return this.map[key];
    },
    print: function () {
        for (var prop in this.map) {
            console.log(prop + '--' + map[prop]);
        }
    },
    containsKey: function (key) {
        return key in this.map;
    },
    containsValue: function (value) {
        for (var prop in this.map) {
            if (this.map[prop] == value)
            {
                return true;
            }
        }
        return false;
    },
    clear: function () {
        for (var prop in this.map)
        {
            delete this.map[prop];
        }
    },
    remove: function (key) {
        delete this.map[key];
    },
    Keys: function () {
        var arKey = new Array();
        for (var prop in this.map)
        {
            arKey.push(prop);
        }
        return arKey;
    },
    values: function () {
        var arVal = new Array();
        for (var prop in this.map)
        {
            arval.push(this.map[prop]);
        }
        return arVal;
    },
    size: function () {
        var count = 0;
        for (var prop in this.map)
        {
            count++;
        }
        return count;
    }
}

function TGStatusController($scope) {
    $scope.RackList = ['test1', 'test2', 'test3'];
    $scope.ActList = [];
    $scope.TG_List = [];
    $scope.SensorList = [];
    $scope.isVisiable = true;
    var oMap = new JqMap();
    oMap.put("Act01", "on");
    oMap.put("Act02", "off");
    oMap.put("Act03", "on");
    $scope.rackClicked = function (idx, rack) {
        console.log(idx + ' - ' + rack);
    };
    $scope.printActList = function () {
        oMap.print();
    };
}

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




    eb.onopen = function () {

    };

    eb.onclose = function () {
        eb = null;
    };

    $scope.tgregister = function () {
        if ($scope.tgid.trim() != '') {
            eb.send('mqttclient', {"action": "registor", "tgID": $scope.tgid});
        } else {
            alert('invalid Thin-gateway id');
        }
    };

    $scope.eplt1 = JSON.parse('{"type":"EPL", "event_num":"E01", "event":"Select light from LACK01 where light >1.4","listenerType":"actuator","actuator_id":"act1","action":"on"}');
    $scope.eplt2 = JSON.parse('{"type":"EPL", "event_num":"E02", "event":"Select light from LACK01 where light <1.0","listenerType":"actuator","actuator_id":"act1","action":"off"}');
    $scope.actn = JSON.parse('{"type":"act","actuator_id":"act2","action":"on"}');
    $scope.actf = JSON.parse('{"type":"act","actuator_id":"act1","action":"off"}');


    $scope.epl1 = function () {
        eb.send('mqttclient', {
            "action": "publish",
            "topic": "EPL",
            "document": $scope.eplt1
        });
    };
    $scope.epl2 = function () {
        eb.send('mqttclient', {
            "action": "publish",
            "topic": "EPL",
            "document": $scope.eplt2
        });
    };

    $scope.acton = function () {
        eb.send('mqttclient', {
            "action": "publish",
            "topic": "ACT",
            "document": $scope.actn
        });
    };

    $scope.actoff = function () {
        eb.send('mqttclient', {
            "action": "publish",
            "topic": "ACT",
            "document": $scope.actf
        });
    };

    $scope.push = function () {
        eb.send('mqttclient', {
            "action": "publish",
            "topic": $scope.topic,
            "document": JSON.parse($scope.message)
        });
    };

    $scope.pdregister = function () {
        if ($scope.publicAddress.trim() != '' && $scope.APIkey.trim() != '' && $scope.period.trim() != '') {
            eb.send("pdCollector", {
                "url": $scope.publicAddress,
                "apikey": $scope.APIkey,
                "collection": "test",
                "dbconfig": {
                    "host": "localhost",
                    "port": 30000,
                    "db_name": "publicdata"
                },
                "period": $scope.period
            });
        } else {
            alert('invalid publicdata infomation');
        }
    };

    $scope.login = function () {
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