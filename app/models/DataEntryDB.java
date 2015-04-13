package models;

import views.pagedata.EntryFormData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mock-up in-memory repository for urls.
 */
public class DataEntryDB {

  private static Map<Long, DataEntry> urls = new HashMap<>();
  private static long currentId = 1;

  /**
   * Creates a DataEntry instance and adds it to an internal data structure.
   * @param entryData the form data.
   */
  public static void addUrl(EntryFormData entryData) {

    DataEntry dataEntry = new DataEntry( entryData.url);
    urls.put(currentId++, dataEntry);
  }

  /**
   * Returns the url list.
   * @return the url list.
   */
  public  static List<DataEntry> getUrls() {
    return new ArrayList<>(urls.values());
  }
}
