package controllers;

import models.DataEntryDB;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.Search;
import views.pagedata.EntryFormData;
import views.html.Index;

/**
 * Provides controllers for this application.
 */
public class Application extends Controller {

  /**
   * Returns the home page.
   * @return The resulting home page.
   */
  public static Result index() {
    Form<EntryFormData> formData = Form.form(EntryFormData.class);
    return ok(Index.render(formData));
  }

  /**
   * Returns search, a simple example of a second page to illustrate navigation.
   * @param id The entryId.
   * @return The Search.
   */
  public static Result search(long id) {
    //entryData data = (id == 0) ? new entryData() : new entryData(DataEntryDB.getContact(id));
    EntryFormData data = new EntryFormData();
    Form<EntryFormData> formData = Form.form(EntryFormData.class);
    return ok(Search.render(formData));
  }
  /**
   * Handles the http POST request for new DataEntry form.
   * @return The recent data added to the new DataEntry form.
   */
  public static Result postEntry() {
    System.out.print("INSIDE POST");
    Form<EntryFormData> formData = Form.form(EntryFormData.class).bindFromRequest();
    if (formData.hasErrors()) {
      System.out.println("has error");
      return badRequest(Index.render(formData));
    }
    else {
      EntryFormData data = formData.get();
      ManageHTML.extractMetaData(data.url);
      DataEntryDB.addUrl(data);
      System.out.printf("%s, %n", data.url);
      return ok(Index.render(formData));
    }
  }

}
