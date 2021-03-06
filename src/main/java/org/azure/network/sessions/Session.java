package org.azure.network.sessions;

import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.azure.communication.encryption.DiffieHellman;
import org.azure.communication.messages.outgoing.unsorted.SuperNotificationMessageComposer;
import org.azure.communication.protocol.ServerMessage;
import org.azure.network.codec.EncryptionDecoder;

public class Session {
    private static final Logger logger = LogManager.getLogger(Session.class);

    private final int id;
    private final Channel channel;
    private DiffieHellman diffieHellman;
    private String uniqueID;

    public Session(int id, Channel ch) {
        this.id = id;
        this.channel = ch;
        this.diffieHellman = new DiffieHellman();
    }

    public int getId() {
        return this.id;
    }

    public void killSession() {
        if (this.channel != null) {
            this.channel.close();
            logger.info("Killed <Session " + this.id + ">");
        } else {
            logger.error("Can't kill <Session " + this.id + ">, already disposed.");
        }
    }

    public Channel getChannel() {
        return this.channel;
    }

    public DiffieHellman getDiffieHellman() {
        return this.diffieHellman;
    }

    public void sendQueued(ServerMessage message) {
        channel.write(message);
    }

    public void sendMessage(ServerMessage message) {
        channel.writeAndFlush(message);
    }

    public void enableRC4(byte[] sharedKey) {
        this.channel.pipeline().addBefore("gameDecoder", "gameCrypto", new EncryptionDecoder(sharedKey));
    }

    public String getUniqueID() {
        return this.uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public void sendHotelAlert(String title, String message) {
        this.sendMessage(SuperNotificationMessageComposer.compose("staffcloud", title, message));
    }
}
