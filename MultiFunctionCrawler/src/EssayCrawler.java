import Kits.UrlKit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.seleniumhq.jetty9.util.IO;

import javax.swing.*;
import java.util.*;

import static java.lang.Thread.sleep;

/**
 * EssayCrawler: This is the major part that implements the functions to crawl the essay.
 * It works following steps:
 * 1) check the input: whether it is a correct and effective url.
 * 2) parse the information and get new urls.
 * 3) print the result to the outputArea.
 *
 * @author Lin_Shu
 */
public class EssayCrawler {
    static final int MIN_CIT = 10; // if the citation number of an essay is below 10,abandon it
    static final int MAX_CHILDREN = 3; //from every essay,only the top 3 could enqueue
    static final int MAXN = 20; //maximum number of essays to be demonstrated
    ArrayList<Essay> esLst = new ArrayList<>();
    LinkedList<String> urlQue = new LinkedList<>();
    HashMap<String, Integer> essay2Id = new HashMap<>();
    boolean[][] graph = new boolean[MAXN][MAXN]; //a graph that describes the relationships between essays
    String stUrl; // the very url our Crawler start with

    //constructor
    EssayCrawler(String _stUrl) {
        esLst.clear();
        urlQue.clear();
        stUrl = _stUrl;
        essay2Id.clear();
    }

    // the major part of crawling
    void crawl(String stUrl) {
        //check the input url
        if (!UrlKit.checkUrl(stUrl)) {
            //System.out.println("Url input invalid! Please input another one.");
            CrawlerFrame.outputArea.setText("");
            CrawlerFrame.outputArea.append("Url input invalid! Please input another one.\n");
            CrawlerFrame.outputArea.paintImmediately(CrawlerFrame.outputArea.getBounds());
            CrawlerFrame.urlOrigin = null;
            JOptionPane.showMessageDialog(null, "Invalid Url!");
            return;
        }
        // To run selenium,you need to set the path of your chromedriver
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\ZZN\\IdeaProjects\\MultiFunctionCrawler\\chromedriver.exe");
        //System.out.println(System.getProperty("webdriver.chrome.driver"));
        WebDriver driver = new ChromeDriver();

        urlQue.push(stUrl);
        while (!urlQue.isEmpty()) {
            if (esLst.size() >= MAXN) {
                driver.close();
                //change some variables
                CrawlerFrame.lastOp = CrawlerFrame.Op.CrawlEssay;
                CrawlerFrame.lastUrl = stUrl;
                CrawlerFrame.haveSaved = false;
                JOptionPane.showMessageDialog(null, "Done!");
                return;
            }
            //randomly pop an element from the urlQue
            int idx = (int) (Math.random() * urlQue.size());
            System.out.println(idx);
            String frontUrl = urlQue.get(idx);
            urlQue.remove(idx);
            //visit that url using selenium
            driver.get(frontUrl);
            try {
                //basic information of the front essay
                String frontTitle = driver.findElement(By.className("main-info")).findElement(By.tagName("a")).getText();
                int frontCite = Integer.parseInt(driver.findElement(By.className("sc_cite_cont")).getText());
                Essay frontEssay = new Essay(frontTitle, frontUrl, frontCite);

                // check uniqueness
                if (checkName(frontTitle) == false)
                    continue;
                // explicit wait is used in the following steps
                WebDriverWait wait = new WebDriverWait(driver, 6);
                //wait for the page to load
                wait.until(ExpectedConditions.presenceOfElementLocated(By.className("cit_tab")));
                //click the button “引证文献” using selenium
                WebElement btn1 = driver.findElement(By.className("cit_tab"));
                btn1.click();
                // wait for the page to load

                wait.until(ExpectedConditions.presenceOfElementLocated(By.className("citation_lists")));
                // all the essays that cited the front essay is to be stored in citList
                ArrayList<WebElement> citList = new ArrayList<>();
                citList = (ArrayList<WebElement>) driver.findElement(By.className("citation_lists")).findElements(By.tagName("li"));

                int counter = 0;
                for (WebElement esy : citList) {
                    // esy.findElement(By.className("relative_title"))
                    int citNum = 0;
                    try {
                        citNum = getCiteNum(esy.findElement(By.className("sc_cited")));
                    } catch (Exception e) {
                        continue;
                    }
                    // we would dismiss the essay if it's seldom cited
                    // meanwhile, for variety, we only expand top-3 most cited essays
                    if (citNum < MIN_CIT || counter > MAX_CHILDREN) {
                        break;
                    }
                    counter += 1;
                    String essayTitle = esy.findElement(By.className("relative_title")).getText();
                    String essayUrl = esy.findElement(By.className("relative_title")).getAttribute("href");
                    frontEssay.citList.add(essayTitle);
                    urlQue.add(essayUrl);
                }
                // add front essay to esLst and show its information in outputArea
                essay2Id.put(frontTitle, essay2Id.size());
                esLst.add(frontEssay);
                showEssay(frontEssay, esLst.size());
            } catch (Exception e) {
                // if exception occurred with no essays in esLst
                // we deduce that the url is not appropriate
                // otherwise, we simply go to next essay
                if (esLst.size() == 0) {
                    CrawlerFrame.outputArea.setText("");
                    CrawlerFrame.outputArea.append("Please input an appropriate Url!\n");
                    CrawlerFrame.outputArea.paintImmediately(CrawlerFrame.outputArea.getBounds());
                    CrawlerFrame.urlOrigin = null;
                    driver.close();
                    //change some variables
                    CrawlerFrame.lastOp = CrawlerFrame.Op.CrawlEssay;
                    CrawlerFrame.lastUrl = stUrl;
                    CrawlerFrame.haveSaved = false;
                    JOptionPane.showMessageDialog(null, "Inappropriate Url!");
                    return;
                }
                continue;
            }
        }
        System.out.println("Finished!");
        driver.close();
        //change some variables
        CrawlerFrame.lastOp = CrawlerFrame.Op.CrawlEssay;
        CrawlerFrame.lastUrl = stUrl;
        CrawlerFrame.haveSaved = false;
        return;
    }

