package ru.simplearink.udarapp;

import android.os.AsyncTask;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

class ApiConnection extends AsyncTask<Void, Void, String[][]> {
    int responseCode;

    String wordID;
    String word;
    String correctness;

    String[] words;
    String[] wordsIDs;
    String[] wordsCorrectness;

    int egeModeOn;
    int gameMode;
    int num;


    public ApiConnection(boolean egeMode, int mode, int numb) { // mode: 0 - checker mode; 1 - selection mode
        if (egeMode) {
            egeModeOn = 1;
        } else {
            egeModeOn = 0;
        }

        gameMode = mode;
        num = numb;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String[][] doInBackground(Void... params) {
        try {
            connectApi();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[][] resArray;

        if (gameMode == 0) {
            resArray = new String[3][1];
            resArray[0][0] = word;
            resArray[1][0] = correctness;
            resArray[2][0] = wordID;
        } else {
            resArray = new String[3][num];
            resArray[0] = words;
            resArray[1] = wordsCorrectness;
            resArray[2] = wordsIDs;
        }

        return resArray;
    }

    @Override
    protected void onPostExecute(String[][] result) {
        super.onPostExecute(result);
    }

    //method now works only for Checker Mode
    protected void connectApi() throws IOException {
        URL url;
        if (gameMode == 0) {
            url = new URL("http://api.accent-checker.ru/api/v1.1/words/get_word/?type=" + egeModeOn);
        } else if (num != 0) {
            url = new URL("http://api.accent-checker.ru/v2.0/words/get_question/?num_q=1&type=" + egeModeOn + "&num_w=" + num);
        } else {
            url = null;
        }
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        responseCode = conn.getResponseCode();
        if (responseCode != 200)
            throw new RuntimeException("HttpResponseCode:" + responseCode);
        else {
            if (gameMode == 0) {
                parseJSON(url);
            } else {
                parseJSONMultiple(url, num);
            }
        }
        conn.disconnect();
    }

    protected void parseJSON(URL url) throws IOException {
        String inline = "";
        Scanner sc = new Scanner(url.openStream());
        while (sc.hasNext()) {
            inline += sc.nextLine();
        }
        System.out.println("\nJSON Response in String format");
        System.out.println(inline);
        sc.close();

        JsonParser parser = new JsonParser();

        JsonArray jsonArray = (JsonArray) parser.parse(inline);

        JsonElement jsonObj = jsonArray.get(0);

        if (jsonObj.isJsonObject()) {
            JsonObject jsonObject = jsonObj.getAsJsonObject();
            System.out.println(jsonObject.get("word"));
            word = jsonObject.get("word").toString().replace("\"", "");
            correctness = jsonObject.get("answer").toString();
            wordID = jsonObject.get("word_id").toString();
        }
    }

    protected void parseJSONMultiple(URL url, int num) throws IOException {
        words = new String[num];
        wordsIDs = new String[num];
        wordsCorrectness = new String[num];

        String inline = "";
        Scanner sc = new Scanner(url.openStream());
        while (sc.hasNext()) {
            inline += sc.nextLine();
        }
        System.out.println("\nJSON Response in String format");
        System.out.println(inline);
        sc.close();

        JsonParser parser = new JsonParser();

        JsonArray jsonArray = (JsonArray) parser.parse(inline);

        JsonElement jsonObj = jsonArray.get(0);
        System.out.println(jsonObj);

        if (jsonObj.isJsonObject()) {
            JsonObject jsonObject = jsonObj.getAsJsonObject();
            JsonArray question = (JsonArray) jsonObject.get("question");
            for (int i = 0; i < num; i++) {
                JsonElement jsonElem = question.get(i);
                if (jsonElem.isJsonObject()) {
                    JsonObject jsonOb = jsonElem.getAsJsonObject();
                    wordsIDs[i] = jsonOb.get("word_id").toString();
                    words[i] = jsonOb.get("word").toString();
                }
            }
            JsonElement ans = jsonObject.get("answer");
            System.out.println(ans);
            JsonObject answer = ans.getAsJsonObject();
            wordsCorrectness[0] = String.valueOf(answer.get("word_id"));
            System.out.println(answer.get("word_id"));
        }

    }
}