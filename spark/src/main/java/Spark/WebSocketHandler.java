package Spark;

import DTO.TransferDTO;
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

        // CREATE GSON INSTANCE AND USE IT TO INITIALIZE TRANSFERDTO OBJECT:
        Gson gson = new Gson();
        TransferDTO transferDTO_1 = gson.fromJson(message, TransferDTO.class);
        // ADD TO USERMAP WITH GAMERTAG AS KEY IF CHOICE FIELD IS EMPTY:
        if (transferDTO_1.choice.equals("")) {
            String playerName = transferDTO_1.name;
            userMap.put(playerName, session);
        }

        System.out.println("USERMAP QUEUE SIZE: " + userMap.size());
        // WAITING MESSAGE SHOULD BE DISPLAYED
        // IF ANOTHER SESSION IS PRESENT IN USERMAP, PAIR THEM UP:
        if (userMap.size() == 2) {
            // SEND MESSAGE TO REACT FRONTEND SIGNALING START OF GAME B/W 2 PLAYERS
            for (String key : userMap.keySet()) {
                userMap.get(key).getRemote().sendString("REMOVE_WAITSCREEN_PLAY_GAME");
            }
            System.out.println("REACHED HERE");
//            ArrayList<String> playerNames = new ArrayList<>(userMap.keySet());
            // ARRAYLIST OF GENERATED TRANSFERDTO OBJECTS DURING GAMEPLAY (SHOULD BE 1 FROM EACH OF THE 2 PLAYERS):
            ArrayList<TransferDTO> gameStatsList = new ArrayList<>();
            // THIS IS THE TRANSFERDTO OBJECT WITH THE R/P/S INPUTS FROM THE PLAYER:
            TransferDTO transferDTO_2 = gson.fromJson(message, TransferDTO.class);
            gameStatsList.add(transferDTO_2);
            String winnerName = "";
            String loserName = "";

            // GAME LOGIC IS CALCULATED ONCE GAME ENDS. THIS IS INDICATED WHEN GAMESTATSLIST HAS A SIZE OF 2:
            if (gameStatsList.size() == 2) {
                String player1Name = gameStatsList.get(0).name;
                String player1Choice = gameStatsList.get(0).choice;
                String player2Name = gameStatsList.get(1).name;
                String player2Choice = gameStatsList.get(1).choice;

                // DECIDE WINNER AND LOSER HERE. GAMELOGIC IS WRAPPED IN playGame METHOD AT BOTTOM
                String[] gameResults = playGame(player1Name, player1Choice, player2Name, player2Choice);

                // IF THERE IS A TIE:
                if (gameResults[0].equals("tie")) {
                    userMap.get(player1Name).getRemote().sendString("TIE");
                    userMap.get(player2Name).getRemote().sendString("TIE");
                }
                // OTHERWISE:
                else {
                    winnerName = gameResults[0];
                    loserName = gameResults[1];
                    // UPDATE THE DATABASE
                    PlayerDAO playerDAO = new PlayerDAO();
                    PlayerDTO playerDTO = playerDAO.get(winnerName, loserName);
                    // TO EACH RESPECTIVE SESSION, SEND BACK MESSAGE STATING IF THEY WON OR LOST
                    userMap.get(loserName).getRemote().sendString("LOSER");
                    userMap.get(winnerName).getRemote().sendString("WINNER");
                }

                // CLEAR THE GAMESTATSLIST FOR FUTURE PLAYERS
                gameStatsList.clear();
                // TRY CLEARING JUST THE 2 PEOPLE THAT JUST FINISHED PLAYING?:
                userMap.remove(loserName);
                userMap.remove(winnerName);
            }

            System.out.println("USERMAP QUEUE SIZE: " + userMap.size());
        }

        else if (userMap.size() == 1)  {
            System.out.println("TRIGGER WAITSCREEN");
            String playerName = transferDTO_1.name;
            userMap.get(playerName).getRemote().sendString("WAITSCREEN");
        }


    }

    // GAME LOGIC HERE
    public String[] playGame(String playerName1, String playerChoice1, String playerName2, String playerChoice2) {
        //Basic logic:
        String winnerName = "";
        String loserName = "";
        boolean tie = false;
        switch (playerChoice1) {
            case "Rock":
                switch (playerChoice2) {
                    case "Rock":
                        //not sure what to put down for ties as of now so I'll print a tie
                        System.out.println("Its a tie");
                        tie = true;
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
                        tie = true;
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
                        tie = true;
                        break;
                }
        }
        String[] list = new String[2];
        if (tie) {
            list[0] = "tie";
            list[1] = "tie";
            return list;
        }
        else {
            list[0] = winnerName;
            list[1] = loserName;
            return list;
        }
    }

}