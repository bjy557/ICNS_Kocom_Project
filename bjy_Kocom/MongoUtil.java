// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MongoUtil.java

package org.vertx.mods;

import com.mongodb.DBObject;
import com.mongodb.util.*;
import java.util.HashMap;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.json.impl.Json;

public class MongoUtil
{

    public MongoUtil()
    {
    }

    public static DBObject convertJsonToBson(JsonObject json)
    {
        if(json == null)
            throw new IllegalArgumentException("Cannot convert null object to DBObject");
        else
            return convertJsonToBson(json.encode());
    }

    public static DBObject convertJsonToBson(String json)
    {
        if(json == null || json.equals(""))
            throw new IllegalArgumentException("Cannot convert empty string to DBObject");
        else
            return (DBObject)JSON.parse(json);
    }

    public static JsonObject convertBsonToJson(DBObject dbObject)
    {
        if(dbObject == null)
        {
            throw new IllegalArgumentException("Cannot convert null to JsonObject");
        } else
        {
            String serialize = JSONSerializers.getStrict().serialize(dbObject);
            HashMap jsonMap = (HashMap)Json.decodeValue(serialize, java/util/HashMap);
            return new JsonObject(jsonMap);
        }
    }
}
