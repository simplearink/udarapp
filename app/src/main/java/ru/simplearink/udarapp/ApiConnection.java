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

class ApiConnection extends AsyncTask<Void, Void, String[]> {
    int responseCode;
    String word;
    String correctness;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String[] doInBackground(Void... params) {
        try {
            connectApi();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] resArray = {word, correctness};
        return resArray;
    }

    @Override
    protected void onPostExecute(String[] result) {
        super.onPostExecute(result);
    }

    protected void connectApi() throws IOException {
        URL url = new URL("http://142.93.136.40/api/v1.1/words/get_word/?type=1");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        responseCode = conn.getResponseCode();
        if(responseCode != 200)
            throw new RuntimeException("HttpResponseCode:" + responseCode);
        else {
            parseJSON(url);
        }
        conn.disconnect();
    }

    protected void parseJSON(URL url) throws IOException {
        String inline = "";
        Scanner sc = new Scanner(url.openStream());
        while(sc.hasNext())
        {
            inline+=sc.nextLine();
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
        }
    }

}