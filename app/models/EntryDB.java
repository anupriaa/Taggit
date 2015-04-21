package models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Adds a new entry to the database.
 */
public class EntryDB {
  /*@SequenceGenerator(name = "entrySeq", sequenceName = "ENTRY_SEQ", allocationSize = 1, initialValue = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entrySeq")
  private static long ENTRY_ID;*/

  /**
   * Adds entry to database.
   *
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
      //keywordList.add(new Keywords(keywordString));
      Keywords keywords1 = new Keywords(keywordString);
      keywords1.setEntry(entry);
      keywords1.setKeywordEntryId(entry.getEntryId());

      keywords1.save();
    }

  }
}
