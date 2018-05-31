package tv.tn.craftxiv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class XIVDBPull {
    public static void main(String[] args) {
        try{
            //Recipe File
            File recipeFile = new File("app/src/main/res/data/recipe.json");
            URL xivdbRecipe = new URL("https://api.xivdb.com/recipe"); //URL to parse
            URLConnection urlRecipe = xivdbRecipe.openConnection();
            urlRecipe.addRequestProperty("User-Agent","Mozilla/5.0");
            BufferedReader inputRecipe = new BufferedReader(new InputStreamReader(urlRecipe.getInputStream()));

            Writer recipeOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(recipeFile),"UTF8"));
            recipeOut.append(StringEscapeUtils.unescapeJava(inputRecipe.readLine()));
            recipeOut.flush();
            recipeOut.close();
            inputRecipe.close();

            //Item
            File itemFile = new File("app/src/main/res/data/item.json");
            URL xivdbItem = new URL("https://api.xivdb.com/item"); //URL to parse
            URLConnection urlItem = xivdbItem.openConnection();
            urlItem.addRequestProperty("User-Agent","Mozilla/5.0");
            BufferedReader inputItem = new BufferedReader(new InputStreamReader(urlItem.getInputStream()));

            Writer itemOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(itemFile),"UTF8"));
            itemOut.append(StringEscapeUtils.unescapeJava(inputItem.readLine()));
            itemOut.flush();
            itemOut.close();
            inputItem.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
