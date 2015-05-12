package controllers;


import play.Play;
import play.libs.F;
import play.mvc.Http;
import play.mvc.SimpleResult;
import play.mvc.Result;

public class HttpsAction extends play.mvc.Action.Simple {

  private static final String SSL_HEADER = "x-forwarded-proto";
  private static String httpsPort;

  @Override
  public F.Promise<Result> call(Http.Context ctx) throws Throwable {

    F.Promise<Result> ret = null;

    //redirect if it's not secure
    if (!isHttpsRequest(ctx.request())) {
      String url = redirectHostHttps(ctx) + ctx.request().uri();
      ret = F.Promise.pure(redirect(url));
    }
    else {
      // Let request proceed.
      ret = delegate.call(ctx);
    }

    return ret;
  }

  public static String redirectHostHttps(Http.Context ctx) {

    String[] pieces = ctx.request().host().split(":");
    String ret = "https://" + pieces[0];

    // In Dev mode we want to append the port.
    // In Prod mode, no need to append the port as we use the standard https port, 443.
    if (Play.isDev()) {
      ret += ":" + getHttpsPort();
    }

    return ret;
  }

  public static boolean isHttpsRequest(Http.Request request) {
    return (request.getHeader(SSL_HEADER) != null
        && request.getHeader(SSL_HEADER).contains("https"))
        || isOverHttpsPort(request.host());
  }

  public static boolean isOverHttpsPort(String host) {
    boolean ret = false;
    String[] hostParts = host.split(":");

    if (hostParts.length > 1) {
      ret = hostParts[1].equalsIgnoreCase(getHttpsPort());
    }

    return ret;
  }

  private synchronized static String getHttpsPort() {
    if (httpsPort == null)
      httpsPort = (String) Play.application().configuration().getString("https.port");

    return httpsPort;
  }

}