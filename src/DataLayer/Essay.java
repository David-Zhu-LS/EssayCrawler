package DataLayer;

import java.util.ArrayList;

/**
 * Essay: This class defines basic methods and fields of an "Essay"
 */
public class Essay {
    public String name; // title of the essay
    public String url; // url
    public int citedCnt; // number of essays that cited this one
    public int year; // published year
    public double heat;
    //some essays that cited this one is stored in citList
    public ArrayList<String> citList = new ArrayList<>();

    public Essay(String _name, String _url, int _citedCnt,int _year) {
        name = _name;
        url = _url;
        citedCnt = _citedCnt;
        year = _year;
    }
}