package Spark;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import static spark.Spark.*;
import DAO.PlayerDAO;
import DTO.PlayerDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;
import spark.Spark;

@WebSocket
public class WebSocketHandler {
    // Store sessions if you want to, for example, broadcast a message to all users
    static Map<Session, Session> sessionMap = new ConcurrentHashMap<>();
    public static Map<String, Session> userMap = new ConcurrentHashMap<>(); // GAMERTAG IS KEY, GENERATED SESSION OBJECT IS VALUE

    public static void broadcast(String message) {
        sessionMap.keySet().stream()
                .filter(Session::isOpen)
                .forEach(session -> {
            try {
                session.getRemote().sendString(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }



    @OnWebSocketConnect
    public void connected(Session session) throws IOException {
        System.out.println("A client has connected");
//        System.out.println("USERNAP QUEUE SIZE: " + userMap.size());

        //sessionMap.put(session, session);
        //session.getRemote().sendString(clickCountString); // and send it back
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        System.out.println("A client has disconnected");
        System.out.println(statusCode);
        System.out.println(reason);
//        sessionMap.remove(session);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {

        // ENTER IS PRESSED:
        System.out.println("Got: " + message);   // Print message

        String gamerTag = message.split(" ")[0];
        userMap.put(gamerTag, session); // ADD TO USERMAP

        System.out.println("USERMAP QUEUE SIZE: " + userMap.size());
        // WAITING MESSAGE SHOULD BE DISPLAYED
        // IF ANOTHER SESSION IS PRESENT IN USERMAP, PAIR THEM UP:
        //username: "Barack Rock"
        if (userMap.size() == 2) {
            System.out.println("REACHED HERE");
            ArrayList<String> playerNames = new ArrayList<>(userMap.keySet());
            // WAIT FOR BOTH R/P/S INPUTS FROM THE 2 PLAYERS
            // ONCE 2 INPUTS ARE CHOSEN, DECIDE WHO WINS HERE

            // String winnerName, loserName;

            String winnerName = playerNames.get(0);
            String loserName = playerNames.get(1);
            String player1 = playerNames.get(0); // Joe Rock
            String player2 = playerNames.get(1); // Raza Paper

            // Game Logic:
            String playerName1 = player1.split(" ")[0];
            String playerChoice1 = player1.split(" ")[1];
            String playerName2 = player2.split(" ")[0];
            String playerChoice2 = player2.split(" ")[1];

            //Basic logic:
            switch (playerChoice1) {
                case "Rock":
                    switch (playerChoice2) {
                        case "Rock":
                            //not sure what to put down for ties as of now so I'll print a tie
                            System.out.println("Its a tie");
                            break;
                        case "Paper":
                            winnerName = playerName2;
                            loserName = playerName1;
                            break;
                        case "Scissors":
                            winnerName = playerName1;
                            loserName = playerName2;
                    }
                    break;

                case "Paper":
                    switch (playerChoice2) {
                        case "Rock":
                            winnerName = playerName1;
                            loserName = playerName2;
                            break;
                        case "Paper":
                            System.out.println("Its a tie");
                            break;
                        case "Scissors":
                            winnerName = playerName2;
                            loserName = playerName1;
                            break;
                    }
                    break;
                case "Scissors":
                    switch(playerChoice2) {
                        case "Rock":
                           winnerName = playerName2;
                           loserName = playerName1;
                            break;
                        case "Paper":
                            winnerName = playerName1;
                            loserName = playerName2;
                            break;
                        case "Scissors":
                            System.out.println("It is a tie");
                            break;
            }
        }


           //String winnerName = playerNames.get(0);
           // String loserName = playerNames.get(1);
            // UPDATE THE DATABASE
            PlayerDAO playerDAO = new PlayerDAO();
            PlayerDTO playerDTO = playerDAO.get(winnerName, loserName);
            // TO EACH RESPECTIVE SESSION, SEND BACK MESSAGE STATING IF THEY WON OR LOST
            System.out.println("REACHED HERE 2");
            userMap.get(loserName).getRemote().sendString("LOSER");
            userMap.get(winnerName).getRemote().sendString("WINNER");
            System.out.println("REACHED HERE 3");
            // CLEAR THE MAP OF SESSIONS OF PREVIOUS GAMES:
            //userMap.clear();
            // INSTEAD, TRY CLEARING JUST THE 2 PEOPLE THAT JUST FINISHED PLAYING?:
            userMap.remove(loserName);
            userMap.remove(winnerName);
            System.out.println("USERMAP QUEUE SIZE: " + userMap.size());

            System.out.println("USERMAP QUEUE SIZE: " + userMap.size());
        }
        else if (userMap.size() == 1)  {
            System.out.println("TRIGGER WAITSCREEN");
        }



//        clickCountString = message; // save the count
//        broadcast(message);
    }
}