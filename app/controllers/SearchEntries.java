package controllers;

import models.Entry;
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
    ArrayList<Long> finalIdList = new ArrayList<Long>();

    List<Keywords> idList = Keywords.find()
                    .select("keywordEntryId")
                    .where()
                    .in("keyword", queryKeywords)
                    .findList();


    for (Keywords keywords : idList) {
      keywordIdList.add(keywords.getKeywordEntryId());
    }
    System.out.println("keywordIdList---"+keywordIdList);
    String email = Secured.getUser(ctx());
    System.out.println("LOgged in user in search--" + email);
    List<Entry> entryIdList = Entry.find()
                            .select("entryId")
                            .where()
                            .eq("email", email)
                            .in("entryId", keywordIdList)
                            .findList();
    for (Entry entry : entryIdList) {
      finalIdList.add(entry.getEntryId());
    }
    System.out.println("finalIdList---"+finalIdList);
    List<UrlInfo> urlList = UrlInfo.find().select("url").where().in("urlEntryId", finalIdList).findList();
    /*ArrayList<String> urls = new ArrayList<String>();
    for (UrlInfo urlInfo : urlList) {
      urls.add(urlInfo.getUrl());
    }
    System.out.println("urls in search----" + urls);*/
    return urlList;
  }
}
