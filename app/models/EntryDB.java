package models;

import play.db.ebean.Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Adds a new entry to the database.
 * Adds the url information related to entry.
 * Adds the keywords associated with entry.
 */
public class EntryDB extends Model {

  /**
   * Adds entry to the database.
   * @param entryType the type of entry.
   * @param keywords  the keywords associated with the entry.
   * @param urlType   the type of url
   * @param url       the url.
   */

  public static void addEntry(String entryType, ArrayList<String> keywords, String urlType, String url) {

    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

    ArrayList<Keywords> keywordList = new ArrayList<>();
    for (String keywordString : keywords) {
      keywordList.add(new Keywords(keywordString));

    }
    UrlInfo urlInfo = new UrlInfo(urlType, url);
    Entry entry = new Entry(entryType, timeStamp, keywordList, urlInfo);

    entry.setUrlInfo(urlInfo);
    urlInfo.setEntry(entry);

    entry.setKeywords(keywordList);

    entry.save();
    urlInfo.setUrlEntryId(entry.getEntryId());
    urlInfo.save();

    for (String keywordString : keywords) {
      Keywords keywords1 = new Keywords(keywordString);
      keywords1.setEntry(entry);
      keywords1.setKeywordEntryId(entry.getEntryId());
      keywords1.save();
    }
  }
}
