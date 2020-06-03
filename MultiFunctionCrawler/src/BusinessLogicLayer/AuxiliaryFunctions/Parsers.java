package BusinessLogicLayer.AuxiliaryFunctions;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parsers: 解析类，包括一些解析得到内容的函数
 * 1) parseToGetUrls:分析句法得到urls
 * 2) parseToGetEmailAddr:分析句法得到email地址
 * 3) parseToGetPicture:分析句法得到图片地址
 * 4) parseToGetInfo:分析句法得到tag计数
 */
public class Parsers {
    /**
     * some static fields: 一些正则表达式匹配串
     */
    static String urlPatternString = "href\\s*=\\s*\"(.*?)\"";
    static String EAPatternString = "[\\w!#$%&'*+=?^_`{|}~-]+(?:[\\w!#$%&'*+=?^_`{|}~-]+)" +
            "*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
    static String PicPatternString = "<img[\\s\\S]+?src=\"(.+?)\"";
    static String tagPatternString = "<([!a-zA-Z0-9]{1,16}?)[ >]";

    /**
     * Functions:
     */
    //parseToGetUrls: parse the text to get a url list.
    public static List<String> parseToGetUrls(String text){
        Pattern urlPattern = Pattern.compile(urlPatternString, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = urlPattern.matcher(text);
        List<String> list = new LinkedList<String>();
        while(urlMatcher.find()){
            String href = urlMatcher.group(1);
            href = href.replaceAll("\'","").replaceAll("\"","");
            if(href.startsWith("http:")){
                list.add(href);
            }
        }
        return list;
    }
    //parseToGetEmailAddr: parse the text to get a Email Address list.
    public static List<String> parseToGetEmailAddr(String text){
        Pattern EAPattern = Pattern.compile(EAPatternString, Pattern.CASE_INSENSITIVE);
        Matcher EAMatcher = EAPattern.matcher(text);
        List<String> list = new LinkedList<String>();
        while(EAMatcher.find()){
            String ea = EAMatcher.group(0);
            list.add(ea);
        }
        return list;
    }
    //parseToGetPicture: parse the text to get a picture list.
    public static List<String> parseToGetPicture(String text){
        Pattern PicPattern = Pattern.compile(PicPatternString, Pattern.CASE_INSENSITIVE);
        Matcher PicMatcher = PicPattern.matcher(text);
        List<String> list = new LinkedList<String>();
        while(PicMatcher.find()){
            String pic = PicMatcher.group(1);
            list.add(pic);
        }
        return list;
    }
    //parseToGetInfo: parse the text to get a info(Tag) list.
    public static List<String> parseToGetInfo(String text){
        Pattern tagPattern = Pattern.compile(tagPatternString, Pattern.CASE_INSENSITIVE);
        Matcher tagMatcher = tagPattern.matcher(text);
        List<String> list = new LinkedList<String>();
        while(tagMatcher.find()){
            String tag = tagMatcher.group(1);
            list.add(tag.trim());
        }
        return list;
    }
}
