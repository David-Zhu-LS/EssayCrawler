import AuxiliaryFunctions.Download;
import AuxiliaryFunctions.Parsers;
import Kits.HttpKit;
import Kits.UrlKit;

import java.net.URL;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * EmailGetter: This is one of the key parts of Crawler.
 * It works following steps:
 * 1) check the input: whether it is a correct and effective url.
 * 2) download the information.
 * 3) parse the information we just downloaded to get email addresses.
 * 4) print the result to the outputArea.
 */
public class EmailGetter {
    public static void getEmailAddr(String initialUrl) {
        // start getting email addresses and display information in time.
        CrawlerFrame.outputArea.setText("Getting email addresses...\n");
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
        Thread gettingEmailAddrThread = new Thread(() -> {
            try {
                //Download the content from the url.
                String content;
                if (CrawlerFrame.isDynamic) content = HttpKit.get(initialUrl);
                else content = Download.download(new URL(initialUrl), "UTF-8");
                //Parse the content and get what we want. Store it in a list and then return.
                List<String> emailAddr = Parsers.parseToGetEmailAddr(content);
                //print the result.
                for (String str : emailAddr) {
                    CrawlerFrame.outputArea.append(str + "\n");
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
        gettingEmailAddrThread.start();
        try {
            latch.await();
        } catch (InterruptedException iex) {
            System.out.println("An exception occurs in latch.");
            iex.printStackTrace();
            System.out.println(iex.getMessage());
        }
        CrawlerFrame.outputArea.append("Getting email addresses ends.");
        CrawlerFrame.outputArea.paintImmediately(CrawlerFrame.outputArea.getBounds());
        //change some variables
        CrawlerFrame.lastOp = CrawlerFrame.Op.GetEmailAddr;
        CrawlerFrame.lastUrl = initialUrl;
        CrawlerFrame.haveSaved = false;
    }

}
