package models;

import controllers.Secured;
import org.mindrot.jbcrypt.BCrypt;
import play.mvc.Http;

import javax.naming.Context;
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

  public static void addEntry(String entryType, ArrayList<String> keywords, ArrayList<Double> keywordRelevance, String urlType, String url,Http.Context context) {

    /*for(double rel: keywordRelevance) {
      System.out.println("Rele---"+rel);
    }*/

    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

    ArrayList<Keywords> keywordList = new ArrayList<>();
    int i = 0;
    for (String keywordString : keywords) {
      keywordList.add(new Keywords(keywordString, keywordRelevance.get(i)));
      i++;
    }

    UrlInfo urlInfo = new UrlInfo(urlType, url);
    String email = Secured.getUser(context);
    UserInfo userInfo = getUser(email);
    Entry entry = new Entry(entryType, timeStamp, keywordList, urlInfo, userInfo);
    //get logged in users email.
    System.out.println("email of logged in ---" + Secured.getUser(context));
    entry.setEmail(email);

    entry.setUrlInfo(urlInfo);
    entry.setUserInfo(userInfo);
    urlInfo.setEntry(entry);

    entry.setKeywords(keywordList);

    entry.save();
    urlInfo.setUrlEntryId(entry.getEntryId());
    urlInfo.save();
    i=0;
    for (String keywordString : keywords) {
      Keywords keywords1 = new Keywords(keywordString, keywordRelevance.get(i));
      keywords1.setEntry(entry);
      keywords1.setKeywordEntryId(entry.getEntryId());
      keywords1.save();
      i++;
    }
  }




}
