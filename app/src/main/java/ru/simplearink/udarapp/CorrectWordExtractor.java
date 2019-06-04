package ru.simplearink.udarapp;

import android.os.AsyncTask;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CorrectWordExtractor extends AsyncTask<Void, Void, String> {
    int responseCode;

    int id;
    String word;

    CorrectWordExtractor(int newID) {
        id = newID;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            getCorrectWord(id);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String res = word;
        return res;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

    public void getCorrectWord(int id) throws IOException {
        URL url = new URL("http://www.api.accent-checker.ru/v2.0/words/" + id);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        responseCode = conn.getResponseCode();
        if (responseCode != 200)
            throw new RuntimeException("HttpResponseCode:" + responseCode);
        else {
            String inline = "";
            Scanner sc = new Scanner(url.openStream());
            while (sc.hasNext()) {
                inline += sc.nextLine();
            }
            sc.close();

            JsonParser parser = new JsonParser();

            JsonElement jsonObj = parser.parse(inline);

            String newWord = "";

            if (jsonObj.isJsonObject()) {
                JsonObject jsonObject = jsonObj.getAsJsonObject();
//                System.out.println(jsonObject.get("word"));
                word = jsonObject.get("word").toString().replace("\"", "");
            }
            conn.disconnect();
        }
    }
}
