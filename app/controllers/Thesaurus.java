package controllers;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.simple.*; // json package, download at http://code.google.com/p/json-simple/

public class Thesaurus {

  public static void main(String[] args) {
// NOTE: replace test_only with your own key
    new SendRequest("happy", "en_US", "78kRR6u1f8NfRAxzPCSq", "json");
  }
} // end of Thesaurus

class SendRequest {
  ArrayList<String> synonyms = new ArrayList<String>();
  final String endpoint = "http://thesaurus.altervista.org/thesaurus/v1";

  public SendRequest(String word, String language, String key, String output) {
      try {
        URL serverAddress = new URL(endpoint + "?word=" + URLEncoder.encode(word, "UTF-8") + "&language=" + language + "&key=" + key + "&output=" + output);
        HttpURLConnection connection = (HttpURLConnection) serverAddress.openConnection();
        connection.connect();
        int rc = connection.getResponseCode();
      if (rc == 200) {
        String line = null;
        BufferedReader br = new BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null)
          sb.append(line + '\n');
        JSONObject obj = (JSONObject) JSONValue.parse(sb.toString());
        JSONArray array = (JSONArray)obj.get("response");
        JSONObject list = new JSONObject();
        for (int i=0; i < array.size(); i++) {
          list = (JSONObject) ((JSONObject) array.get(i)).get("list");
          //System.out.println(list.get("category") + ":" + list.get("synonyms"));
          System.out.println("list--" + list.toJSONString());
          synonyms.add(list.get("synonyms").toString());
        }
        for(String syn: synonyms){
          System.out.println("sy---"+synonyms);
        }
      }
      else {
        System.out.println("HTTP error:" + rc);
      }
      connection.disconnect();
    }
    catch (java.net.MalformedURLException e) {
      e.printStackTrace();
    }
    catch(java.net.ProtocolException e) {
      e.printStackTrace();
    }
    catch (java.io.IOException e) {
      e.printStackTrace();
    }
  }
} // end of SendRequest