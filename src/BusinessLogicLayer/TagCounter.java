package BusinessLogicLayer;

import BusinessLogicLayer.AuxiliaryFunctions.Download;
import BusinessLogicLayer.AuxiliaryFunctions.Parsers;
import BusinessLogicLayer.Interfaces.Getter;
import BusinessLogicLayer.Kits.HttpKit;
import BusinessLogicLayer.Kits.UrlKit;
import GUILayer.CrawlerFrame;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * TagCounter: This is one of the key part of Crawler.
 * It works following steps:
 * 1) check the input: whether it is a correct and effective url.
 * 2) download the information.
 * 3) parse the information we just downloaded to count the tags.
 * 4) print the result to the outputArea.
 */
public class TagCounter implements Getter {
    @Override
    public void get(String initialUrl) {
        // start getting information and display information in time.
        CrawlerFrame.outputArea.setText("Getting tags...\n");
        CrawlerFrame.outputArea.paintImmediately(CrawlerFrame.outputArea.getBounds());
        //check the input url
        if (!UrlKit.checkUrl(initialUrl)) {
            //System.out.println("Url input invalid! Please input another one.");
            CrawlerFrame.outputArea.append("Url input invalid! Please input another one.");
            CrawlerFrame.outputArea.paintImmediately(CrawlerFrame.outputArea.getBounds());
            CrawlerFrame.urlOrigin = null;
            return;
        }
        CountDownLatch latch = new CountDownLatch(1);
        Thread gettingTagThread = new Thread(() -> {
            try {
                //Download the content from the url.
                String content;
                if (CrawlerFrame.isDynamic) content = HttpKit.get(initialUrl);
                else content = Download.download(new URL(initialUrl), "UTF-8");
                //Parse the content and get what we want. Store it in a list and then return.
                List<String> tags = Parsers.parseToGetInfo(content);
                //print the result.
                ConcurrentHashMap<String, Integer> tagsCount = new ConcurrentHashMap<String, Integer>();
                for (String tag : tags) {
                    if (!tagsCount.containsKey(tag)) {
                        tagsCount.put(tag, 1);
                    } else {
                        tagsCount.put(tag, tagsCount.get(tag) + 1);
                    }
                }
                List<Map.Entry<String, Integer>> ml = new ArrayList<Map.Entry<String, Integer>>(tagsCount.entrySet());
                ml.sort((e1, e2) -> {
                    return e1.getKey().compareTo(e2.getKey());
                });
                for (Map.Entry<String, Integer> tag : ml) {
                    CrawlerFrame.outputArea.append(tag.getKey() + " " + tag.getValue() + "\n");
                    CrawlerFrame.outputArea.paintImmediately(CrawlerFrame.outputArea.getBounds());
                }
                latch.countDown();
            }
            //Deal with the exceptions.
            catch (Exception ex) {
                System.out.println("An exception occurs in gettingEmailAddrThread.");
                ex.printStackTrace();
                System.out.println(ex.getMessage());
            }

        });
        gettingTagThread.start();
        try {
            latch.await();
        } catch (InterruptedException iex) {
            System.out.println("An exception occurs in latch.");
            iex.printStackTrace();
            System.out.println(iex.getMessage());
        }
        CrawlerFrame.outputArea.append("Getting Tags ends.");
        CrawlerFrame.outputArea.paintImmediately(CrawlerFrame.outputArea.getBounds());
        //change some variables
        CrawlerFrame.lastOp = CrawlerFrame.Op.TagsCount;
        CrawlerFrame.lastUrl = initialUrl;
        CrawlerFrame.haveSaved = false;
    }

}
