package views.pagedata;

import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Backing class for the index form.
 */
public class EntryFormData {
  /** String to hold url. */
  public String url = "http://dictionary.reference.com";

  /**
   * Checks if the form field is valid. Called bu bindRequestForm().
   * @return null if no errors else ist of errors.
   */
  public List<ValidationError> validate() {
    System.out.println("INSIDE VALIDATE url : " + url + "end");
    List<ValidationError> errors = new ArrayList<>();

    if (url == null || url.length() == 0) {
      errors.add(new ValidationError("url", "url is required"));
    }
    return errors.isEmpty() ? null : errors;
  }
  /**
   * No-arg constructor required by play.
   */
  public EntryFormData() {
    //no arg constructor
  }

  /**
   * Constructor that accepts dataEntry.
   * @param dataEntry the dataEntry.
   *//*
  public EntryFormData(DataEntry dataEntry) {
    this.entryId = dataEntry.getEntryId();
    this.url = dataEntry.getUrl();
  }*/



}
