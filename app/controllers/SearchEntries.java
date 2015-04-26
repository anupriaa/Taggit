package controllers;

import models.Keywords;
import models.UrlInfo;
import play.mvc.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Searches the url entries related to entered keyword.
 */
public class SearchEntries extends Controller {
  /**
   * Queries the database for urls related to entered keyword.
   * @param queryKeywords the entered keywords.
   * @return the list of urls.
   */
  public static List<UrlInfo> searchUrl(ArrayList<String> queryKeywords) {

    ArrayList<Long> keywordIdList = new ArrayList<Long>();

    List<Keywords> idList = Keywords.find()
                    .select("keywordEntryId")
                    .where()
                    .in("keyword", queryKeywords)
                    .findList();


    for (Keywords keywords : idList) {
      keywordIdList.add(keywords.getKeywordEntryId());
    }
    List<UrlInfo> urlList = UrlInfo.find().select("url").where().in("urlEntryId", keywordIdList).findList();
    /*ArrayList<String> urls = new ArrayList<String>();
    for (UrlInfo urlInfo : urlList) {
      urls.add(urlInfo.getUrl());
    }
    System.out.println("urls in search----" + urls);*/
    return urlList;
  }
}
