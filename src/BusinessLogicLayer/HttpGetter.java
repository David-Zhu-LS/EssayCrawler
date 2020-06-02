package BusinessLogicLayer;

import BusinessLogicLayer.Interfaces.Getter;
import BusinessLogicLayer.Kits.HttpKit;
import BusinessLogicLayer.Kits.UrlKit;
import GUILayer.CrawlerFrame;

/**
 * HttpGetter: This is one of the key parts of Crawler.
 * It works following steps:
 * 1) Use MyHttpKit to make a http request Get.
 * 2) Print the response to the outputArea.
 */
public class HttpGetter implements Getter {
    @Override
    public void get(String initialUrl) {
        // start getting information and display information in time.
        CrawlerFrame.outputArea.setText("Getting...\n");
        CrawlerFrame.outputArea.paintImmediately(CrawlerFrame.outputArea.getBounds());
        //check the input url
        if (!UrlKit.checkUrl(initialUrl)) {
            //System.out.println("Url input invalid! Please input another one.");
            CrawlerFrame.outputArea.append("Url input invalid! Please input another one.");
            CrawlerFrame.outputArea.paintImmediately(CrawlerFrame.outputArea.getBounds());
            CrawlerFrame.urlOrigin = null;
            return;
        }
        CrawlerFrame.outputArea.append(HttpKit.get(initialUrl));
        CrawlerFrame.outputArea.append("Getting ends.");
        CrawlerFrame.outputArea.paintImmediately(CrawlerFrame.outputArea.getBounds());
        //change some variables
        CrawlerFrame.lastOp = CrawlerFrame.Op.HttpGet;
        CrawlerFrame.lastUrl = initialUrl;
        CrawlerFrame.haveSaved = false;
    }
}
