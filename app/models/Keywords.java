package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.ArrayList;

/**
 * Created by Anupriya on 4/20/2015.
 */
@Entity
public class Keywords extends Model{
  @Id
  private long id;
  private String keyword = "";
  @ManyToOne
  //@JoinColumn(name = "keyword_id", referencedColumnName = "entry_id")
  ArrayList<Entry> entry = new ArrayList<>();

  public Keywords(String keyword) {
    this.keyword = keyword;
  }

  /**
   * Adds the entries.
   * @param entry the entry list.
   */
  public void addEntry(Entry entry) {
    this.entry.add(entry);
  }
  /**
   * The EBean ORM finder method for database queries.
   * @return The finder method.
   */
  public static Model.Finder<Long, Keywords> find() {
    return new Model.Finder<Long, Keywords>(Long.class, Keywords.class);
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public ArrayList<Entry> getEntry() {
    return entry;
  }

  public void setEntry(ArrayList<Entry> entry) {
    this.entry = entry;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
}
