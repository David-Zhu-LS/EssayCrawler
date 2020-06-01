import java.util.ArrayList;

/**
 * Essay: This class defines basic methods and fields of an "Essay"
 *
 * @author Lin_Shu
 */
public class Essay {
    String name; // title of the essay
    String url; // url
    int citedCnt; // number of essays that cited this one
    double heat;
    //some essays that cited this one is stored in citList
    ArrayList<String> citList = new ArrayList<>();

    Essay(String _name, String _url, int _citedCnt) {
        name = _name;
        url = _url;
        citedCnt = _citedCnt;
    }
}