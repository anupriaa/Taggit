package views.formdata;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to manipulate tagId type .
 */
public class TelephoneTypes {
  private static String[] types = {"Home", "Work", "Mobile"};

  /**
   * To get the tagId types.
   *
   * @return the map of tagId types.
   */
  public static Map<String, Boolean> getTypes() {
    Map<String, Boolean> typeMap = new HashMap<String, Boolean>();
    for (String type : types) {
      typeMap.put(type, false);
    }
    return typeMap;
  }

  /**
   * Checks if tagId type is valid or not.
   *
   * @param type the tagId type.
   * @return true if valid tagId type, else false.
   */
  public static boolean isType(String type) {
    return getTypes().containsKey(type);
  }

  /**
   * Checks for acceptable tagId type and sets to true if selected.
   *
   * @param type the tagId type.
   * @return the map of tagId types.
   */
  public static Map<String, Boolean> getTypes(String type) {
    Map<String, Boolean> typeMap = getTypes();
    if (isType(type)) {
      typeMap.put(type, true);
    }
    return typeMap;
  }
}
