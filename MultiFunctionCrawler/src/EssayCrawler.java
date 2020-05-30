import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.seleniumhq.jetty9.util.IO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static java.lang.Thread.activeCount;
import static java.lang.Thread.sleep;


public class EssayCrawler {
    ArrayList<Essay> esLst = new ArrayList<>(); //线性表，每个元素是一篇论文
    LinkedList<String> urlQue = new LinkedList<>();//队列，用于存放待扩展的url
    HashMap<String,Integer>essay2Id= new HashMap<>();//论文名到编号(Id)的哈希表
    static final int MAXN = 20; //论文数
    int [][]graph= new int[MAXN][MAXN]; //图
    String stUrl;

    EssayCrawler(String _stUrl) {
        esLst.clear();
        urlQue.clear();
        stUrl = _stUrl;
        essay2Id.clear();
        crawl(stUrl);
        getGraph();
        // 输出建好的图
        for (int i = 0; i < MAXN; i++) {
            for (int j = 0; j < MAXN; j++) {
                System.out.printf("%d ",graph[i][j]);
            }
            System.out.println("");
        }
    }


    void crawl(String stUrl) {
        // 设置路径
        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver_win32\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        urlQue.push(stUrl);
        while (!urlQue.isEmpty()) {
            if (esLst.size() >= MAXN) {
                return;
            }
            //从urlQue中随机取出一个元素
            int idx = (int)(Math.random()*urlQue.size());
            System.out.println(idx);
            String frontUrl = urlQue.get(idx);
            urlQue.remove(idx);

            driver.get(frontUrl);
            try{
                //获取当前待扩展的论文的信息
                String frontTitle = driver.findElement(By.className("main-info")).findElement(By.tagName("a")).getText();
                int frontCite = Integer.parseInt(driver.findElement(By.className("sc_cite_cont")).getText());
                Essay frontEssay = new Essay(frontTitle, frontUrl, frontCite);

                // 检查该论文是否已经被扩展过
                if(checkName(frontTitle)==false)
                    continue;
                WebDriverWait wait = new WebDriverWait(driver, 20);


                //等待页面加载
                wait.until(ExpectedConditions.presenceOfElementLocated(By.className("cit_tab")));
                //selenium上模拟点击“引证文献”
                WebElement btn1 = driver.findElement(By.className("cit_tab"));
                btn1.click();
//            Actions action = new Actions(driver);
//            action.click(btn1).perform();
                // 等待页面加载

                wait.until(ExpectedConditions.presenceOfElementLocated(By.className("citation_lists")));

                ArrayList<WebElement> citList = new ArrayList<>();
                citList = (ArrayList<WebElement>) driver.findElement(By.className("citation_lists")).findElements(By.tagName("li"));

                int counter = 0;
                for (WebElement esy : citList) {
//            esy.findElement(By.className("relative_title"))
                    int citNum = 0;
                    try{
                        citNum =  getCiteNum(esy.findElement(By.className("sc_cited")));
                    }catch (Exception e){
                        continue;
                    }
                    if (citNum < 10 || counter > 3) {
                        break;
                    }
                    counter += 1;
                    String essayTitle = esy.findElement(By.className("relative_title")).getText();
                    String essayUrl = esy.findElement(By.className("relative_title")).getAttribute("href");
                    frontEssay.citList.add(essayTitle);
                    urlQue.add(essayUrl);
                }
                essay2Id.put(frontTitle,essay2Id.size());
                esLst.add(frontEssay);
            }catch (Exception e){
                continue;
            }
        }
        System.out.println("Bye~");
//        driver.close();
    }
    void getGraph(){
        int len = esLst.size();
        for (int id = 0; id < len; id++) {
            Essay es = esLst.get(id);
            for(String title:es.citList){
                if(essay2Id.containsKey(title)) {
                    graph[id][essay2Id.get(title)] = 1;
                }
            }
        }

    }

    boolean checkName(String name){
        for(Essay essay:esLst){
            if(name.equals(essay.name))
                return false;
        }
        return true;
    }
    int getCiteNum(WebElement wbe) {
        return Integer.parseInt(wbe.findElement(By.className("sc_info_a")).getText());
    }
}
