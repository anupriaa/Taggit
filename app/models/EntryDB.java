package models;

import org.mindrot.jbcrypt.BCrypt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Adds a new entry to the database.
 * Adds the url information related to entry.
 * Adds the keywords associated with entry.
 */
public class EntryDB {

  /**
   * Get a specific User from the database using email.
   *
   * @param email The email address associated with the user.
   * @return The User object or Null if not found.
   */
  public static UserInfo getUser(String email) {
    UserInfo userFromDB = UserInfo.find().where().eq("email", email).findUnique();
    return userFromDB;
  }

  /**
   * Get a specific User from the database using ID.
   *
   * @param id The email address associated with the user.
   * @return The User object or Null if not found.
   */
  public static UserInfo getUser(long id) {
    UserInfo userFromDB = UserInfo.find().byId(id);
    return userFromDB;
  }

  /**
   * Check if an email address is associated with an existing user.
   *
   * @param email The email address to check.
   * @return True if exists, otherwise false.
   */
  public static boolean isUser(String email) {
    int count = UserInfo.find().where().eq("email", email).findRowCount();
    return count >= 1;
  }

  /**
   * Create a new user and save them to the database with encrypted password.
   *
   * @param email    Email Address
   * @param password The password to save with the user.
   */
  public static void addNewUser(String email, String password) {
    UserInfo user = new UserInfo(email, BCrypt.hashpw(password, BCrypt.gensalt(12)));
    user.save();
  }

  /**
   * Returns true if email and password are valid credentials.
   *
   * @param email    The email.
   * @param password The password.
   * @return True if email is a valid user email and password is valid for that email.
   */
  public static boolean isValid(String email, String password) {
    return ((email != null)
        && (password != null)
        && isUser(email)
        && BCrypt.checkpw(password, getUser(email).getPassword()));
  }

  /**
   * Adds entry to the database.
   * @param entryType the type of entry.
   * @param keywords  the keywords associated with the entry.
   * @param urlType   the type of url
   * @param url       the url.
   */

  public static void addEntry(String entryType, ArrayList<String> keywords, String urlType, String url, Long userId) {

    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

    ArrayList<Keywords> keywordList = new ArrayList<>();
    for (String keywordString : keywords) {
      keywordList.add(new Keywords(keywordString));

    }
    UrlInfo urlInfo = new UrlInfo(urlType, url);
    UserInfo userInfo = getUser(userId);
    Entry entry = new Entry(entryType, timeStamp, keywordList, urlInfo, userInfo);

    entry.setUrlInfo(urlInfo);
    urlInfo.setEntry(entry);

    entry.setKeywords(keywordList);

    entry.save();
    urlInfo.setUrlEntryId(entry.getEntryId());
    urlInfo.save();

    for (String keywordString : keywords) {
      Keywords keywords1 = new Keywords(keywordString);
      keywords1.setEntry(entry);
      keywords1.setKeywordEntryId(entry.getEntryId());
      keywords1.save();
    }
  }




}
