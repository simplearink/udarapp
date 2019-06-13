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

    private int egeModeOn;
    private int gameMode;
    private int num;

    private String[][] resArray;


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

        return resArray;
    }

    @Override
    protected void onPostExecute(String[][] result) {
        super.onPostExecute(result);
    }

    protected void connectApi() throws IOException {
        URL url;
        if (gameMode == 0) {
            url = new URL("http://www.api.accent-checker.ru/v2.0/words/get_word/?type=" + egeModeOn + "&num=" + num);
            resArray = new String[4][num];
        } else if (num != 0) {
            url = new URL("http://api.accent-checker.ru/v2.0/words/get_question/?num_q=1&type=" + egeModeOn + "&num_w=" + num);
            resArray = new String[3][num];
        } else {
            url = null;
        }
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int responseCode = conn.getResponseCode();
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
        System.out.println(inline);
        sc.close();

        JsonParser parser = new JsonParser();

        JsonArray jsonArray = (JsonArray) parser.parse(inline);

        for (int i = 0; i < num; i++) {
            JsonElement jsonObj = jsonArray.get(i);

            if (jsonObj.isJsonObject()) {
                JsonObject jsonObject = jsonObj.getAsJsonObject();
                resArray[0][i] = jsonObject.get("word").toString().replace("\"", ""); //word
                resArray[1][i] = jsonObject.get("answer").toString(); //correctness (answer)
                resArray[2][i] = jsonObject.get("word_id").toString(); //wordID
                resArray[3][i] = jsonObject.get("correct_word").toString().replace("\"", ""); //correctWord
            }
        }
    }

    protected void parseJSONMultiple(URL url, int num) throws IOException {
        String[] words = new String[num];
        String[] wordsIDs = new String[num];
        String[] wordsCorrectness = new String[num];

        String inline = "";
        Scanner sc = new Scanner(url.openStream());
        while (sc.hasNext()) {
            inline += sc.nextLine();
        }
        sc.close();

        JsonParser parser = new JsonParser();

        JsonArray jsonArray = (JsonArray) parser.parse(inline);

        JsonElement jsonObj = jsonArray.get(0);

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
            JsonObject answer = ans.getAsJsonObject();
            wordsCorrectness[0] = String.valueOf(answer.get("word_id"));
        }

        resArray[0] = words;
        resArray[1] = wordsCorrectness;
        resArray[2] = wordsIDs;

    }
}