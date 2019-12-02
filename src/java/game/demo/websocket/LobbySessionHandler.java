/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.demo.websocket;

import game.demo.model.Lobby;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;

/**
 *
 * @author kschoudh
 */
@ApplicationScoped
public class LobbySessionHandler {
    private int LobbyId = 0;
    private final Set<Session> sessions = new HashSet<Session>();
    private final Set<Lobby> lobbies = new HashSet<Lobby>();
    
    public void addSession(Session session) {
        sessions.add(session);
        for (Lobby lobby : lobbies) {
            JsonObject addMessage = createAddMessage(lobby);
            sendToSession(session, addMessage);
        }
    }
    
    public void addLobby(Lobby lobby) {
        lobby.setId(LobbyId);
        lobbies.add(lobby);
        LobbyId++;
        JsonObject addMessage = createAddMessage(lobby);
        sendToAllConnectedSessions(addMessage);
    }
    
    public void removeLobby(int id) {
        Lobby lobby = getLobbyById(id);
        if (lobby != null) {
            lobbies.remove(lobby);
            JsonProvider provider = JsonProvider.provider();
            JsonObject removeMessage = provider.createObjectBuilder()
                    .add("action", "remove")
                    .add("id", id)
                    .build();
            sendToAllConnectedSessions(removeMessage);
        }
    }
    
    private Lobby getLobbyById(int id) {
        for (Lobby lobby : lobbies) {
            if (lobby.getId() == id) {
                return lobby;
            }
        }
        return null;
    }
    
    public void refresh() {
    Random random = new Random();
        for(Lobby l:lobbies){
            int x=Math.abs(random.nextInt()%100);
            l.setJoinCount(x);
            JsonProvider provider = JsonProvider.provider();
        JsonObject upMessage = provider.createObjectBuilder()
                .add("action", "refresh")
                .add("id", l.getId())
                .add("name", l.getName())
                .add("entryFee", l.getEntryFee())
                .add("joinCount", l.getJoinCount())
                .add("size", l.getSize())
                .build();
        sendToAllConnectedSessions(upMessage);
        }
        
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }
    
    private JsonObject createAddMessage(Lobby lobby) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject addMessage = provider.createObjectBuilder()
                .add("action", "add")
                .add("id", lobby.getId())
                .add("name", lobby.getName())
                .add("entryFee", lobby.getEntryFee())
                .add("joinCount", lobby.getJoinCount())
                .add("size", lobby.getSize())
                .build();
        return addMessage;
    }

    private void sendToAllConnectedSessions(JsonObject message) {
        for (Session session : sessions) {
            sendToSession(session, message);
        }
    }

    private void sendToSession(Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException ex) {
            sessions.remove(session);
            Logger.getLogger(LobbySessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
