import java.util.ArrayList;

public class Essay {
    String name;
    String url;
    int citedCnt;
    ArrayList<String>citList = new ArrayList<>();
    Essay(String _name,String _url,int _citedCnt){
        name = _name;
        url = _url;
        citedCnt = _citedCnt;
    }
}
