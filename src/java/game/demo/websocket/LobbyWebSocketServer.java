/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.demo.websocket;

import game.demo.model.Lobby;
import java.io.IOException;
import java.io.StringReader;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author kschoudh
 */
@ApplicationScoped
@ServerEndpoint("/data")
public class LobbyWebSocketServer {
    @Inject
    private LobbySessionHandler sessionHandler;
    
    @OnOpen
        public void open(Session session) {
           sessionHandler.addSession(session);
           TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                sessionHandler.refresh();
            }
        };
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 5 * 1000);
    }

    @OnClose
        public void close(Session session) {
            sessionHandler.removeSession(session);
    }

    @OnError
        public void onError(Throwable error) {
            Logger.getLogger(LobbyWebSocketServer.class.getName()).log(Level.SEVERE, null, error);
    }

    @OnMessage
        public void handleMessage(String message, Session session) {
            try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();

            if ("add".equals(jsonMessage.getString("action"))) {
                Lobby lobby = new Lobby();
                lobby.setName(jsonMessage.getString("name"));
                String entryFee = jsonMessage.getString("entryFee");
                int joinC = (int) jsonMessage.getInt("joinCount");
                String size = jsonMessage.getString("size");
                int entryF = Integer.parseInt(entryFee);
                int sz = Integer.parseInt(size);
                lobby.setEntryFee(entryF);
                lobby.setJoinCount(joinC);
                lobby.setSize(sz);
                sessionHandler.addLobby(lobby);
            }

            if ("remove".equals(jsonMessage.getString("action"))) {
                int id = (int) jsonMessage.getInt("id");
                sessionHandler.removeLobby(id);
            }
        }
 
    }
    
}
