package io.github.giovibal.mqtt.parser;

import io.netty.buffer.ByteBuf;
import org.dna.mqtt.moquette.proto.messages.AbstractMessage;
import org.dna.mqtt.moquette.proto.messages.PublishMessage;
import org.vertx.java.core.buffer.Buffer;

/**
 *
 * @author andrea
 */
class PublishEncoder extends DemuxEncoder<PublishMessage> {

    @Override
    protected void encode(PublishMessage message, ByteBuf out) {
        if (message.getQos() == AbstractMessage.QOSType.RESERVED) {
            throw new IllegalArgumentException("Found a message with RESERVED Qos");
        }
        if (message.getTopicName() == null || message.getTopicName().isEmpty()) {
            throw new IllegalArgumentException("Found a message with empty or null topic name");
        }
        
//        ByteBuf variableHeaderBuff = ctx.alloc().buffer(2);
        ByteBuf variableHeaderBuff = new Buffer(2).getByteBuf();
        try {
            variableHeaderBuff.writeBytes(Utils.encodeString(message.getTopicName()));
            if (message.getQos() == AbstractMessage.QOSType.LEAST_ONE ||
                message.getQos() == AbstractMessage.QOSType.EXACTLY_ONCE ) {
                if (message.getMessageID() == null) {
                    throw new IllegalArgumentException("Found a message with QOS 1 or 2 and not MessageID setted");
                }
                variableHeaderBuff.writeShort(message.getMessageID());
            }
            variableHeaderBuff.writeBytes(message.getPayload());
            int variableHeaderSize = variableHeaderBuff.readableBytes();

            byte flags = Utils.encodeFlags(message);

//            ByteBuf buff = ctx.alloc().buffer(2 + variableHeaderSize);
            ByteBuf buff = new Buffer(2).getByteBuf();
            buff.writeByte(AbstractMessage.PUBLISH << 4 | flags);
            buff.writeBytes(Utils.encodeRemainingLength(variableHeaderSize));
            buff.writeBytes(variableHeaderBuff);
            out.writeBytes(buff);
        } finally {
            variableHeaderBuff.release();
        }
    }
    
}
