package models;


import play.db.ebean.Model;
import javax.persistence.*;

import java.util.ArrayList;

/**
 * entity class for each entry.
 */
@Entity
public class Entry extends Model {
  //private fields for all attributes.
  @Id
  @Column(name = "entry_id", nullable = false, unique = true)
  private long entryId;
  //Entry type can be either url, text or image.
  private String entryType = "";
  private String timestamp = "";

  @OneToOne (mappedBy = "entry", cascade = CascadeType.PERSIST)
  private UrlInfo urlInfo;

  @OneToMany (mappedBy = "entry", cascade = CascadeType.PERSIST)
  private ArrayList<Keywords> keywords;

  /**
   * Constructor to initialize the entry.
   * @param entryType the type of entry.
   * @param timestamp the time and date.
   */
  public Entry(String entryType, String timestamp, ArrayList<Keywords> keywords) {
    this.entryType = entryType;
    this.timestamp = timestamp;
    this.keywords = keywords;
  }

  /**
   * The EBean ORM finder method for database queries.
   * @return The finder method.
   */
  public static Finder<Long, Entry> find() {
    return new Finder<Long, Entry>(Long.class, Entry.class);
  }

  public long getEntryId() {
    return entryId;
  }

  public void setEntryId(long entryId) {
    this.entryId = entryId;
  }

  public String getEntryType() {
    return entryType;
  }

  public void setEntryType(String entryType) {
    this.entryType = entryType;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public UrlInfo getUrlInfo() {
    return urlInfo;
  }

  public void setUrlInfo(UrlInfo urlInfo) {
    this.urlInfo = urlInfo;
  }

  public ArrayList<Keywords> getKeywords() {
    return keywords;
  }

  public void setKeywords(ArrayList<Keywords> keywords) {
    this.keywords = keywords;
  }
}
