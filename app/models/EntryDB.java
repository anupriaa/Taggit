package models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
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
   * @param entryType the type of entry.
   * @param keywords the keywords associated with the entry.
   */

  public static void addEntry(String entryType, ArrayList<String> keywords, String urlType, String url) {

    System.out.println("KEYWORD--"+keywords);

    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

    ArrayList<Keywords> keywordList = new ArrayList<>();
    for (String keywordString : keywords) {
      keywordList.add(new Keywords(keywordString));

    }

    Entry entry = new Entry(entryType, timeStamp, keywordList);

    UrlInfo urlInfo = new UrlInfo(urlType, url);


    entry.setUrlInfo(urlInfo);
    urlInfo.setEntry(entry);

    entry.save();

    urlInfo.save();







    //return(ENTRY_ID);
  }
}
