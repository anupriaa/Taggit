package controllers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Handles the html data from the entered URL
 */
public class ManageHTML {

  /**
   * Extracts the meta data from html.
   * @param url The url entered by the user.
   */
  public static void extractMetaData(String url) {
    System.out.println("INSIDE extractMetaData ");
    try {
      Document doc = Jsoup.connect("/http://dictionary.reference.com/").get();
      String title = doc.title();
      //get meta description content
      String description = doc.select("meta[name=description]").get(0).attr("content");
      System.out.println("Meta description : " + description);

      //get meta keyword content
      String keywords = doc.select("meta[name=keywords]").first().attr("content");
      System.out.println("Meta keyword : " + keywords);
    }
    catch (IOException e) {
      System.out.println("Exception : " + e);
    }
  }

}
