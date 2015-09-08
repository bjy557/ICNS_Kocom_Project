// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MQTT.java

package org.vertx.mods.mqtt;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.eclipse.paho.client.mqttv3.*;
import org.vertx.java.busmods.BusModBase;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Container;

public class MQTT extends BusModBase
    implements Handler
{

    public MQTT()
    {
    }

    public void start()
    {
        start();
        port = getOptionalIntConfig("port", 1883);
        host = getOptionalStringConfig("host", "localhost");
        clientID = getOptionalStringConfig("clientID", MqttClient.generateClientId());
        dbConfig = getOptionalObjectConfig("dbConfig", null);
        Address = "mqttclient";
        key = getOptionalStringConfig("key", "g2PYYeRkm4XwNs5SkT%2BEm6ZWuLXQCBNLJ4jdEH43rTuU0WjKjo");
        Key = new JsonObject();
        if(key == null)
            Key.putString("Key", "g2PYYeRkm4XwNs5SkT%2BEm6ZWuLXQCBNLJ4jdEH43rTuU0WjKjo");
        else
            Key.putString("Key", key);
        try
        {
            mClient = new MqttClient((new StringBuilder("tcp://")).append(host).append(":").append(port).toString(), clientID);
            mClient.connect();
        }
        catch(MqttException e)
        {
            e.printStackTrace();
        }
        eb.registerHandler(Address, this);
    }

    public void handle(Message message)
    {
label0:
        {
            System.out.println(((JsonObject)message.body()).toString());
            String action = ((JsonObject)message.body()).getString("action");
            if(action == null)
            {
                sendError(message, "action must be specified");
                return;
            }
            String s;
            switch((s = action).hashCode())
            {
            default:
                break;

            case -690212903: 
                MqttException e;
                if(!s.equals("registor"))
                    break;
                doRegistor(message);
                break label0;

            case -235365105: 
                if(!s.equals("publish"))
                    break;
                doPublish(message);
                break label0;

            case 514841930: 
                if(s.equals("subscribe"))
                {
                    try
                    {
                        doSubscribe(message);
                    }
                    // Misplaced declaration of an exception variable
                    catch(MqttException e)
                    {
                        e.printStackTrace();
                    }
                    break label0;
                }
                break;
            }
            sendError(message, (new StringBuilder("Invalid action: ")).append(action).toString());
        }
    }

    private void doSubscribe(Message message)
        throws MqttException
    {
        String topic = getMandatoryString("topic", message);
        mClient.subscribe((new StringBuilder(String.valueOf(key))).append("/").append(topic).toString());
        mClient.setCallback(new MqttCallback() {

            public void connectionLost(Throwable throwable)
            {
            }

            public void deliveryComplete(IMqttDeliveryToken imqttdeliverytoken)
            {
            }

            public void messageArrived(String arg0, MqttMessage arg1)
                throws Exception
            {
                JsonObject doc = new JsonObject(arg1.toString());
                subscribeFunc(doc, dbConfig);
            }

            final MQTT this$0;

            
            {
                this$0 = MQTT.this;
                Object();
            }
        }
);
    }

    private void doPublish(Message message)
    {
        String topic = getMandatoryString("topic", message);
        if(topic == null)
            return;
        JsonObject doc = getMandatoryObject("document", message);
        if(doc == null)
            return;
        MqttMessage mMessage = new MqttMessage(doc.toString().getBytes());
        try
        {
            mClient.publish((new StringBuilder(String.valueOf(key))).append("/").append(topic).toString(), mMessage);
        }
        catch(MqttPersistenceException e)
        {
            e.printStackTrace();
        }
        catch(MqttException e)
        {
            e.printStackTrace();
        }
    }

    private void doRegistor(Message message)
    {
        String topic = getMandatoryString("tgID", message);
        if(topic == null)
            return;
        System.out.println(Key.toString());
        MqttMessage mMessage = new MqttMessage(Key.toString().getBytes());
        try
        {
            mClient.publish(topic, mMessage);
        }
        catch(MqttPersistenceException e)
        {
            e.printStackTrace();
        }
        catch(MqttException e)
        {
            e.printStackTrace();
        }
    }

    private void subscribeFunc(final JsonObject doc, final JsonObject dbConfig)
    {
        container.deployModule("icns.kocom~mongo-persistor~1.0", dbConfig, new Handler() {

            public void handle(AsyncResult arg0)
            {
label0:
                {
label1:
                    {
label2:
                        {
                            if(!arg0.succeeded())
                                break label1;
                            String s;
                            switch((s = doc.getString("type")).hashCode())
                            {
                            default:
                                break;

                            case 107332: 
                                if(!s.equals("log"))
                                    break;
                                dbname = "logdata";
                                break label2;

                            case 3452698: 
                                if(s.equals("push"))
                                {
                                    doPush(doc);
                                    return;
                                }
                                break;

                            case 1711986276: 
                                if(!s.equals("sensordata"))
                                    break;
                                dbname = "sensordata";
                                break label2;
                            }
                            System.out.println((new StringBuilder("Invalid action: ")).append(doc.getString("Type")).toString());
                            return;
                        }
                        doc.removeField("type");
                        if(dbConfig.containsField("db_name"))
                            dbConfig.removeField("db_name");
                        dbConfig.putString("db_name", dbname);
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
                        Date currentTime = new Date();
                        String dTime = formatter.format(currentTime);
                        doc.putString("time", dTime);
                        JsonObject sendData = new JsonObject();
                        if(doc.containsField("tg_id"))
                        {
                            sendData.putString("collection", doc.getString("tg_id"));
                            doc.removeField("tg_id");
                        } else
                        {
                            sendData.putString("collection", "data");
                        }
                        sendData.putString("action", "save");
                        sendData.putObject("document", doc);
                        sendData.putString("db_name", dbConfig.getString("db_name"));
                        System.out.println(sendData.toString());
                        .send("vertx.mongopersistor", sendData);
                        break label0;
                    }
                    System.err.println("Error!");
                }
            }

            public volatile void handle(Object obj)
            {
                handle((AsyncResult)obj);
            }

            final MQTT this$0;
            private final JsonObject val$doc;
            private final JsonObject val$dbConfig;

            
            {
                this$0 = MQTT.this;
                doc = jsonobject;
                dbConfig = jsonobject1;
                Object();
            }
        }
);
    }

    private void doPush(JsonObject jsonobject)
    {
    }

    public volatile void handle(Object obj)
    {
        handle((Message)obj);
    }

    protected String host;
    protected int port;
    protected String clientID;
    private static MqttClient mClient;
    protected String Address;
    protected JsonObject dbConfig;
    protected String deployID;
    protected String dbname;
    protected String key;
    protected JsonObject Key;



}
    