    // form a graph using esLst and essay2Id
    Graph getGraph() {
        int len = esLst.size(), edgeCnt = 0;
        for (int id = 0; id < len; id++) {
            Essay es = esLst.get(id);
            for (String title : es.citList) {
                if (essay2Id.containsKey(title)) {
                    graph[id][essay2Id.get(title)] = true;
                    edgeCnt += 1;
                }
            }
        }
        return new Graph(len, edgeCnt, esLst, graph);
    }

    // print the graph
    void printGraph() {
        for (int i = 0; i < MAXN; i++) {
            for (int j = 0; j < MAXN; j++) {
                System.out.printf("%d ", graph[i][j] ? 1 : 0);
            }
            System.out.println("");
        }
    }

    // show basic information of an essay
    void showEssay(Essay essay, int idx) {
        CrawlerFrame.outputArea.append(idx + "---------------------------\n");
        CrawlerFrame.outputArea.append("Title:" + essay.name + "\n");
        CrawlerFrame.outputArea.append("Url:" + essay.url + "\n");
        CrawlerFrame.outputArea.append("CitedCnt:" + essay.citedCnt + "\n");
        CrawlerFrame.outputArea.append("-----------------------------\n");
        CrawlerFrame.outputArea.paintImmediately(CrawlerFrame.outputArea.getBounds());
    }

    // helper function to check uniqueness
    boolean checkName(String name) {
        for (Essay essay : esLst) {
            if (name.equals(essay.name))
                return false;
        }
        return true;
    }

    // helper function to parse the web source code
    int getCiteNum(WebElement wbe) {
        return Integer.parseInt(wbe.findElement(By.className("sc_info_a")).getText());
    }
}
