var eb = vertx.eventBus;

var pa = 'mongo-persistor';

eb.send(pa, {action: 'delete', db_name: 'scconfig', collection: 'key', matcher: {}}, function() {

  // And insert a user

  eb.send(pa, {
    action: 'save',
    db_name: 'scconfig',
    collection: 'key',
    document: {
      key: key
    }
  });
});
