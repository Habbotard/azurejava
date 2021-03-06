package org.azure.communication.messages.incoming.unsorted;

import org.azure.communication.messages.EClientMessage;
import org.azure.communication.messages.MessageEvent;
import org.azure.communication.messages.outgoing.unsorted.CitizenshipStatusMessageComposer;
import org.azure.communication.protocol.ClientMessage;
import org.azure.network.sessions.Session;

/**
 * AzureJava,
 * Edited: 11/26/2015 (Scott Stamp).
 */
@SuppressWarnings("unused")
public class RetrieveCitizenshipStatus {
    @MessageEvent(messageId = EClientMessage.RetrieveCitizenshipStatus)
    public static void eventHandler(Session session, ClientMessage message) {
        // CitizenshipStatusMessageComposer - 265
        session.sendMessage(CitizenshipStatusMessageComposer.compose());
    }
}