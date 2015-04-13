package controllers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
//import maui.main.MauiTopicExtractor

/**
 * Handles the html data from the entered URL.
 */
public class ManageHTML {

  /**
   * Extracts the meta data from html.
   * @param enteredUrl The url entered by the user.
   */
  public static void extractMetaData(String enteredUrl) {
    ArrayList aList;
    System.out.println("INSIDE extractMetaData ");
    try {
      System.out.println("enteredUrl-- " + enteredUrl);
      //URL url = new URL("http://www.apple.com/pr/");
      //Document document = Jsoup.parse(url, 3000);
      //System.out.println("encoding== " + java.net.URLEncoder.encode("http://dictionary.reference.com", "UTF-8"));
      Document doc = Jsoup.connect(enteredUrl).get();
      String title = doc.title();

      //get meta keyword content
      String keywords = doc.select("meta[name=keywords]").first().attr("content");
      System.out.println("Meta keyword : " + keywords);
      aList = new ArrayList(Arrays.asList(keywords.split(",")));

      //if no keywords then get meta description content
      String description = doc.select("meta[name=description]").get(0).attr("content");
      System.out.println("Meta description : " + description);

    }
    catch (IOException e) {
      System.out.println("Exception : " + e);
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
