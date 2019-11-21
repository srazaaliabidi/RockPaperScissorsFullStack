package DTO;

import java.util.ArrayList;
import java.util.HashMap;


public class PlayerDTO {
    private ArrayList<HashMap<String, String>> response;

    public PlayerDTO(ArrayList<HashMap<String, String>> response) {
        this.response = response;
    }

    public ArrayList<HashMap<String, String>> getResponse() {
        return response;
    }


}

