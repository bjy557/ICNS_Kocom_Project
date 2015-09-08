package io.github.giovibal.mqtt;

import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.VoidHandler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.net.NetSocket;
import org.vertx.java.core.shareddata.ConcurrentSharedMap;
import org.vertx.java.platform.Container;

/**
 * Created by giovanni on 07/05/2014.
 */
public class MQTTNetSocket extends MQTTSocket {

    private NetSocket netSocket;

    public MQTTNetSocket(Vertx vertx, final Container container, NetSocket netSocket) {
        super(vertx, container);
        this.netSocket = netSocket;

    }

    public void start() {
        netSocket.dataHandler(this);
        netSocket.closeHandler(new Handler<Void>() {
            @Override
            public void handle(Void aVoid) {
                container.logger().info("net-socket closed ... "+ netSocket.writeHandlerID());
                shutdown();
            }
        });
    }

    @Override
    protected void sendMessageToClient(Buffer bytes) {
        try {
            if (!netSocket.writeQueueFull()) {
                netSocket.write(bytes);
            } else {
                netSocket.pause();
                netSocket.drainHandler(new VoidHandler() {
                    public void handle() {
                        netSocket.resume();
                    }
                });
            }
        } catch(Throwable e) {
            container.logger().error(e.getMessage());
        }
    }
}
