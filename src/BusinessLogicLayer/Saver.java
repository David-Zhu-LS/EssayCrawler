package BusinessLogicLayer;

import GUILayer.CrawlerFrame;

import javax.swing.*;
import java.io.*;

/**
 * Saver: This is one of the key parts of Crawler.
 * It works following steps:
 * 1) If the lastOp is NewCrawl or KeepCrawl, save all the urls in the urlsHaveBeenFound.
 * 2) Otherwise, save all the things in the outputArea to a file named by the lastOp and lastUrl.
 * The default save path is Desktop.
 * The default filename is : modifiedUrl + lastOp.toString() + ".txt";
 */
public class Saver {
    static String defaultPath = "./saved/CrawlerResult";

    public static void save() {
        //If it has been saved, return;
        if (CrawlerFrame.haveSaved) return;
        if (CrawlerFrame.lastOp == CrawlerFrame.Op.None || CrawlerFrame.lastUrl == null) return;
        //Get the content in the outputArea.
        String outputOnScreen = CrawlerFrame.outputArea.getText();
        //The default save path is Desktop.
        //FileSystemView fsv = FileSystemView.getFileSystemView();
        //File com = fsv.getHomeDirectory();// com.getPath() returns the path of Desktop.
        //Modify the lastUrl to get a correct filename.
        String modifiedUrl;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CrawlerFrame.lastUrl.length(); ++i) {
            char ch = CrawlerFrame.lastUrl.charAt(i);
            if (ch == '?' || ch == '*' || ch == '/' || ch == '\\' || ch == ':') sb.append('.');
            else sb.append(ch);
        }
        modifiedUrl = sb.toString();
        //if lastOp is Crawl
        if (CrawlerFrame.lastOp == CrawlerFrame.Op.Crawl) {
            try {
                String outName = defaultPath + File.separator + modifiedUrl + "Crawled" +
                        (CrawlerFrame.isDynamic ? "Dynamic" : "Static") + ".txt";
                File fout = new File(outName);
                Object[] a = CrawlerFrame.urlsHaveBeenFound.keySet().toArray();
                PrintWriter out = new PrintWriter(new FileWriter(fout));
                for (Object obj : a) {
                    out.println((String) obj);
                }
                out.close();
                JOptionPane.showMessageDialog(null, "Saved successfully!");
            } catch (FileNotFoundException e1) {
                System.err.println("File not found!");
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } else {
            try {
                String outName = defaultPath + File.separator + modifiedUrl + CrawlerFrame.lastOp.toString() +
                        (CrawlerFrame.isDynamic ? "Dynamic" : "Static") + ".txt";
                File fout = new File(outName);
                BufferedReader in = new BufferedReader(new StringReader(outputOnScreen));
                PrintWriter out = new PrintWriter(new FileWriter(fout));
                String s = in.readLine();
                while (s != null) {
                    out.println(s);
                    s = in.readLine();
                }
                in.close();
                out.close();
                JOptionPane.showMessageDialog(null, "Saved successfully!");
            } catch (FileNotFoundException e1) {
                System.err.println("File not found!");
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        //change some variables
        CrawlerFrame.haveSaved = true;
    }

    public static void essaySaver() {
        //If it has been saved, return;
        if (CrawlerFrame.haveSaved) return;
        if (CrawlerFrame.lastOp != CrawlerFrame.Op.CrawlEssay) return;
        String path = defaultPath + File.separator + "Essay.txt";
        try {
            File fout = new File(path);
            BufferedReader in = new BufferedReader(new StringReader(CrawlerFrame.outputArea.getText()));
            PrintWriter out = new PrintWriter(new FileWriter(fout));
            String s = in.readLine();
            while (s != null) {
                out.println(s);
                s = in.readLine();
            }
            in.close();
            out.close();
            JOptionPane.showMessageDialog(null, "Saved successfully!");
        } catch (FileNotFoundException e1) {
            System.err.println("File not found!");
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        CrawlerFrame.haveSaved = true;
    }
}
