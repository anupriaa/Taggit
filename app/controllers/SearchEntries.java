package controllers;

import models.Entry;
import models.Keywords;
import models.UrlInfo;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import play.mvc.Controller;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

    //getSynonyms(queryKeywords);

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
    System.out.println("keywordIdList---" + keywordIdList);
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
    System.out.println("finalIdList---" + finalIdList);
    List<UrlInfo> urlList = UrlInfo.find().select("url").where().in("urlEntryId", finalIdList).findList();
    /*ArrayList<String> urls = new ArrayList<String>();
    for (UrlInfo urlInfo : urlList) {
      urls.add(urlInfo.getUrl());
    }
    System.out.println("urls in search----" + urls);*/
    return urlList;
  }

  /**
   * Api call to get synonyms of the queried keywords.
   * @param keywords the queried keywords.
   */
  public static void getSynonyms(ArrayList<String> keywords) {
    final String endpoint = "http://www.dictionaryapi.com/api/v1/references/collegiate/xml/";
    final String key = "79b70eee-858c-486a-b155-a44db036bfe0";
    try {
      for (String keyword : keywords) {
        String url = endpoint + keyword + "?key=" + key;
        System.out.print("url--" + url);
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestProperty("User-Agent", USER_AGENT);
        BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

          DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
          DocumentBuilder builder;
          InputSource is;
          try {
            builder = factory.newDocumentBuilder();
            is = new InputSource(new StringReader(response.toString()));
            Document doc = builder.parse(is);
            NodeList list = doc.getElementsByTagName("syn");
            System.out.println("synonyms" + list.item(0).getTextContent());
          }
          catch (Exception e) {
            e.printStackTrace();
          }
        }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
