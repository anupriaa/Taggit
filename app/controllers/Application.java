package controllers;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.EnterUrl;
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
    //Form<UrlFormData> formData = Form.form(UrlFormData.class);
    return ok(Index.render("Home page"));
  }

  /**
   * Returns the page to enter url.
   * temporary until button is added.
   * @return the form data.
   */
  public static Result enterUrl() {
    String url = Form.form().bindFromRequest().get("url");
    System.out.println("url---"+url);
    if (url != null) {
      //call class that captures data and feeds it to db.
      ProcessUrlData.processUrl(url);
      return ok(EnterUrl.render("Data entered"));
    }
    else {
      return badRequest(EnterUrl.render("Data not entered"));
    }

  }
  /**
   * Returns search, a simple example of a second page to illustrate navigation.
   * @param id The entryId.
   * @return The Search.
   */
  /*public static Result search(long id) {
    //entryData data = (id == 0) ? new entryData() : new entryData(DataEntryDB.getContact(id));
    UrlFormData data = new UrlFormData();
    Form<UrlFormData> formData = Form.form(UrlFormData.class);
    return ok(Search.render(formData));
  }*/
  /**
   * Handles the http POST request for new DataEntry form.
   * @return The recent data added to the new DataEntry form.
   */
  /*public static String processUrl(String url) {
    System.out.print("INSIDE processUrl");


      ProcessUrlData.extractMetaData(url);
      //ManageAPICall.alchemyAPICall(data.url);
      //DataEntryDB.addUrl(data);
      System.out.printf("%s, %n", url);
    return("ok");

  }*/

}
