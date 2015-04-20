package controllers;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import models.EntryDB;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
//import maui.main.MauiTopicExtractor

/**
 * Handles the html data from the entered URL.
 */
public class ProcessUrlData {

  public static ArrayList<String> KEYWORDS = new ArrayList<String>();
  public static String ENTRY_TYPE = "url";
  public static String URL_TYPE = "";
  public static long ENTRY_ID;

  /**
   * Extracts all the information from URL and stores it in the DB.
   * @param url the url entered by the user.
   */
  public static void processUrl(String url) {
    //check if url points to an image.
    System.out.println("ISIMAGE ==="+isImage(url));
    if (isImage(url)) {
      URL_TYPE = "image";
      extractImageInfo(url);
      System.out.println("FINAL KEYWORDS---"+KEYWORDS);
      EntryDB.addEntry(ENTRY_TYPE, KEYWORDS, URL_TYPE, url);
      //store the url information associated with this entry.
     // UrlInfoDB.addUrlInfo(URL_TYPE, url);
    }
    else {
      URL_TYPE = "text";
      //call function to extract keywords from meta data.
      extractMetaData(url);
      //to extract info of main image n the page
      extractMainImageInfo(url);
      System.out.println("FINAL KEYWORDS---"+KEYWORDS);
      EntryDB.addEntry(ENTRY_TYPE, KEYWORDS, URL_TYPE, url);
      //store the url information associated with this entry.
     // UrlInfoDB.addUrlInfo(URL_TYPE, url);
    }
  }

   /**
   * Extracts the meta data from html.
   * @param url The url entered by the user.
   */
  private static void extractMetaData(String url) {
    //ArrayList aList;
    try {
      //System.out.println("enteredUrl-- " + url);
      Document doc = Jsoup.connect(url).get();
      String title = doc.title();

      //get meta keyword content
      String keywords = doc.select("meta[name=keywords]").first().attr("content");
      System.out.println("Meta keyword : " + keywords);
      KEYWORDS = new ArrayList(Arrays.asList(keywords.split(",")));

      //if no keywords then get meta description content
      String description = doc.select("meta[name=description]").get(0).attr("content");
      System.out.println("Meta description : " + description);
      extractKeywords(description);
    }
    catch (IOException e) {
      System.out.println("Exception : " + e);
    }

  }

  /**
   * Extracts the keywords from description of a website.
   * Uses  an open-source library. http://unirest.io/java.
   * @param description the description of the website.
   */
  private static void extractKeywords(String description) {
    try {
      //ArrayList<String> keyword = new ArrayList<String>();
      String endpoint = "http://access.alchemyapi.com/calls/text/TextGetRankedKeywords";
      String apiKey = "f97fec645fc4998b6c91d2113ad5a1d8b7189cec";
      String maxRetrieve = "5";
      String extractMode = "strict";
      String contentType = "application/x-www-form-urlencoded";
      String headerAccept = "application/json";
      String outputMode = "json";
      // These code snippets use an open-source library. http://unirest.io/java
      HttpResponse<JsonNode> response = Unirest.post(endpoint)
          .header("Content-Type", contentType)
          .header("Accept", headerAccept)
          .field("text", description)
          .field("apikey", apiKey)
          .field("maxRetrieve", maxRetrieve)
          .field("keywordExtractMode", extractMode)
          .field("outputMode", outputMode)
          .asJson();
      //Promise<WS.Response>
      System.out.println("Response: " + response.getBody().getObject());
      JSONObject respObj = response.getBody().getObject();
      if (respObj.getString("status").equals("OK")) {
        JSONArray array = respObj.getJSONArray("keywords");
        for (int i = 0; i < array.length(); i++) {
          KEYWORDS.add(array.getJSONObject(i).getString("text"));
        }
      }
      System.out.println("list---"+KEYWORDS);
    }
    catch (Exception e) {
      UnsupportedEncodingException en;
      e.printStackTrace();
    }
  }

