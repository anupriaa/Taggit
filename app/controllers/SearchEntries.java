package controllers;

import models.Keywords;
import models.UrlInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Searches the url entries related to entered keyword.
 */
public class SearchEntries {
  public static List<UrlInfo> searchUrl(ArrayList<String> queryKeywords) {
    List<Keywords> idList = Keywords.find()
                    .select("keywordEntryId")
                    .where()
                    .in("keyword",queryKeywords)
                    .findList();

    ArrayList<Long> keywordIdList = new ArrayList<Long>();
    for (Keywords keywords : idList) {
      keywordIdList.add(keywords.getKeywordEntryId());
    }

    System.out.println("keyword in search----" + keywordIdList);
    List<UrlInfo> urlList = UrlInfo.find().select("url").where().in("urlEntryId", keywordIdList).findList();

    ArrayList<String> urls = new ArrayList<String>();
    for (UrlInfo urlInfo : urlList) {
      urls.add(urlInfo.getUrl());
    }
    System.out.println("urls in search----" + urls);
    return urlList;
  }

}
