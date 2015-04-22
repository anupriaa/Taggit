package models;


import play.db.ebean.Model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;

/**
 * Entity class for each entry.
 */
@Entity
public class Entry extends Model {
  //private fields for all attributes.
  @Id
  @Column(name = "entry_id", nullable = false, unique = true)
  private long entryId;
  //Entry type can be either url, text or image.
  private String entryType = "";
  //timestamp format is yyyyMMdd_HHmmss.
  private String timestamp = "";

  @OneToOne(mappedBy = "entry", cascade = CascadeType.PERSIST)
  private UrlInfo urlInfo;

  @OneToMany(mappedBy = "entry", cascade = CascadeType.PERSIST)
  private ArrayList<Keywords> keywords;

  /**
   * Constructor to initialize the entry.
   * @param entryType the type of entry.
   * @param timestamp the time and date.
   * @param keywords List of Keyword instances.
   * @param urlInfo  Instance of entity UrlInfo.
   */
  public Entry(String entryType, String timestamp, ArrayList<Keywords> keywords, UrlInfo urlInfo) {
    this.entryType = entryType;
    this.timestamp = timestamp;
    this.keywords = keywords;
    this.urlInfo = urlInfo;
  }

  /**
   * The EBean ORM finder method for database queries.
   * @return The finder method.
   */
  public static Finder<Long, Entry> find() {
    return new Finder<Long, Entry>(Long.class, Entry.class);
  }

  /**
   * Gets the Entry Id.
   * @return the Entry id.
   */
  public long getEntryId() {
    return entryId;
  }

  /**
   * Sets the Entry Id.
   * @param entryId the Entry Id.
   */
  public void setEntryId(long entryId) {
    this.entryId = entryId;
  }
  /**
   * Gets the Entry Type.
   * @return the Entry Type.
   */
  public String getEntryType() {
    return entryType;
  }
  /**
   * Sets the Entry Type.
   * @param entryType the Entry Type.
   */
  public void setEntryType(String entryType) {
    this.entryType = entryType;
  }
  /**
   * Gets the Time stamp.
   * @return the Time stamp.
   */
  public String getTimestamp() {
    return timestamp;
  }
  /**
   * Sets the Time stamp.
   * @param timestamp the Time stamp.
   */
  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }
  /**
   * Gets the urlInfo instances.
   * @return the urlInfo.
   */
  public UrlInfo getUrlInfo() {
    return urlInfo;
  }
  /**
   * Sets the urlInfo instances.
   * @param urlInfo the urlInfo.
   */
  public void setUrlInfo(UrlInfo urlInfo) {
    this.urlInfo = urlInfo;
  }
  /**
   * Gets the keyword instance list.
   * @return the keywords list.
   */
  public ArrayList<Keywords> getKeywords() {
    return keywords;
  }
  /**
   * Sets the list of keywords..
   * @param keywords the list of keywords.
   */
  public void setKeywords(ArrayList<Keywords> keywords) {
    this.keywords = keywords;
  }
}
