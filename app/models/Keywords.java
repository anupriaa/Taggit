package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by Anupriya on 4/20/2015.
 */
@Entity
public class Keywords extends Model {
  @Id
  private long id;
  //ArrayList<Keywords> keyword = new ArrayList<Keywords>();
  private String keyword = "";
  private long keywordEntryId;
  @ManyToOne
  private Entry entry;

  public Keywords(String keyword) {
    this.keyword = keyword;
  }

  /**
   * Adds the entries.
   *
   * @param entry the entry list.
   */
  public void addEntry(Entry entry) {
    this.entry = entry;
  }

  /**
   * The EBean ORM finder method for database queries.
   *
   * @return The finder method.
   */
  public static Model.Finder<Long, Keywords> find() {
    return new Model.Finder<Long, Keywords>(Long.class, Keywords.class);
  }


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  /*public ArrayList<Keywords> getKeyword() {
    return keyword;
  }

  public void setKeyword(ArrayList<Keywords> keyword) {
    this.keyword = keyword;
  }*/

  public Entry getEntry() {
    return entry;
  }

  public void setEntry(Entry entry) {
    this.entry = entry;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public long getKeywordEntryId() {
    return keywordEntryId;
  }

  public void setKeywordEntryId(long keywordEntryId) {
    this.keywordEntryId = keywordEntryId;
  }
}
