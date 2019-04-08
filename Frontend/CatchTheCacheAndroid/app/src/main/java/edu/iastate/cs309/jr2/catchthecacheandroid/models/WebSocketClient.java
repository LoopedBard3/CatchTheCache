package edu.iastate.cs309.jr2.catchthecacheandroid.models;

import android.util.Log;

import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public abstract class WebSocketClient extends org.java_websocket.client.WebSocketClient {

    public WebSocketClient(URI serverUri) {
        super(serverUri);
        Log.d("WEBSOCKET", "New WebSocket Created");
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        Log.d("WEBSOCKET", "run() returned: " + "is connecting");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.d("WEBSOCKET", "onClose() returned: " + reason);
    }

    @Override
    public void onError(Exception e)
    {
        Log.d("WEBSOCKET", e.toString());
    }

    public Boolean broadcast(String message){
        Log.d("WEBSOCKET", "Attempting Broadcast");
        if(this.isOpen()) {
            this.send(message);
            return true;
        }
        return false;
    }
}
