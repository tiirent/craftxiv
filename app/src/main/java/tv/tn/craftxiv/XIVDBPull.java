package tv.tn.craftxiv;


//URL Connections
import java.net.URL;
import java.net.URLConnection;

//File I/O
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.Writer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

//Google/Android implementation JSON
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.apache.commons.lang.StringEscapeUtils; //Unicode Support

public class XIVDBPull {
    private static final int limitAPI = 10;

    private static void queryCollection(String mode){
        try{
            File genericFile = new File("app/src/main/res/data/" + mode + ".json");
            URL xivdbGeneric = new URL("https://api.xivdb.com/" + mode); //URL to parse
            URLConnection urlGeneric = xivdbGeneric.openConnection();
            urlGeneric.addRequestProperty("User-Agent","Mozilla/5.0");
            BufferedReader inputGeneric = new BufferedReader(new InputStreamReader(urlGeneric.getInputStream()));

            Writer genericOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(genericFile),"UTF8"));
            genericOut.append(StringEscapeUtils.unescapeJava(inputGeneric.readLine()));
            genericOut.flush();
            genericOut.close();
            inputGeneric.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void queryDetails(String mode, Object ob){
        try{
            JSONObject rec = (JSONObject) ob;
            String recID = String.valueOf(rec.get("id"));
            File queryFile = new File("app/src/main/res/data/"+ mode + "/" + recID + ".json");
            URL detailURL = new URL("https://api.xivdb.com/" + mode + "/" + recID); //URL to parse
            URLConnection tempDetailURL = detailURL.openConnection();
            tempDetailURL.addRequestProperty("User-Agent","Mozilla/5.0");

            BufferedReader tempDetailStream = new BufferedReader(new InputStreamReader(tempDetailURL.getInputStream()));

            Writer queryOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(queryFile),"UTF8"));
            queryOut.append(StringEscapeUtils.unescapeJava(tempDetailStream.readLine()));
            queryOut.flush();
            queryOut.close();
            tempDetailStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    public static void main(String[] args) {

        JSONParser parser = new JSONParser();

        try{
            //Recipe File
            queryCollection("recipe");


            //Item File
            queryCollection("item");


            //Shop File
            queryCollection("shop");

            int counter = 3;

            //Parse through recipes
            JSONArray recipeJSON = null;
            try {
                recipeJSON = (JSONArray) parser.parse(new FileReader("app/src/main/res/data/recipe.json"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //Parse through recipe details
            for (Object o: recipeJSON){

                if(counter >= limitAPI){
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        queryDetails("recipe", o);
                        counter = 1;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    JSONObject temp = (JSONObject) o;
                    String x = String.valueOf(temp.get("id"));
                    queryDetails("recipe", o);
                    counter++;
                }
            }



            //Parse through items
            JSONArray itemJSON = null;
            try {
                itemJSON = (JSONArray) parser.parse(new FileReader("app/src/main/res/data/item.json"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            for (Object o: itemJSON){

                if(counter >= limitAPI){
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        queryDetails("item", o);
                        counter = 1;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    queryDetails("item", o);
                    counter++;
                }
            }

            //Parse through shops
            JSONArray shopJSON = null;
            try {
                shopJSON = (JSONArray) parser.parse(new FileReader("app/src/main/res/data/shop.json"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            for (Object o: shopJSON){

                if(counter >= limitAPI){
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        queryDetails("shop", o);
                        counter = 1;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    queryDetails("shop", o);
                    counter++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
