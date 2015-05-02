package models;

import org.mindrot.jbcrypt.BCrypt;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class for users.
 */
@Entity
public class UserInfo extends Model {
    @Id
    private long id;
    private String email;
    private String password;
    @OneToMany(mappedBy = "userInfo")
    private List<Entry> entries = new ArrayList<>();


    /**
     * Create a new UserInfo object.
     *
     * @param email The email address of the user.
     * @param password The password of the user.
     */
    public UserInfo(String email, String password) {

      this.email = email;
      this.password = password;
    }

    /**
     * The EBean ORM finder method for database queries on Entries.
     *
     * @return The finder method for Contacts.
     */
    public static Finder<Long, UserInfo> find() {
      return new Finder<Long, UserInfo>(Long.class, UserInfo.class);
    }

    /**
     * Set the ID.
     *
     * @param id The ID.
     */
    public void setId(long id) {
      this.id = id;
    }

    /**
     * Set the email.
     *
     * @param email The email address.
     */
    public void setEmail(String email) {
      this.email = email;
    }

    /**
     * Set the password, encrypted.
     *
     * @param password The password.
     */
    public void setPassword(String password) {
      this.password = BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    /**
     * Set the associated Contact.
     *
     * @param entry The entry.
     */
    public void setEntries(List<Entry> entry) {
      this.entries = entry;
    }

    /**
     * Returns the id value to the caller.
     *
     * @return id long value.
     */
    public long getId() {
      return id;
    }

    /**
     * Get the email address.
     *
     * @return String email address.
     */
    public String getEmail() {
      return email;
    }

    /**
     * Get the encrypted password.
     * NOTE:  We do NOT return a decrypted password for security reasons.
     *
     * @return The encrypted password.
     */
    public String getPassword() {
      return password;
    }

    /**
     * Get the associated entries.
     *
     * @return The associated entries.
     */
    public List<Entry> getEntry() {
      return entries;
    }

    /**
     * Add a new entry to the list of entries associated with this User.
     *
     * @param entry The entry to Add.
     */
    public void addEntry(Entry entry) {
      entries.add(entry);
    }

  public List<Entry> getEntries() {
    return entries;
  }
}
