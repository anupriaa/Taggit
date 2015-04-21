package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * Entity class for url information.
 */

@Entity
public class UrlInfo extends Model {
  @Id
  private long urlId;
  private String urlType = "";
  private String url = "";
  private long urlEntryId;

  @OneToOne
  private Entry entry;

  public UrlInfo(String urlType, String url) {
    this.urlType = urlType;
    this.url = url;
  }

  /**
   * Adds the entries.
   *
   * @param entry the contact list.
   */
  public void addEntry(Entry entry) {
    this.entry = entry;
  }

  /**
   * The EBean ORM finder method for database queries.
   *
   * @return The finder method.
   */
  public static Finder<Long, UrlInfo> find() {
    return new Finder<Long, UrlInfo>(Long.class, UrlInfo.class);
  }

  public long getUrlId() {
    return urlId;
  }

  public void setUrlId(long urlId) {
    this.urlId = urlId;
  }

  public String getUrlType() {
    return urlType;
  }

  public void setUrlType(String urlType) {
    this.urlType = urlType;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Entry getEntry() {
    return entry;
  }

  public void setEntry(Entry entry) {
    this.entry = entry;
  }

  public long getUrlEntryId() {
    return urlEntryId;
  }

  public void setUrlEntryId(long urlEntryId) {
    this.urlEntryId = urlEntryId;
  }
}
