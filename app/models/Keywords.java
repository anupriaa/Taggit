package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Entity class for the keywords associated with each entry.
 */
@Entity
public class Keywords extends Model {
  @Id
  private long id;
  private String keyword = "";
  private long keywordEntryId;
  @ManyToOne
  private Entry entry;

  /**
   * Constructor to initialize attributes.
   * @param keyword the keywords associated with the entries.
   */
  public Keywords(String keyword) {
    this.keyword = keyword;
  }

  /**
   * Adds the entries.
   * @param entry the entry list.
   */
  public void addEntry(Entry entry) {
    this.entry = entry;
  }

  /**
   * The EBean ORM finder method for database queries.
   * @return The finder method.
   */
  public static Model.Finder<Long, Keywords> find() {
    return new Model.Finder<Long, Keywords>(Long.class, Keywords.class);
  }

  /**
   * Gets the Id.
   * @return the Id.
   */
  public long getId() {
    return id;
  }

  /**
   * Sets the Id.
   * @param id the Id.
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * Gets the entry instance for this keyword.
   * @return the entry.
   */
  public Entry getEntry() {
    return entry;
  }

  /**
   * Sets the entry instance.
   * @param entry the entry instance.
   */
  public void setEntry(Entry entry) {
    this.entry = entry;
  }

  /**
   * Gets the keywords.
   * @return the keywords.
   */
  public String getKeyword() {
    return keyword;
  }

  /**
   * Sets the keywords.
   * @param keyword the keywords.
   */
  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  /**
   * Gets the entry id from entry table.
   * @return the entry id for this keyword.
   */
  public long getKeywordEntryId() {
    return keywordEntryId;
  }
  /**
   * Sets the entry id for this keyword..
   * @param keywordEntryId the entry id for this keyword.
   */
  public void setKeywordEntryId(long keywordEntryId) {
    this.keywordEntryId = keywordEntryId;
  }
}
