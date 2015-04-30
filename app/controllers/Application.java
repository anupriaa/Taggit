package controllers;

import models.EntryDB;
import models.UrlInfo;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.formdata.LoginFormData;
import views.formdata.SearchFormData;
import views.formdata.SignupFormData;
import views.html.EnterUrl;
import views.html.Index;
import views.html.Login;
import views.html.Search;
import views.html.Signup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides controllers for this application.
 */
public class Application extends Controller {

  /**
   * Returns the home page.
   *
   * @return The resulting home page.
   */
  public static Result index() {
    return ok(Index.render("Home", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx())));
  }

  /**
   * Provides the Login page (only to unauthenticated users).
   *
   * @return The Login page.
   */
  public static Result login(String message) {
    Form<LoginFormData> formData = Form.form(LoginFormData.class);
    return ok(Login.render("Login", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), formData, message));
  }

  /**
   * Processes a login form submission from an unauthenticated user.
   * First we bind the HTTP POST data to an instance of LoginFormData.
   * The binding process will invoke the LoginFormData.validate() method.
   * If errors are found, re-render the page, displaying the error data.
   * If errors not found, render the page with the good data.
   *
   * @return The index page with the results of validation.
   */
  public static Result postLogin() {

    // Get the submitted form data from the request object, and run validation.
    Form<LoginFormData> formData = Form.form(LoginFormData.class).bindFromRequest();

    if (formData.hasErrors()) {
      for (String key : formData.errors().keySet()) {
        List<ValidationError> currentError = formData.errors().get(key);
        for (play.data.validation.ValidationError error : currentError) {
          if (!error.message().equals("")) {
            flash(key, error.message());
          }
        }
      }
      return badRequest(Login.render("Login", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), formData, ""));
    }
    else {
      // email/password OK, so now we set the session variable and only go to authenticated pages.
      session().clear();
      session("email", formData.get().email);
      return redirect(routes.Application.index());
    }
  }

  /**
   * Logs out (only for authenticated users) and returns them to the Index page.
   *
   * @return A redirect to the Index page.
   */
  @Security.Authenticated(Secured.class)
  public static Result logout() {
    session().clear();
    return redirect(routes.Application.index());
  }

  /**
   * Provides the Signup page (only to unauthenticated users).
   *
   * @return The Signup page.
   */
  public static Result signup() {
    Form<SignupFormData> formData = Form.form(SignupFormData.class);
    return ok(Signup.render("Signup", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), formData));
  }

  /**
   * Processes a Signup form submission from an unauthenticated user.
   * First we bind the HTTP POST data to an instance of SignupFormData.
   * The binding process will invoke the SignupFormData.validate() method.
   * If errors are found, re-render the page, displaying the error data.
   * If errors not found, take the user to the login screen and display message.
   *
   * @return The index page with the results of validation.
   */
  public static Result postSignup() {

    // Get the submitted form data from the request object, and run validation.
    Form<SignupFormData> formData = Form.form(SignupFormData.class).bindFromRequest();

    if (formData.hasErrors()) {
      for (String key : formData.errors().keySet()) {
        List<ValidationError> currentError = formData.errors().get(key);
        for (play.data.validation.ValidationError error : currentError) {
          if (!error.message().equals("")) {
            flash(key, error.message());
          }
        }
      }
      return badRequest(Signup.render("Signup", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), formData));
    }
    else {
      // email/password OK, so now we set the session variable and only go to authenticated pages.
      //session().clear();
      //session("email", formData.get().email);
      SignupFormData dataFromForm = formData.get();
      EntryDB.addNewUser(dataFromForm.email, dataFromForm.password);
      return redirect(routes.Application.login("Success"));
    }
  }

  /**
   * Returns the page to enter url.
   * temporary until button is added.
   * @return the form data.
   */
  @Security.Authenticated(Secured.class)
  public static Result enterUrl() {
    String url = Form.form().bindFromRequest().get("url");
    if (url != null) {
      System.out.println("url---" + url);
      int rowCount = UrlInfo.find().select("url").where().ieq("url", url).findRowCount();
      System.out.println("rowcount== " + rowCount);
      if (rowCount == 0) {
        //call class that captures data and feeds it to db.
        ProcessUrlData.processUrl(url);
        return ok(EnterUrl.render("EnterUrl", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx())));
      }
      else {
        return badRequest(EnterUrl.render("EnterUrl", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx())));
      }
    }
    else {
      return badRequest(EnterUrl.render("EnterUrl", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx())));
    }
  }

  /**
   * Returns the search page.
   * @return the searchFormData and an empty urlList
   */
  @Security.Authenticated(Secured.class)
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
  @Security.Authenticated(Secured.class)
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
}
