package org.azure.communication.messages.incoming.unsorted;

import org.azure.communication.messages.MessageEvent;
import org.azure.communication.protocol.ClientMessage;
import org.azure.network.sessions.Session;

/**
 * AzureJava,
 * Edited: 11/26/2015 (Scott Stamp).
 */
@SuppressWarnings("unused")
public class NewNavigatorMessageEvent {
    @MessageEvent(messageId = 1121)
    public static void eventHandler(Session session, ClientMessage message) {
        // TODO: Implementation
    }
}