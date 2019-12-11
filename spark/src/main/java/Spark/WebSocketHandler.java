package Spark;

import DTO.TransferDTO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.lang.reflect.Array;
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
    public static Map<String, Session> userMap = new ConcurrentHashMap<>(); // MAPS EACH GAMERTAG TO GENERATED SESSION OBJECT
    public static ArrayList<String> queueOrder = new ArrayList<>(); // THE QUEUE DATA STRUCTURE
    public static ArrayList<String> tempList = new ArrayList<>(); // TO HOLD 2 PLAYERS FROM QUEUE AT START OF GAME/DURING
    public static ArrayList<TransferDTO> gameList = new ArrayList<>(); // TO STORE THE NAME AND CHOICES OF PLAYERS IN A GAME

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
        if (transferDTO_1.choice.length() == 0) {
            String playerName = transferDTO_1.name;
            processQueue(playerName, session);
        }
        else {
            String playerName = transferDTO_1.name;
            if (tempList.contains(playerName)) {
                gameList.add(transferDTO_1);
                if (gameList.size() == 2) {
                    // AT THIS POINT, THE PLAYER CHOICES HAVE BEEN MADE, EXTRACT THEM:
                    String player1Name = gameList.get(0).name;
                    String player1Choice = gameList.get(0).choice;
                    String player2Name = gameList.get(1).name;
                    String player2Choice = gameList.get(1).choice;
                    String[] result = playGame(player1Name, player1Choice, player2Name, player2Choice);
                    // IF THERE IS A TIE:
                    if (result[0].equals("tie")) {
                        userMap.get(player1Name).getRemote().sendString("TIE");
                        userMap.get(player2Name).getRemote().sendString("TIE");
                    }
                    // OTHERWISE
                    else {
                        String winnerName = result[0];
                        String loserName = result[1];
                        userMap.get(winnerName).getRemote().sendString("WINNER");
                        userMap.get(loserName).getRemote().sendString("LOSER");
                        userMap.remove(player1Name);
                        userMap.remove(player2Name);
                        gameList.clear();
                        tempList.clear();
                    }
                }
            }
        }
    }

    public void processQueue(String username, Session session) throws IOException {
        queueOrder.add(username);
        userMap.put(username, session);
        if (queueOrder.size() == 2) {
            // A PAIRING HAS BEEN FOUND
            // COPY OVER ITEMS IN QUEUEORDER TO TEMPLIST
            for (String str : queueOrder) {
                tempList.add(str);
            }
            // REMOVE FIRST AND SECOND PLAYERS FROM QUEUE SINCE THEY ARE JUST MATCHED
            queueOrder.remove(0);
            queueOrder.remove(1);
            // SEND MESSAGE TO START GAME
            for (String str : tempList) {
                userMap.get(str).getRemote().sendString("REMOVE_WAITSCREEN_PLAY_GAME");
            }
        }
        // IF ONLY ONE PERSON IN QUEUE TRIGGER WAITSCREEN
        else if (queueOrder.size() == 1) {
            String name = queueOrder.get(0);
            userMap.get(name).getRemote().sendString("WAITSCREEN");
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