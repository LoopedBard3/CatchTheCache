package edu.iastate.cs309.jr2.catchthecacheandroid.models;

import android.util.Log;

import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Abstract class that takes care of the open, close, and error methods of the java websocket client class.
 * Also implements a broadcast method for sending out a message.
 * @author Parker Bibus
 */
public abstract class WebSocketClient extends org.java_websocket.client.WebSocketClient {

    /**
     * Default constructor that takes in the URI to create the socket.
     * @param serverUri URI to connect to.
     */
    public WebSocketClient(URI serverUri) {
        super(serverUri);
        Log.d("WEBSOCKET", "New WebSocket Created, connected to " + serverUri.toString());
    }

    /**
     * Overridden onOpen method that logs the connection.
     * @param handshake handshake that is passed in
     */
    @Override
    public void onOpen(ServerHandshake handshake) {
        Log.d("WEBSOCKET", "run() returned: " + "is connecting");
    }

    /**
     * Overridden onClose method that logs the close with the reason.
     * @param code passed code.
     * @param reason reason for the close.
     * @param remote
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.d("WEBSOCKET", "onClose() returned: " + reason);
    }

    /**
     * Overridden onError method that logs any errors for the websocket.
     * @param e Exception that occurred.
     */
    @Override
    public void onError(Exception e)
    {
        Log.d("WEBSOCKET", e.toString());
    }

    /**
     * Method that send the specified message out through the websocket.
     * @param message the message to send.
     * @return true if message is sent and false if it was not sent, most likely due to the socket being closed.
     */
    public Boolean broadcast(String message){
        Log.d("WEBSOCKET", "Attempting Broadcast");
        if(this.isOpen()) {
            this.send(message);
            return true;
        }
        return false;
    }
}
