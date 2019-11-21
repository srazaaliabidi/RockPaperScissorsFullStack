package Spark;

import static spark.Spark.*;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.ArrayList;
import java.util.List;

public class SparkDemo {
  public String winnerName;
  public String loserName;

  public static void main(String[] args) {
    port(1234);
    // calling get will make your app start listening for the GET path with the /hello endpoint
    //get("/hello", (req, res) -> "Hello World");

    // IDEALLY:
    // I think the URL data will be received something like:
    // "http://localhost:1234/playgame?player1Name=Joe&player1choice=rock&player2Name=Raza&player2choice=scissors"
    Spark.get("/playgame", SparkDemo::managePlayers);

  }

  public static String managePlayers(Request req, Response res) {
    // Steps for managing game after winner is decided:
    // 1) grab the player1 name and look thru database for matching name
    // 2) if match, update the score property and set winner=True/False
    // 3) if no match, add new doc to database w/ score=0/1 and set  winner=True/False
    // 4) generate JSON from DTO and return it
    String player1Name = req.queryMap().get("player1Name").value().toString(); // get player 1 name

    // Winners and Losers decided here


    return "";
  }

}