package controllers;

import models.Entry;
import models.EntryDB;
import models.Keywords;
import models.UrlInfo;
import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.formdata.LoginFormData;
import views.formdata.SearchFormData;
import views.formdata.SignupFormData;
import views.html.AddBookmarklet;
import views.html.Bookmarklet;
import views.html.EnterUrl;
import views.html.Index;
import views.html.Login;
import views.html.MyLinks;
import views.html.Search;
import views.html.Signup;
import wordcloud.CollisionMode;
import wordcloud.WordCloud;
import wordcloud.WordFrequency;
import wordcloud.bg.RectangleBackground;
import wordcloud.font.scale.LinearFontScalar;
import wordcloud.nlp.FrequencyAnalizer;
import wordcloud.palette.ColorPalette;

import java.awt.Color;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides controllers for this application.
 */
public class Application extends Controller {

  /**
   * True if there are any search results.
   * false other wise.
   */
  public static boolean isSearchResult = false;

  /**
   * Returns the home page.
   *
   * @return The resulting home page.
   */
  public static Result index() {
    //session().clear();
    //List<Tag> tag = cloud();
    //cloud();
    return ok(Index.render("Home", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx())));
  }
  /**
   * Returns the home page.
   *
   * @return The resulting home page.
   */
  public static Result addBookmarklet() {
    //session().clear();
    //List<Tag> tag = cloud();
    //cloud();
    return ok(AddBookmarklet.render("AddBookmarklet", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx())));
  }
  /**
   * Provides the Login page (only to unauthenticated users).
   *@param message The message.
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
      return redirect(routes.Application.search());
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
    //Long userId = Long.parseLong(Form.form().bindFromRequest().get("UserId"));
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
    buildCloud();
    isSearchResult = false;
    List<UrlInfo> urlList = new ArrayList<>();
    SearchFormData data = new SearchFormData();
    Form<SearchFormData> searchFormData = Form.form(SearchFormData.class).fill(data);
    return ok(Search.render("Search", Secured.isLoggedIn(ctx()),
        Secured.getUserInfo(ctx()), searchFormData, urlList, isSearchResult));
  }

  /**
   * Returns the search page with results.
   * @return the searchFormData and the url list.
   */
  @Security.Authenticated(Secured.class)
  public static Result searchResult() {
    isSearchResult = true;
    List<UrlInfo> urlList = new ArrayList<>();

    Form<SearchFormData> searchFormData = Form.form(SearchFormData.class).bindFromRequest();
    if (searchFormData.hasErrors()) {
      return badRequest(Search.render("Search", Secured.isLoggedIn(ctx()),
          Secured.getUserInfo(ctx()), searchFormData, urlList, isSearchResult));
    }
    else {
      String queryData = Form.form().bindFromRequest().get("queryData");
      System.out.println("queryData in application---" + queryData);
      if (queryData != null) {
        ArrayList<String> queryKeywords = new ArrayList<>();
        Collections.addAll(queryKeywords, queryData.toLowerCase().split("\\W"));
        System.out.println("query arraylist--" + queryKeywords);
        urlList = SearchEntries.searchUrl(queryKeywords);
        return ok(Search.render("Search", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), searchFormData,
            urlList, isSearchResult));
      }
      else {
        return badRequest(Search.render("Search", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()),
            searchFormData, urlList, isSearchResult));
      }
    }
  }
  /**
   * Returns the search page with results.
   * @return the searchFormData and the url list.
   */
  @Security.Authenticated(Secured.class)
  public static Result myLinks() {
    isSearchResult = true;
    List<UrlInfo> urlList = new ArrayList<>();
        urlList = SearchEntries.searchAllUrl();
        return ok(MyLinks.render("MyLinks", Secured.isLoggedIn(ctx()),
            Secured.getUserInfo(ctx()), urlList, isSearchResult));
  }

  /**
   * Returns the page to enter url.
   * temporary until button is added.
   * @return the form data.
   */
  @Security.Authenticated(Secured.class)
  public static Result enterUrlTest() {
    ArrayList<Long> entryIdList = new ArrayList<Long>();
    String url = Form.form().bindFromRequest().get("url");
    String bk = Form.form().bindFromRequest().get("bk");
    //Long userId = Long.parseLong(Form.form().bindFromRequest().get("UserId"));
    System.out.println("INSIDE BOOKMARKLET URL TEST");
    if (url != null) {
      System.out.println("url---" + url);
      List<Entry> idList      = Entry.find()
                              .select("entryId")
                              .where()
                              .eq("email", Secured.getUser(ctx()))
                              .findList();
      for (Entry entry : idList) {
        entryIdList.add(entry.getEntryId());
      }
      System.out.println("BK ENTRY ID LIST--" + entryIdList);
      int rowCount = UrlInfo.find()
                  .select("url")
                  .where()
                  .ieq("url", url)
                  .in("urlEntryId", entryIdList)
                  .findRowCount();
      System.out.println("rowcount== " + rowCount);
      if (rowCount == 0) {
        //call class that captures data and feeds it to db.
        ProcessUrlData.processUrl(url);
        return ok(Bookmarklet.render("Bookmarklet", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx())));
      }
      else {
        return badRequest(Bookmarklet.render("Bookmarklet", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx())));
      }
    }
    else {
      return badRequest(Bookmarklet.render("Bookmarklet", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx())));
    }
  }
  /**
   * Allows JavaScript to access routes.
   * Not using as of now.
   * This is necessary to allow JavaScript issue GET/POST requests to the
   * correct route.
   *
   * @return Result object.
   */
  /*public static Result jsRoutes() {
    response().setContentType("text/javascript");
    return ok(
        // Every route accessible to JavaScript needs to be added here.
        Routes.javascriptRouter("jsRoutes" , controllers.routes.javascript.Application.enterUrlTest()));
  }*/

   /**
   * Define any extra CORS headers needed for option requests (see http://enable-cors.org/server.html for more info).
   * @param all all.
   * @return ok after setting header.
   */
  public static Result preflight(String all) {
    System.out.println("PREFLIGHT");
    response().setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
    response().setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept");
    return ok();
  }

  /**
   * Builds a png of the word cloud of the logged in user's keywords.
   * The png is displayed in the search screen.
   */
  public static void buildCloud() {
    System.out.println("INSIDE BUILD");
    List<String> keywords = new ArrayList<String>();
    ArrayList<Long> entryIdList = new ArrayList<Long>();
    List<Entry> idList      = Entry.find()
                            .select("entryId")
                            .where()
                            .eq("email", Secured.getUser(ctx()))
                            .findList();
    for (Entry entry : idList) {
      entryIdList.add(entry.getEntryId());
    }
    List<Keywords> list = Keywords.find()
                          .select("keyword")
                          .where()
                           .in("keywordEntryId", entryIdList)
                            .findList();
    for (Keywords keyword: list) {
      keywords.add(keyword.getKeyword());
    }
    try {
      final FrequencyAnalizer frequencyAnalizer = new FrequencyAnalizer();
      //frequencyAnalizer.setWordFrequencesToReturn(20);
      //frequencyAnalizer.setMinWordLength(4);
      //frequencyAnalizer.setStopWords(loadStopWords());

      final List<WordFrequency> wordFrequencies = frequencyAnalizer.load(keywords);
      //System.out.println("list=====00"+Arrays.toString(list.toArray()));
      final WordCloud wordCloud = new WordCloud(400, 200, CollisionMode.PIXEL_PERFECT);
      wordCloud.setPadding(2);
      System.out.println("Before back");
      /*try {
        wordCloud.setBackground(new PixelBoundryBackground(getInputStream("public/images/TagIt.png")));
      }
      catch (Exception e) {
        System.out.println("e.p");
        e.printStackTrace();
      }*/
      System.out.println("Afer back");
      //wordCloud.setBackground(new CircleBackground(150));
      wordCloud.setBackground(new RectangleBackground(400, 200));
      wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1),
          new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
      //wordCloud.setFontScalar(new LinearFontScalar(10, 40));
      //wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
      //wordCloud.setColorPalette(buildRandomColorPallete(20));
      wordCloud.setFontScalar(new LinearFontScalar(10, 40));
      wordCloud.build(wordFrequencies);
      System.out.println("Before writing");
      wordCloud.writeToFile("public/images/Taggit_wordcloud.png");
      System.out.println("After writing");
    }
    catch (Exception e) {
      System.out.print("Exception" + e);
    }
  }

  /**
   * Gets the input stream to set background of word cloud.
   * @param path path of the image for the background.
   * @return the path thread.
   */
  private static InputStream getInputStream(String path) {
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
  }

  /**
   * Makes a word cloud with links.
   * Not using it as of now.
   * @return the tags added to the cloud.
   */
  public static List cloud() {
    final double maxWeight = 38.0;
    Cloud cloud = new Cloud();  // create cloud
    cloud.setMaxWeight(maxWeight);   // max font size
    cloud.setWordPattern("circle");
    cloud.setTagCase(Cloud.Case.CAPITALIZATION);
    System.out.println("INSIDE CLOUD");
    //List<String> keywords = new ArrayList<String>();
    List<Keywords> list = Keywords.find()
                        .select("keyword")
                        .select("score")
                        .where()
                        .findList();

    for (Keywords keyword: list) {
      //keywords.add(keyword.getKeyword());
      Long id = Keywords.find().select("keywordEntryId").where().eq("keyword",
          keyword.getKeyword()).findUnique().getId();
      String url = UrlInfo.find().select("url").where().eq("entryId", id).findUnique().getUrl();
      Tag tag = new Tag(keyword.getKeyword(), url);   // creates a tag
      cloud.addTag(tag);
    }
    return (cloud.allTags());

  }
}