  /**
   * Checks if entered url points to an image.
   * @param url the url entered by the user.
   * @return true if points to an image, false otherwise.
   */
  public static boolean isImage(String url) {
    boolean isImage = false;
    try {
      URLConnection connection = new URL(url).openConnection();
      String contentType = connection.getHeaderField("Content-Type");
      isImage = contentType.startsWith("image/");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return isImage;
  }

  /*public static void extractFace(String url) {
    try {
      ArrayList<String> keyword = new ArrayList<String>();
      String endpoint = "http://access.alchemyapi.com/calls/url/URLGetRankedImageFaceTags";
      String apiKey = "f97fec645fc4998b6c91d2113ad5a1d8b7189cec";
      String contentType = "application/x-www-form-urlencoded";
      String headerAccept = "application/json";
      String outputMode = "json";
      HttpResponse<JsonNode> response = Unirest.post(endpoint)
          .header("Content-Type", contentType)
          .header("Accept", headerAccept)
          .field("url", url)
          .field("apikey", apiKey)
          .field("outputMode", outputMode)
          .asJson();
      System.out.println("Response: " + response.getBody().getObject());
      JSONObject respObj = response.getBody().getObject();
      if (respObj.getString("status").equals("OK")) {
        JSONArray array = respObj.getJSONArray("imageFaces");
        for (int i = 0; i < array.length(); i++) {
          keyword.add(array.getJSONObject(i).getString("text"));
        }
      }
      System.out.println("list---"+keyword);
    }
    catch (Exception e) {
      UnsupportedEncodingException en;
      e.printStackTrace();
    }
  }*/

  /**
   * Extracts image information from an image url.
   * Uses Alchemy API.
   * @param url url entered by the user.
   */
  public static void extractImageInfo(String url) {
    try {
      //ArrayList<String> imageKeywords = new ArrayList<String>();
      String endpoint = "http://access.alchemyapi.com/calls/url/URLGetRankedImageKeywords";
      String apiKey = "f97fec645fc4998b6c91d2113ad5a1d8b7189cec";
      String contentType = "application/x-www-form-urlencoded";
      String headerAccept = "application/json";
      String outputMode = "json";
      HttpResponse<JsonNode> response = Unirest.post(endpoint)
          .header("Content-Type", contentType)
          .header("Accept", headerAccept)
          .field("url", url)
          .field("apikey", apiKey)
          .field("outputMode", outputMode)
          .asJson();
      System.out.println("IMAGE Response: " + response.getBody().getObject());
      JSONObject respObj = response.getBody().getObject();
      if (respObj.getString("status").equals("OK")) {
        JSONArray array = respObj.getJSONArray("imageKeywords");
        for (int i = 0; i < array.length(); i++) {
          KEYWORDS.add(array.getJSONObject(i).getString("text"));
        }
      }
      System.out.println("IMAGE list---"+ KEYWORDS);
    }
    catch (Exception e) {
      UnsupportedEncodingException en;
      e.printStackTrace();
    }
  }

  public static void extractMainImageInfo(String url) {
    try {
      String imageUrl = "";
      String endpoint = "http://access.alchemyapi.com/calls/url/URLGetImage";
      String apiKey = "f97fec645fc4998b6c91d2113ad5a1d8b7189cec";
      String contentType = "application/x-www-form-urlencoded";
      String headerAccept = "application/json";
      String outputMode = "json";
      HttpResponse<JsonNode> response = Unirest.post(endpoint)
          .header("Content-Type", contentType)
          .header("Accept", headerAccept)
          .field("url", url)
          .field("apikey", apiKey)
          .field("outputMode", outputMode)
          .asJson();
      System.out.println("IMAGE Response: " + response.getBody().getObject());
      JSONObject respObj = response.getBody().getObject();
      imageUrl = respObj.getString("image");
      if(imageUrl != "" || imageUrl == null) {
        System.out.println("IMAGE url---" + imageUrl);
        extractImageInfo(imageUrl);
      }
    }
    catch (Exception e) {
      UnsupportedEncodingException en;
      e.printStackTrace();
    }
  }


  /*public static void extractKeywordsFromString(String text) {
    val extractor = new MauiTopicExtractor();
    extractor.setOptions(opts)
    extractor.loadModel()

    extractor.configMauiFilter()

    println("Keyphrases are: " + extractor.extractKeyphrasesFromText(text).toList.toString)
  }
*/
}
