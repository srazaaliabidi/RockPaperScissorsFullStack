package DAO;

import DTO.PlayerDTO;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;


public class PlayerDAO {
    PlayerDTO returnPlayerDTO;

    // open connection
    MongoClient mongoClient = new MongoClient("localhost", 27017);
    // get ref to database
    MongoDatabase db = mongoClient.getDatabase("MyDatabase");
    // get ref to collection
    MongoCollection<Document> myColection = db.getCollection("MyCollection");

    public PlayerDTO get(String winnerName, String loserName) {
        System.out.println("REACHED HERE 2");
//        MongoCursor<Document> cursor = myColection.find().iterator();
        ArrayList<HashMap<String, String>> responses = new ArrayList<>();
        System.out.println("TRY1");
        Document winnerDoc = myColection.find(eq("name", winnerName)).first();
        Document loserDoc = myColection.find(eq("name", loserName)).first();

        System.out.println("TRY2");

        // FOR WINNER
        if (Objects.isNull(winnerDoc)) {
            System.out.println("Document not found");
            // If document is not found, a new player is playing and we must add it to database
            Document doc = new Document()
                    .append("name", winnerName)
                    .append("score", 1);
            myColection.insertOne(doc);
            // Modify responses ArrayList for DTO:
            HashMap<String, String> tempMap = new HashMap<>();
            tempMap.put("name", winnerName);
            tempMap.put("winner", "true");
            tempMap.put("score", "1");
            responses.add(tempMap);

        } else {
            System.out.println("Document Found!");
            // If document is found, it is a returning player and must update their document score
            System.out.println(winnerDoc);
            System.out.println(winnerDoc.get("score"));
            int winnerScore = (int) winnerDoc.get("score") + 1;
            System.out.println(winnerScore);
            myColection.updateOne(eq("name", winnerName), new Document("$set", new Document("score", winnerScore)));
            // Modify responses ArrayList for DTO:
            HashMap<String, String> tempMap = new HashMap<>();
            tempMap.put("name", winnerName);
            tempMap.put("winner", "true");
            tempMap.put("score", String.valueOf(winnerScore));
            responses.add(tempMap);
        }

        // FOR LOSER
        if (Objects.isNull(loserDoc)) {
            System.out.println("Document not found");
            // If document is not found, a new player is playing and we must add it to database
            Document doc = new Document()
                    .append("name", loserName)
                    .append("score", 0);
            myColection.insertOne(doc);
            // Modify responses ArrayList for DTO:
            HashMap<String, String> tempMap = new HashMap<>();
            tempMap.put("name", loserName);
            tempMap.put("winner", "false");
            tempMap.put("score", "0");
            responses.add(tempMap);
        } else {
            System.out.println("Document Found!");
            // If document is found, it is a returning player and must update their document score
            System.out.println(loserDoc);
            System.out.println(loserDoc.get("score"));
            int loserScore = (int) loserDoc.get("score");
            if (loserScore > 0) {
                loserScore--;
            }
            System.out.println(loserScore);
            myColection.updateOne(eq("name", loserName), new Document("$set", new Document("score", loserScore)));
            // Modify responses ArrayList for DTO:
            HashMap<String, String> tempMap = new HashMap<>();
            tempMap.put("name", loserName);
            tempMap.put("winner", "false");
            tempMap.put("score", String.valueOf(loserScore));
            responses.add(tempMap);
        }

        System.out.println("REACHED END OF GET");
        returnPlayerDTO = new PlayerDTO(responses);
        return returnPlayerDTO;
    }


    public PlayerDTO getAllList() {
        MongoCursor<Document> cursor = myColection.find().iterator();
        ArrayList<HashMap<String, String>> responses = new ArrayList<>();
        HashMap<String, String> tempMap;
        try {
            while (cursor.hasNext()) {
                String object = cursor.next().toJson();
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                JsonElement name_element = jsonObject.get("name");
                JsonElement score_element = jsonObject.get("score");
                tempMap = new HashMap<>();
                String name_str = String.valueOf(name_element);
                name_str = name_str.substring(1, name_str.length()-1);
                String score_str = String.valueOf(score_element);
                tempMap.put("name", name_str);
                tempMap.put("score", score_str);
                responses.add(tempMap);
            }
        } finally {
            cursor.close();
        }
        returnPlayerDTO = new PlayerDTO(responses);
        return returnPlayerDTO;
    }


}