package ir.taxi1880.manager.app;

import ir.taxi1880.manager.BuildConfig;

public class EndPoints {

/*TODO : check apis and ports before release*/

  public static final String IP = (BuildConfig.DEBUG)
          ? "http://172.16.2.203"
//          ? "http://turbotaxi.ir"
          : "http://turbotaxi.ir";
//          : "http://172.16.2.203";

  public static final String HAKWEYE_IP = (BuildConfig.DEBUG)
          ? "http://172.16.2.201"
//          ? "http://turbotaxi.ir"
          : "http://turbotaxi.ir";
//          : "http://172.16.2.203";


  public static final String APIPort = (BuildConfig.DEBUG) ? "1881" : "1881";
  public static final String HAWKEYE_APIPort = (BuildConfig.DEBUG) ? "1890" : "1890";

  public static final String WEBSERVICE_PATH = IP + ":" + APIPort + "/api/manager/v2/splash/";
  public static final String LINE_PATH  = IP + ":" + APIPort + "/api/manager/v2/line";
  public static final String QUEUE_PATH  = IP + ":" + APIPort + "/api/manager/v2/queue";

  public static final String HAWKEYE_PATH = HAKWEYE_IP + ":" + HAWKEYE_APIPort + "/api/user/v1/";
  public static final String HAWKEYE_LOGIN_PATH = HAKWEYE_IP + ":" + HAWKEYE_APIPort + "/api/user/v1/login/phone/";

  public static final String APP_INFO = WEBSERVICE_PATH ;
  public static final String GET_LINE = LINE_PATH ;
  public static final String GET_QUEUE = QUEUE_PATH ;
  public static final String SUMMERY = WEBSERVICE_PATH ; //TODO check later


  /******************************** refresh token Api *********************************/

  public static final String REFRESH_TOKEN = HAWKEYE_PATH + "token";
  public static final String LOGIN = HAWKEYE_PATH + "login";
  public static final String VERIFICATION =HAWKEYE_LOGIN_PATH + "verification";
  public static final String CHECK = HAWKEYE_LOGIN_PATH + "check";
}
