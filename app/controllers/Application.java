package controllers;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider;
import models.UrlInfo;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.formdata.LoginFormData;
import views.formdata.SearchFormData;
import views.html.EnterUrl;
//import views.html.Index;
import views.html.Login;
import views.html.Search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides controllers for this application.
 */
public class Application extends Controller {

  /**
   * Returns the home page.
   * @return The resulting home page.
   */
  /*public static Result index() {
    return ok(Index.render("Home page"));
  }*/

  /**
   * Returns the page to enter url.
   * temporary until button is added.
   * @return the form data.
   */
  public static Result enterUrl() {
    String url = Form.form().bindFromRequest().get("url");
    if (url != null) {
      System.out.println("url---" + url);
      int rowCount = UrlInfo.find().select("url").where().ieq("url", url).findRowCount();
      System.out.println("rowcount== " + rowCount);
      if (rowCount == 0) {
        //call class that captures data and feeds it to db.
        ProcessUrlData.processUrl(url);
        return ok(EnterUrl.render("Data entered"));
      }
      else {
        return badRequest(EnterUrl.render("Data already exists."));
      }
    }
    else {
      return badRequest(EnterUrl.render("Data not entered"));
    }
  }

  /**
   * Returns the search page.
   * @return the searchFormData and an empty urlList
   */
  public static Result search() {
    List<UrlInfo> urlList = new ArrayList<>();
    SearchFormData data = new SearchFormData();
    Form<SearchFormData> searchFormData = Form.form(SearchFormData.class).fill(data);
    return ok(Search.render(searchFormData, urlList));
  }

  /**
   * Returns the search page with results.
   * @return the searchFormData and the url list.
   */

  public static Result searchResult() {
    List<UrlInfo> urlList = new ArrayList<>();

    Form<SearchFormData> searchFormData = Form.form(SearchFormData.class).bindFromRequest();
    if (searchFormData.hasErrors()) {
      return badRequest(Search.render(searchFormData, urlList));
    }
    else {
      String queryData = Form.form().bindFromRequest().get("queryData");
      if (queryData != null) {
        ArrayList<String> queryKeywords = new ArrayList<>();
        Collections.addAll(queryKeywords, queryData.split("\\W"));
        urlList = SearchEntries.searchUrl(queryKeywords);
        return ok(Search.render(searchFormData, urlList));
      }
      else {
        return badRequest(Search.render(searchFormData, urlList));
      }
    }
  }

  ///login part from here


  public static Result doLogin() {
    com.feth.play.module.pa.controllers.Authenticate.noCache(response());
    Form<LoginFormData> loginForm = com.feth.play.module.pa.providers.
        .bindFromRequest();
    if (loginForm.hasErrors()) {
      // User did not fill everything properly
      return badRequest(Login.render(loginForm));
    } else {
      // Everything was filled
      return UsernamePasswordAuthProvider.handleLogin(ctx());
    }
  }
}
