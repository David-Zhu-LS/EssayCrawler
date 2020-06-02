package BusinessLogicLayer.Kits;

import java.io.InputStream;
import java.net.URL;

/**
 * Kits.UrlKit class:包括一些url相关的函数：
 * 1) checkUrl:检查url是否合法
 */
public class UrlKit {
    //checkUrl: check whether the url is correct.
    public static boolean checkUrl(String iurl){
        URL turl;
        try{
            turl = new URL(iurl);
            InputStream is = turl.openStream();
            is.close();
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
