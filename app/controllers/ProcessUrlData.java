package controllers;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import models.EntryDB;
import models.UrlInfo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import play.mvc.Controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Handles the html data from the entered URL.
 */
public class ProcessUrlData extends Controller {

  /**
   * ArrayList to store associated keywords.
   */
  public static ArrayList<String> keywords = new ArrayList<String>();
  /**
   * Stores the type of entry - url or text.
   */
  public static String entryType = "url";
  /**
   * Stores the type of url - image or non-image.
   */
  public static String urlType = "";
  /**
   * Store the API key for Alchemy API.
   */
  public static String apiKey = "f97fec645fc4998b6c91d2113ad5a1d8b7189cec";

  /**
   * Extracts all the information from URL and stores it in the DB.
   * @param url the url entered by the user.
   */
  public static void processUrl(String url , Long userId) {
    //check if url points to an image.
    if (isImage(url)) {
      keywords = new ArrayList<String>();
      urlType = "image";
      extractImageInfo(url);
      Collections.copy(keywords, removeWhiteSpaces(keywords));
      EntryDB.addEntry(entryType, keywords, urlType, url, userId);
    }
    else {
      keywords = new ArrayList<>();
      urlType = "text";

      //call function to extract keywords from meta data from url.
      extractMetaData(url);
      //to extract info of main image n the page
      extractMainImageInfo(url);
      Collections.copy(keywords, removeWhiteSpaces(keywords));
      EntryDB.addEntry(entryType, keywords, urlType, url, userId);
    }
  }

  /**
   * Extracts the meta data from html.
   * @param url The url entered by the user.
   */
  private static void extractMetaData(String url) {
    try {
      Document doc = Jsoup.connect(url).get();

      //get meta keyword content
      String keywords = doc.select("meta[name=keywords]").first().attr("content");
      ProcessUrlData.keywords = new ArrayList<String>(Arrays.asList(keywords.split(",")));

      //get meta description content
      String description = doc.select("meta[name=description]").get(0).attr("content");
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
      String endpoint = "http://access.alchemyapi.com/calls/text/TextGetRankedKeywords";
      String maxRetrieve = "5";
      String extractMode = "strict";
      String contentType = "application/x-www-form-urlencoded";
      String headerAccept = "application/json";
      String outputMode = "json";
      HttpResponse<JsonNode> response = Unirest.post(endpoint)
          .header("Content-Type", contentType)
          .header("Accept", headerAccept)
          .field("text", description)
          .field("apikey", apiKey)
          .field("maxRetrieve", maxRetrieve)
          .field("keywordExtractMode", extractMode)
          .field("outputMode", outputMode)
          .asJson();
      JSONObject respObj = response.getBody().getObject();
      if (respObj.getString("status").equals("OK")) {
        JSONArray array = respObj.getJSONArray("keywords");
        for (int i = 0; i < array.length(); i++) {
          keywords.add(array.getJSONObject(i).getString("text"));
        }
      }
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

  /**
   * Extracts image information from an image url.
   * Uses Alchemy API.
   * @param url url entered by the user.
   */
  public static void extractImageInfo(String url) {
    try {
      String endpoint = "http://access.alchemyapi.com/calls/url/URLGetRankedImageKeywords";
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
      JSONObject respObj = response.getBody().getObject();
      if (respObj.getString("status").equals("OK")) {
        JSONArray array = respObj.getJSONArray("imageKeywords");
        for (int i = 0; i < array.length(); i++) {
          keywords.add(array.getJSONObject(i).getString("text"));
        }
      }
    }
    catch (Exception e) {
      UnsupportedEncodingException en;
      e.printStackTrace();
    }
  }

  /**
   * Extracts the information from the main image of non-image url.
   * @param url the url entered by user.
   */
  public static void extractMainImageInfo(String url) {
    try {
      String imageUrl = "";
      String endpoint = "http://access.alchemyapi.com/calls/url/URLGetImage";
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
      JSONObject respObj = response.getBody().getObject();
      imageUrl = respObj.getString("image");
      if (!imageUrl.equals("")  || imageUrl == null) {
        extractImageInfo(imageUrl);
      }
    }
    catch (Exception e) {
      UnsupportedEncodingException en;
      e.printStackTrace();
    }
  }

  /**
   * Removes the leading and trailing white spaces from each keyword.
   * @param keywords the keywords extracted.
   * @return the trimmed keywords.
   */
  public static ArrayList<String> removeWhiteSpaces(ArrayList<String> keywords) {
    ArrayList<String> keywordList = new ArrayList<String>();
    for (String keyword: keywords) {
      keywordList.add(keyword.trim());
    }
    return keywordList;
  }
}
