package controllers;

import models.Keywords;

import java.util.ArrayList;
import java.util.List;

/**
 * Searches the url entries related to entered keyword.
 */
public class SearchEntries {
  public static void searchUrl(ArrayList<String> queryKeywords) {
    List<Keywords> id = Keywords.find()
                    .select("entry_id")
                    .where()
                    .in("keyword",queryKeywords)
                    .findList();

    System.out.println("keyword in search----"+id.get(0));
  }

}
