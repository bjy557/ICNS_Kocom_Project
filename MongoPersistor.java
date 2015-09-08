// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MongoPersistor.java

package org.vertx.mods;

import com.mongodb.*;
import java.net.UnknownHostException;
import java.util.*;
import javax.net.ssl.SSLSocketFactory;
import org.vertx.java.busmods.BusModBase;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Container;

// Referenced classes of package org.vertx.mods:
//            MongoUtil

public class MongoPersistor extends BusModBase
    implements Handler
{

    public MongoPersistor()
    {
    }

    public void start()
    {
        super.start();
        this.address = getOptionalStringConfig("address", "vertx.mongopersistor");
        host = getOptionalStringConfig("host", "localhost");
        port = getOptionalIntConfig("port", 27017);
        dbName = getOptionalStringConfig("db_name", "default_db");
        username = getOptionalStringConfig("username", null);
        password = getOptionalStringConfig("password", null);
        readPreference = ReadPreference.valueOf(getOptionalStringConfig("read_preference", "primary"));
        int poolSize = getOptionalIntConfig("pool_size", 10);
        autoConnectRetry = getOptionalBooleanConfig("auto_connect_retry", true);
        socketTimeout = getOptionalIntConfig("socket_timeout", 60000);
        useSSL = getOptionalBooleanConfig("use_ssl", false);
        useMongoTypes = getOptionalBooleanConfig("use_mongo_types", false);
        JsonArray seedsProperty = config.getArray("seeds");
        try
        {
            com.mongodb.MongoClientOptions.Builder builder = new com.mongodb.MongoClientOptions.Builder();
            builder.connectionsPerHost(poolSize);
            builder.autoConnectRetry(autoConnectRetry);
            builder.socketTimeout(socketTimeout);
            builder.readPreference(readPreference);
            if(useSSL)
                builder.socketFactory(SSLSocketFactory.getDefault());
            if(seedsProperty == null)
            {
                ServerAddress address = new ServerAddress(host, port);
                mongo = new MongoClient(address, builder.build());
            } else
            {
                List seeds = makeSeeds(seedsProperty);
                mongo = new MongoClient(seeds, builder.build());
            }
            db = mongo.getDB(dbName);
            if(username != null && password != null)
                db.authenticate(username, password.toCharArray());
        }
        catch(UnknownHostException e)
        {
            logger.error("Failed to connect to mongo server", e);
        }
        eb.registerHandler(this.address, this);
    }

    private List makeSeeds(JsonArray seedsProperty)
        throws UnknownHostException
    {
        List seeds = new ArrayList();
        String host;
        int port;
        for(Iterator iterator = seedsProperty.iterator(); iterator.hasNext(); seeds.add(new ServerAddress(host, port)))
        {
            Object elem = iterator.next();
            JsonObject address = (JsonObject)elem;
            host = address.getString("host");
            port = address.getInteger("port").intValue();
        }

        return seeds;
    }

    public void stop()
    {
        if(mongo != null)
            mongo.close();
    }

    public void handle(Message message)
    {
        String action;
        action = ((JsonObject)message.body()).getString("action");
        if(action == null)
        {
            sendError(message, "action must be specified");
            return;
        }
        String dbtemp = getMandatoryString("db_name", message);
        if(dbtemp != null)
            db = mongo.getDB(dbtemp);
        String s;
        (s = action).hashCode();
        JVM INSTR lookupswitch 15: default 498
    //                   -1600929236: 192
    //                   -1485117506: 206
    //                   -1335458389: 220
    //                   -853167603: 234
    //                   -838846263: 248
    //                   -99083539: 262
    //                   3143097: 276
    //                   3522941: 290
    //                   94851343: 304
    //                   175177151: 318
    //                   950394699: 332
    //                   1049401505: 346
    //                   1213623391: 360
    //                   1343584750: 374
    //                   1793085032: 388;
           goto _L1 _L2 _L3 _L4 _L5 _L6 _L7 _L8 _L9 _L10 _L11 _L12 _L13 _L14 _L15 _L16
_L1:
        break; /* Loop/switch isn't completed */
_L2:
        if(!s.equals("get_collections"))
            break; /* Loop/switch isn't completed */
          goto _L17
_L3:
        if(!s.equals("collection_stats"))
            break; /* Loop/switch isn't completed */
          goto _L18
_L7:
        if(!s.equals("dropCollection"))
            break; /* Loop/switch isn't completed */
          goto _L19
_L9:
        if(s.equals("save"))
        {
            doSave(message);
            break MISSING_BLOCK_LABEL_534;
        }
        break; /* Loop/switch isn't completed */
_L13:
        if(!s.equals("collectionStats"))
            break; /* Loop/switch isn't completed */
          goto _L18
_L14:
        if(!s.equals("getCollections"))
            break; /* Loop/switch isn't completed */
          goto _L17
_L15:
        if(!s.equals("drop_collection"))
            break; /* Loop/switch isn't completed */
          goto _L19
_L6:
        if(!s.equals("update"))
            break; /* Loop/switch isn't completed */
        doUpdate(message);
        break MISSING_BLOCK_LABEL_534;
_L8:
        if(!s.equals("find"))
            break; /* Loop/switch isn't completed */
        doFind(message);
        break MISSING_BLOCK_LABEL_534;
_L5:
        if(!s.equals("findone"))
            break; /* Loop/switch isn't completed */
        doFindOne(message);
        break MISSING_BLOCK_LABEL_534;
_L16:
        if(!s.equals("find_and_modify"))
            break; /* Loop/switch isn't completed */
        doFindAndModify(message);
        break MISSING_BLOCK_LABEL_534;
_L4:
        if(!s.equals("delete"))
            break; /* Loop/switch isn't completed */
        doDelete(message);
        break MISSING_BLOCK_LABEL_534;
_L10:
        if(!s.equals("count"))
            break; /* Loop/switch isn't completed */
        doCount(message);
        break MISSING_BLOCK_LABEL_534;
_L17:
        getCollections(message);
        break MISSING_BLOCK_LABEL_534;
_L19:
        dropCollection(message);
        break MISSING_BLOCK_LABEL_534;
_L18:
        getCollectionStats(message);
        break MISSING_BLOCK_LABEL_534;
_L11:
        if(!s.equals("aggregate"))
            break; /* Loop/switch isn't completed */
        doAggregation(message);
        break MISSING_BLOCK_LABEL_534;
_L12:
        if(!s.equals("command"))
            break; /* Loop/switch isn't completed */
        runCommand(message);
        break MISSING_BLOCK_LABEL_534;
        sendError(message, (new StringBuilder("Invalid action: ")).append(action).toString());
        break MISSING_BLOCK_LABEL_534;
        MongoException e;
        e;
        sendError(message, e.getMessage(), e);
    }

    private void doSave(Message message)
    {
        String collection = getMandatoryString("collection", message);
        if(collection == null)
            return;
        JsonObject doc = getMandatoryObject("document", message);
        if(doc == null)
            return;
        String genID;
        if(doc.getField("_id") == null)
        {
            genID = UUID.randomUUID().toString();
            doc.putString("_id", genID);
        } else
        {
            genID = null;
        }
        DBCollection coll = db.getCollection(collection);
        DBObject obj = jsonToDBObject(doc);
        WriteConcern writeConcern = WriteConcern.valueOf(getOptionalStringConfig("writeConcern", ""));
        if(writeConcern == null)
            writeConcern = WriteConcern.valueOf(getOptionalStringConfig("write_concern", ""));
        if(writeConcern == null)
            writeConcern = db.getWriteConcern();
        WriteResult res = coll.save(obj, writeConcern);
        if(res.getError() == null)
        {
            if(genID != null)
            {
                JsonObject reply = new JsonObject();
                reply.putString("_id", genID);
                sendOK(message, reply);
            } else
            {
                sendOK(message);
            }
        } else
        {
            sendError(message, res.getError());
        }
    }

    private void doUpdate(Message message)
    {
        String collection = getMandatoryString("collection", message);
        if(collection == null)
            return;
        JsonObject criteriaJson = getMandatoryObject("criteria", message);
        if(criteriaJson == null)
            return;
        DBObject criteria = jsonToDBObject(criteriaJson);
        JsonObject objNewJson = getMandatoryObject("objNew", message);
        if(objNewJson == null)
            return;
        DBObject objNew = jsonToDBObject(objNewJson);
        Boolean upsert = Boolean.valueOf(((JsonObject)message.body()).getBoolean("upsert", false));
        Boolean multi = Boolean.valueOf(((JsonObject)message.body()).getBoolean("multi", false));
        DBCollection coll = db.getCollection(collection);
        WriteConcern writeConcern = WriteConcern.valueOf(getOptionalStringConfig("writeConcern", ""));
        if(writeConcern == null)
            writeConcern = WriteConcern.valueOf(getOptionalStringConfig("write_concern", ""));
        if(writeConcern == null)
            writeConcern = db.getWriteConcern();
        WriteResult res = coll.update(criteria, objNew, upsert.booleanValue(), multi.booleanValue(), writeConcern);
        if(res.getError() == null)
        {
            JsonObject reply = new JsonObject();
            reply.putNumber("number", Integer.valueOf(res.getN()));
            sendOK(message, reply);
        } else
        {
            sendError(message, res.getError());
        }
    }

    private void doFind(Message message)
    {
        String collection = getMandatoryString("collection", message);
        if(collection == null)
            return;
        Integer limit = (Integer)((JsonObject)message.body()).getNumber("limit");
        if(limit == null)
            limit = Integer.valueOf(-1);
        Integer skip = (Integer)((JsonObject)message.body()).getNumber("skip");
        if(skip == null)
            skip = Integer.valueOf(-1);
        Integer batchSize = (Integer)((JsonObject)message.body()).getNumber("batch_size");
        if(batchSize == null)
            batchSize = Integer.valueOf(100);
        Integer timeout = (Integer)((JsonObject)message.body()).getNumber("timeout");
        if(timeout == null || timeout.intValue() < 0)
            timeout = Integer.valueOf(10000);
        JsonObject matcher = ((JsonObject)message.body()).getObject("matcher");
        JsonObject keys = ((JsonObject)message.body()).getObject("keys");
        Object hint = ((JsonObject)message.body()).getField("hint");
        Object sort = ((JsonObject)message.body()).getField("sort");
        DBCollection coll = db.getCollection(collection);
        DBCursor cursor;
        if(matcher != null)
            cursor = keys != null ? coll.find(jsonToDBObject(matcher), jsonToDBObject(keys)) : coll.find(jsonToDBObject(matcher));
        else
            cursor = coll.find();
        if(skip.intValue() != -1)
            cursor.skip(skip.intValue());
        if(limit.intValue() != -1)
            cursor.limit(limit.intValue());
        if(sort != null)
            cursor.sort(sortObjectToDBObject(sort));
        if(hint != null)
            if(hint instanceof JsonObject)
                cursor.hint(jsonToDBObject((JsonObject)hint));
            else
            if(hint instanceof String)
                cursor.hint((String)hint);
            else
                throw new IllegalArgumentException((new StringBuilder("Cannot handle type ")).append(hint.getClass().getSimpleName()).toString());
        sendBatch(message, cursor, batchSize.intValue(), timeout.intValue());
    }

    private DBObject sortObjectToDBObject(Object sortObj)
    {
        if(sortObj instanceof JsonObject)
            return jsonToDBObject((JsonObject)sortObj);
        if(sortObj instanceof JsonArray)
        {
            JsonArray sortJsonObjects = (JsonArray)sortObj;
            DBObject sortDBObject = new BasicDBObject();
            Object curSortObj;
            for(Iterator iterator = sortJsonObjects.iterator(); iterator.hasNext(); sortDBObject.putAll(((JsonObject)curSortObj).toMap()))
            {
                curSortObj = iterator.next();
                if(!(curSortObj instanceof JsonObject))
                    throw new IllegalArgumentException((new StringBuilder("Cannot handle type ")).append(curSortObj.getClass().getSimpleName()).toString());
            }

            return sortDBObject;
        } else
        {
            throw new IllegalArgumentException((new StringBuilder("Cannot handle type ")).append(sortObj.getClass().getSimpleName()).toString());
        }
    }

    private void sendBatch(Message message, final DBCursor cursor, final int max, final int timeout)
    {
        int count = 0;
        JsonArray results = new JsonArray();
        for(; cursor.hasNext() && count < max; count++)
        {
            DBObject obj = cursor.next();
            JsonObject m = dbObjectToJsonObject(obj);
            results.add(m);
        }

        if(cursor.hasNext())
        {
            JsonObject reply = createBatchMessage("more-exist", results);
            final long timerID = vertx.setTimer(timeout, new Handler() );
            message.reply(reply, new Handler() );
        } else
        {
            JsonObject reply = createBatchMessage("ok", results);
            message.reply(reply);
            cursor.close();
        }
    }

    private JsonObject createBatchMessage(String status, JsonArray results)
    {
        JsonObject reply = new JsonObject();
        reply.putArray("results", results);
        reply.putString("status", status);
        reply.putNumber("number", Integer.valueOf(results.size()));
        return reply;
    }

    private void doFindOne(Message message)
    {
        String collection = getMandatoryString("collection", message);
        if(collection == null)
            return;
        JsonObject matcher = ((JsonObject)message.body()).getObject("matcher");
        JsonObject keys = ((JsonObject)message.body()).getObject("keys");
        DBCollection coll = db.getCollection(collection);
        DBObject res;
        if(matcher == null)
            res = keys == null ? coll.findOne() : coll.findOne(null, jsonToDBObject(keys));
        else
            res = keys == null ? coll.findOne(jsonToDBObject(matcher)) : coll.findOne(jsonToDBObject(matcher), jsonToDBObject(keys));
        JsonObject reply = new JsonObject();
        if(res != null)
        {
            JsonObject m = new JsonObject(res.toMap());
            reply.putObject("result", m);
        }
        sendOK(message, reply);
    }

    private void doFindAndModify(Message message)
    {
        String collectionName = getMandatoryString("collection", message);
        if(collectionName == null)
            return;
        JsonObject msgBody = (JsonObject)message.body();
        DBObject update = jsonToDBObjectNullSafe(msgBody.getObject("update"));
        DBObject query = jsonToDBObjectNullSafe(msgBody.getObject("matcher"));
        DBObject sort = jsonToDBObjectNullSafe(msgBody.getObject("sort"));
        DBObject fields = jsonToDBObjectNullSafe(msgBody.getObject("fields"));
        boolean remove = msgBody.getBoolean("remove", false);
        boolean returnNew = msgBody.getBoolean("new", false);
        boolean upsert = msgBody.getBoolean("upsert", false);
        DBCollection collection = db.getCollection(collectionName);
        DBObject result = collection.findAndModify(query, fields, sort, remove, update, returnNew, upsert);
        JsonObject reply = new JsonObject();
        if(result != null)
        {
            JsonObject resultJson = dbObjectToJsonObject(result);
            reply.putObject("result", resultJson);
        }
        sendOK(message, reply);
    }

    private void doCount(Message message)
    {
        String collection = getMandatoryString("collection", message);
        if(collection == null)
            return;
        JsonObject matcher = ((JsonObject)message.body()).getObject("matcher");
        DBCollection coll = db.getCollection(collection);
        long count;
        if(matcher == null)
            count = coll.count();
        else
            count = coll.count(jsonToDBObject(matcher));
        JsonObject reply = new JsonObject();
        reply.putNumber("count", Long.valueOf(count));
        sendOK(message, reply);
    }

    private void doDelete(Message message)
    {
        String collection = getMandatoryString("collection", message);
        if(collection == null)
            return;
        JsonObject matcher = getMandatoryObject("matcher", message);
        if(matcher == null)
            return;
        DBCollection coll = db.getCollection(collection);
        DBObject obj = jsonToDBObject(matcher);
        WriteConcern writeConcern = WriteConcern.valueOf(getOptionalStringConfig("writeConcern", ""));
        if(writeConcern == null)
            writeConcern = WriteConcern.valueOf(getOptionalStringConfig("write_concern", ""));
        if(writeConcern == null)
            writeConcern = db.getWriteConcern();
        WriteResult res = coll.remove(obj, writeConcern);
        int deleted = res.getN();
        JsonObject reply = (new JsonObject()).putNumber("number", Integer.valueOf(deleted));
        sendOK(message, reply);
    }

    private void getCollections(Message message)
    {
        JsonObject reply = new JsonObject();
        reply.putArray("collections", new JsonArray(db.getCollectionNames().toArray()));
        sendOK(message, reply);
    }

    private void dropCollection(Message message)
    {
        JsonObject reply = new JsonObject();
        String collection = getMandatoryString("collection", message);
        if(collection == null)
            return;
        DBCollection coll = db.getCollection(collection);
        try
        {
            coll.drop();
            sendOK(message, reply);
        }
        catch(MongoException mongoException)
        {
            sendError(message, (new StringBuilder("exception thrown when attempting to drop collection: ")).append(collection).append(" \n").append(mongoException.getMessage()).toString());
        }
    }

    private void getCollectionStats(Message message)
    {
        String collection = getMandatoryString("collection", message);
        if(collection == null)
        {
            return;
        } else
        {
            DBCollection coll = db.getCollection(collection);
            CommandResult stats = coll.getStats();
            JsonObject reply = new JsonObject();
            reply.putObject("stats", dbObjectToJsonObject(stats));
            sendOK(message, reply);
            return;
        }
    }

    private void doAggregation(Message message)
    {
        if(isCollectionMissing(message))
        {
            sendError(message, "collection is missing");
            return;
        }
        if(isPipelinesMissing(((JsonObject)message.body()).getArray("pipelines")))
        {
            sendError(message, "no pipeline operations found");
            return;
        }
        String collection = getMandatoryString("collection", message);
        JsonArray pipelinesAsJson = ((JsonObject)message.body()).getArray("pipelines");
        List pipelines = jsonPipelinesToDbObjects(pipelinesAsJson);
        DBCollection dbCollection = db.getCollection(collection);
        DBObject firstPipelineOp = (DBObject)pipelines.remove(0);
        AggregationOutput aggregationOutput = dbCollection.aggregate(firstPipelineOp, (DBObject[])pipelines.toArray(new DBObject[0]));
        JsonArray results = new JsonArray();
        DBObject dbObject;
        for(Iterator iterator = aggregationOutput.results().iterator(); iterator.hasNext(); results.add(dbObjectToJsonObject(dbObject)))
            dbObject = (DBObject)iterator.next();

        JsonObject reply = new JsonObject();
        reply.putArray("results", results);
        sendOK(message, reply);
    }

    private List jsonPipelinesToDbObjects(JsonArray pipelinesAsJson)
    {
        List pipelines = new ArrayList();
        DBObject dbObject;
        for(Iterator iterator = pipelinesAsJson.iterator(); iterator.hasNext(); pipelines.add(dbObject))
        {
            Object pipeline = iterator.next();
            dbObject = jsonToDBObject((JsonObject)pipeline);
        }

        return pipelines;
    }

    private boolean isCollectionMissing(Message message)
    {
        return getMandatoryString("collection", message) == null;
    }

    private boolean isPipelinesMissing(JsonArray pipelines)
    {
        return pipelines == null || pipelines.size() == 0;
    }

    private void runCommand(Message message)
    {
        JsonObject reply = new JsonObject();
        String command = getMandatoryString("command", message);
        if(command == null)
        {
            return;
        } else
        {
            DBObject commandObject = MongoUtil.convertJsonToBson(command);
            CommandResult result = db.command(commandObject);
            reply.putObject("result", new JsonObject(result.toMap()));
            sendOK(message, reply);
            return;
        }
    }

    private JsonObject dbObjectToJsonObject(DBObject obj)
    {
        if(useMongoTypes)
            return MongoUtil.convertBsonToJson(obj);
        else
            return new JsonObject(obj.toMap());
    }

    private DBObject jsonToDBObject(JsonObject object)
    {
        if(useMongoTypes)
            return MongoUtil.convertJsonToBson(object);
        else
            return new BasicDBObject(object.toMap());
    }

    private DBObject jsonToDBObjectNullSafe(JsonObject object)
    {
        if(object != null)
            return jsonToDBObject(object);
        else
            return null;
    }

    public volatile void handle(Object obj)
    {
        handle((Message)obj);
    }

    protected String address;
    protected String host;
    protected int port;
    protected String dbName;
    protected String username;
    protected String password;
    protected ReadPreference readPreference;
    protected boolean autoConnectRetry;
    protected int socketTimeout;
    protected boolean useSSL;
    protected Mongo mongo;
    protected DB db;
    private boolean useMongoTypes;



}
