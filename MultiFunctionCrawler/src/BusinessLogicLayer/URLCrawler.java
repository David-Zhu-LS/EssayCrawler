package BusinessLogicLayer;

import BusinessLogicLayer.AuxiliaryFunctions.Download;
import BusinessLogicLayer.AuxiliaryFunctions.Parsers;
import BusinessLogicLayer.Interfaces.Crawler;
import BusinessLogicLayer.Kits.HttpKit;
import BusinessLogicLayer.Kits.UrlKit;
import GUILayer.CrawlerFrame;

import java.net.URL;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * StaticCrawler: This is one of the key parts of Crawler.
 * It works following steps:
 * 1) check the input: whether it is a correct and effective url.
 * 2) start to crawl. During the crawling, we use a thread to download, parse and add more urls to the queue.
 * After we create a thread and make it work, if the queue is empty, we wait for the thread to end, otherwise just
 * let it keep looping.
 * There are two variables that determines the crawler's result:
 * 1) MAX_LOOP_COUNT: how many times the loop runs
 * 2) EXPAND_MAX: how many new urls we want to get from a url at most.
 */
public class URLCrawler implements Crawler {
    final static int MAX_LOOP_COUNT = 10;
    final static int EXPAND_MAX = 5;
    @Override
    public void crawl(String initialUrl) {
        // start crawling and display information in time.
        CrawlerFrame.outputArea.setText("Crawling...\n");
        CrawlerFrame.outputArea.paintImmediately(CrawlerFrame.outputArea.getBounds());
        //check the input url
        if (!UrlKit.checkUrl(initialUrl)) {
            //System.out.println("Url input invalid! Please input another one.");
            CrawlerFrame.outputArea.append("Url input invalid! Please input another one.");
            CrawlerFrame.outputArea.paintImmediately(CrawlerFrame.outputArea.getBounds());
            CrawlerFrame.urlOrigin = null;
            return;
        }
        // Initialize the urls
        CrawlerFrame.urlOrigin = initialUrl;
        CrawlerFrame.urls.add(initialUrl);
        CrawlerFrame.urlsHaveBeenFound.put(initialUrl, 0);
        int counter = 0;
        // Crawl from the urls in the queue.
        while (!CrawlerFrame.urls.isEmpty()) {
            String url = CrawlerFrame.urls.poll();
            //System.out.println(url);
            Integer layer0 = CrawlerFrame.urlsHaveBeenFound.get(url);
            // use a latch in case it is necessary.
            CountDownLatch latch = new CountDownLatch(1);
            final int layer = layer0 + 1;
            CrawlerFrame.outputArea.append("layer " + layer + " " + url + "\n");
            CrawlerFrame.outputArea.paintImmediately(CrawlerFrame.outputArea.getBounds());
            Thread downloadThread = new Thread(() -> {
                try {
                    //Download the content from the url.
                    String content;
                    if (CrawlerFrame.isDynamic) content = HttpKit.get(url);
                    else content = Download.download(new URL(url), "UTF-8");
                    //Parse the content and get what we want. Store it in a list and then return.
                    List<String> moreUrls = Parsers.parseToGetUrls(content);
                    //If the url has been found, ignore it. Otherwise, add it to the queue and map.
                    int cnt = 0;
                    for (String str : moreUrls) {
                        if (!CrawlerFrame.urlsHaveBeenFound.containsKey(str)) {
                            CrawlerFrame.urls.add(str);
                            CrawlerFrame.urlsHaveBeenFound.put(str, layer);
                            cnt++;
                        }
                        if (cnt >= EXPAND_MAX) break;
                    }
                    latch.countDown();
                }
                //Deal with the exceptions.
                catch (Exception ex) {
                    System.out.println("An exception occurs in downloading thread.");
                    ex.printStackTrace();
                    System.out.println(ex.getMessage());
                }

            });
            downloadThread.start();
            counter++;
            if (counter >= MAX_LOOP_COUNT) {
                break;
            }
            try {
                Thread.sleep(4000);
            } catch (InterruptedException ie) {
            }
            if (CrawlerFrame.urls.isEmpty()) {
                try {
                    latch.await();
                } catch (InterruptedException iex) {
                    System.out.println("An exception occurs in latch.");
                    iex.printStackTrace();
                    System.out.println(iex.getMessage());
                }
            }
        }
        CrawlerFrame.outputArea.append("\nHasn't expanded:\n");
        CrawlerFrame.outputArea.paintImmediately(CrawlerFrame.outputArea.getBounds());
        //print other urls we have got.
        Object[] urlsArray = CrawlerFrame.urls.toArray();
        for (Object obj : urlsArray) {
            String url = (String) obj;
            //System.out.println(url);
            Integer layer0 = CrawlerFrame.urlsHaveBeenFound.get(url);
            final int layer = layer0 + 1;
            CrawlerFrame.outputArea.append("layer " + layer + " " + url + "\n");
            CrawlerFrame.outputArea.paintImmediately(CrawlerFrame.outputArea.getBounds());
        }
        CrawlerFrame.outputArea.append("Crawling ends.");
        CrawlerFrame.outputArea.paintImmediately(CrawlerFrame.outputArea.getBounds());
        //change some variables
        CrawlerFrame.lastOp = CrawlerFrame.Op.Crawl;
        CrawlerFrame.lastUrl = initialUrl;
        CrawlerFrame.haveSaved = false;
    }
}
