package models;

/**
 * Mockup model for backend database.
 */
public class DataEntry {
  private long entryId;
  private String entryType;
  private String content;
  private long tagId;
  private String tag;
  private String url;

  /**
   * Creates an instance of DataEntry.
   * @param entryId the entryId.
   * @param entryType the type of entry.
   * @param content the content of the entry.
   * @param tagId the tagId number.
   * @param tag the tag associated with the entry.
   *//*
  public DataEntry(long entryId, String entryType, String content, long tagId, String tag) {
    this.entryType = entryType;
    this.content = content;
    this.tagId = tagId;
    this.entryId = entryId;
    this.tag = tag;
  }
*/
  /**
   * Creates an instance of DataEntry.
   * @param url the url.
   */
  public DataEntry(String url) {
    this.url = url;
  }

  /**
   * Returns the url.
   * @return the url.
   */
  public String getUrl() {
    return url;
  }

  /**
   * Returns the entry type.
   * @return the entry type.
   */
  public String getEntryType() {
    return entryType;
  }

  /**
   * Returns the content.
   * @return the content.
   */
  public String getContent() {
    return content;
  }

  /**
   * Returns the tagId number.
   * @return the tagId number.
   */
  public long getTagId() {
    return tagId;
  }

  /**
   * Returns the tag.
   * @return the tag.
   */
  public String getTag() {
    return tag;
  }
}
