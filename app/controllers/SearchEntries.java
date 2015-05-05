package controllers;

import models.Entry;
import models.Keywords;
import models.UrlInfo;
import play.mvc.Controller;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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

    getSynonyms(queryKeywords);

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
  public static void getSynonyms(ArrayList<String> keywords) {
    final String endpoint = "http://www.dictionaryapi.com/api/v1/references/thesaurus/xml/";
    final String key ="79b70eee-858c-486a-b155-a44db036bfe0";
    try {
      for (String keyword : keywords) {
        URL serverAddress = new URL(endpoint + URLEncoder.encode(keyword, "UTF-8") + "?key=" + key);
        HttpURLConnection connection = (HttpURLConnection) serverAddress.openConnection();
        connection.connect();
        int rc = connection.getResponseCode();
        if (rc == 200) {
          String xml = connection.getResponseMessage();
          System.out.println("xml==="+xml);
          DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
          DocumentBuilder builder;
          InputSource is;
          try {
            builder = factory.newDocumentBuilder();
            is = new InputSource(new StringReader(xml));
            Document doc = builder.parse(is);
            NodeList list = doc.getElementsByTagName("syn");
            System.out.println("synonyms"+list.item(0).getTextContent());
          } catch (ParserConfigurationException e) {
          } catch (SAXException e) {
          } catch (IOException e) {
          }
        }
        else{
          System.out.println("HTTP error:" + rc);
        }
      }
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
}
