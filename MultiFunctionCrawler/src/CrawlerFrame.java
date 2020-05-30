import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.painter.border.StandardBorderPainter;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;
import org.pushingpixels.substance.api.skin.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static javax.print.attribute.standard.MediaSizeName.C;

/**
 * CrawlerFrame: This is the frame of Crawler.
 * The key function is to crawl the essays and to convert them into a graph where their citing and cited relations
 * are shown.
 * Additionally, It has 8 functions in function menu:
 * 1)NewCrawl: Start a new crawling from the given url in the inputArea and then get to other ones.
 *  We use ConcurrentLinkedQueue to store the urls we have found but haven't expanded yet.
 *  We use ConcurrentHashMap to store all the urls that we have got,
 *  for ConcurrentHashMap is thread-safe and it is fast and convenient to check whether a url has been found.
 * 2)KeepCrawl: Keep crawling.
 * 3)TagsCount: Count the tags that have been used.
 * 4)HttpGet: Get Http connection and make the GET request. Print the response.
 * 5)GetEmailAddr: Get email addresses.
 * 6)GetPicture: Get the resource of pictures.(Not download.)
 * 7)Save: Save the urls we have got or other content in the outputArea.
 * 8)Clear: Clear the former information.
 * @auther PKU_zzn
 */
public class CrawlerFrame extends JFrame {
    //Main function which is just an entrance and can be modified or removed if necessary.
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new SubstanceLookAndFeel(new GraphiteAquaSkin()) {
                @Override
                public String getDescription() {
                    return super.getDescription();
                }
            });
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            //SubstanceLookAndFeel.setCurrentTheme(new SubstanceTerracottaTheme());
            SubstanceLookAndFeel.setSkin(new GraphiteAquaSkin());
            //SubstanceLookAndFeel.setCurrentButtonShaper(new ClassicButtonShaper());
            //SubstanceLookAndFeel.setCurrentWatermark(new SubstanceBubblesWatermark());
            //SubstanceLookAndFeel.setCurrentBorderPainter(new StandardBorderPainter());
            //SubstanceLookAndFeel.setCurrentGradientPainter(new StandardGradientPainter());
            //SubstanceLookAndFeel.setCurrentTitlePainter(new FlatTitePainter());
            SwingUtilities.invokeLater(()->{CrawlerFrame myURLCrawler = new CrawlerFrame();});
        } catch (Exception e) {
            System.err.println("Something went wrong!");
        }
        //CrawlerFrame myURLCrawler = new CrawlerFrame();
    }
    /**
     * The followings are some fields of CrawlerFrame.
     * 1) ConcurrentHashMap<String, Integer> urlsHaveBeenFound: the ConcurrentHashMap we used to store all the urls that
     * we have got. It is thread-safe and fast and convenient to check whether a website has been found.
     * 2) ConcurrentLinkedQueue<String> urls: the ConcurrentLinkedQueue we used to temporarily store the urls we want to
     * expand.
     * 3) urlOrigin: Record the original url.
     * 4) lastUrl: Record the url of last operation.
     * 5) lastOp: Record the last operation.
     * 6) haveSaved: whether the content we want to save has been saved.
     */
    public static JFrame mainFrame = new JFrame();
    public static ConcurrentHashMap<String, Integer> urlsHaveBeenFound;
    public static ConcurrentLinkedQueue<String> urls;
    public static String urlOrigin;
    public static String lastUrl;
    public static Op lastOp;
    public static boolean haveSaved;
    public static enum Op{None,Crawl,TagsCount,HttpGet,GetEmailAddr,GetPictures,Save,Clear};

    /**
     * Constructors:
     * 1) Initialize fields: urlsHaveBeenFound, urls, urlOrigin, lastUrl, lastOp and haveSaved
     * 2) Initialize the frame: invoke method initializeFrame()
     */
    public CrawlerFrame(){
        urlsHaveBeenFound = new ConcurrentHashMap<String, Integer>();
        urls = new ConcurrentLinkedQueue<String>();
        urlOrigin = null;
        lastUrl = null;
        lastOp = Op.None;
        haveSaved = false;
        initializeFrame();
    }
    /**
     * The followings are several fields and methods used to initialize the frame.
     * inputUrl: the original url which is "Input the start url here." by default
     * outputUrl: a JTextArea used to display the result
     * btnStart: start a new crawl from the input url
     * btnKeep: keep the last crawling
     * btnGetInfo: count the tags
     * btnGetEmail: get the emails from the url
     * btnGetPicture: get the resource of pictures from the url
     * btnSave: save the content
     * btnHttpGet: make http Get request
     * btnClear: clear the former information
     */
    public static JTextArea inputArea;
    public static JTextArea outputArea;
    public static JRadioButton rbtnDynamic, rbtnStatic;
    public static boolean isDynamic = false;
    public static void initializeFrame(){
        //create objects.
        /*JButton btnStart = new JButton();
        JButton btnKeep = new JButton();
        JButton btnGetInfo = new JButton();
        JButton btnGetEmail = new JButton();
        JButton btnGetPicture = new JButton();
        JButton btnSave = new JButton();
        JButton btnHttpGet = new JButton();
        JButton btnClear = new JButton();
        rbtnDynamic = new JRadioButton("Dynamic");
        rbtnStatic = new JRadioButton("Static");*/
        JButton btnCrawlEssay = new JButton();
        JButton btnSaveEssay = new JButton();
        JButton btnGenerateGraph = new JButton();
        JButton btnSaveGraph = new JButton();
        JLabel lblInfo = new JLabel();
        inputArea = new JTextArea("Input the start url here.",1,60);
        outputArea = new JTextArea("Results will be showed here.",30,20);
        //layout design.
        mainFrame.setLayout(null);
        mainFrame.setSize(800,636);
        mainFrame.setTitle("Crawler");
        /*setAndAddButton(btnStart,"NewCrawl",Color.lightGray,18,72,116,40);
        setAndAddButton(btnKeep,"KeepCrawl",Color.lightGray,140,72,116,40);
        setAndAddButton(btnGetInfo,"TagsCount",Color.lightGray,262,72,116,40);
        setAndAddButton(btnHttpGet,"HttpGet",Color.lightGray,384,72,116,40);
        setAndAddButton(btnGetEmail,"GetEmailAddr",Color.lightGray,18,116,116,40);
        setAndAddButton(btnGetPicture,"GetPicture",Color.lightGray,140,116,116,40);
        setAndAddButton(btnSave,"Save",Color.lightGray,262,116,116,40);
        setAndAddButton(btnClear,"Clear",Color.lightGray,384,116,116,40);*/
        setAndAddButton(btnCrawlEssay,"CrawlEssay",Color.lightGray,520,18,116,40);
        setAndAddButton(btnSaveEssay,"SaveEssay",Color.lightGray,650,18,116,40);
        setAndAddButton(btnGenerateGraph,"MakeGraph",Color.lightGray,520,72,116,40);
        setAndAddButton(btnSaveGraph,"SaveGraph",Color.lightGray,650,72,116,40);
        setAndAdd(lblInfo,520,126,250,300);
        lblInfo.setSize(250,150);
        String infoContent = "<html>Click a button to start.<br/>More information could be shown here.<br/>" +
                "How to use the program?<br/>(1) Input the url you want to start crawling from in the upper textField" +
                "<br/>(2) Click the button.<html>";
        lblInfo.setText(infoContent);
        setAndAddTextArea(inputArea,18,18,484,30,false,18);
        setAndAddTextArea(outputArea,18,72,484,500,true,15);
        /*setAndAdd(rbtnDynamic,50,100,100,50);
        setAndAdd(rbtnStatic,150,100,100,50);*/
        //set button ActionListener.
        setMenu();
        /*btnStart.addActionListener((e)->{
            String input = inputArea.getText();
            urls.clear();
            urlsHaveBeenFound.clear();
            Crawler.crawl(input);
        });
        btnKeep.addActionListener((e)->{
            if(urlOrigin != null){
                Crawler.crawl(urlOrigin);
            }
            else{
                String input = inputArea.getText();
                Crawler.crawl(input);
            }
        });
        btnGetInfo.addActionListener((e)->{
            String input = inputArea.getText();
            TagCounter.getTagCount(input);
        });
        btnGetEmail.addActionListener((e)->{
            String input = inputArea.getText();
            EmailGetter.getEmailAddr(input);
        });
        btnGetPicture.addActionListener((e)->{
            String input = inputArea.getText();
            PictureGetter.getPicture(input);
        });
        btnSave.addActionListener((e)->{
            Saver.save();
        });
        btnHttpGet.addActionListener((e)->{
            String input = inputArea.getText();
            HttpGetter.httpGet(input);
        });
        btnClear.addActionListener((e)->{
            urlOrigin = null;
            urls.clear();
            urlsHaveBeenFound.clear();
            outputArea.setText("");
            outputArea.paintImmediately(outputArea.getBounds());
        });
        rbtnDynamic.addActionListener(e->{
            if(rbtnStatic.isSelected()){
                rbtnStatic.setSelected(false);
            }
            rbtnDynamic.setSelected(true);
            isDynamic = true;
        });
        rbtnStatic.addActionListener(e->{
            if(rbtnDynamic.isSelected()){
                rbtnDynamic.setSelected(false);
            }
            rbtnStatic.setSelected(true);
            isDynamic = false;
        });
        // select false at the beginning.
        rbtnStatic.setSelected(true);
        isDynamic = false;*/
        //set JTextArea.
        outputArea.setLineWrap(true);        // Activate LineWrap function(start a new line automatically)
        outputArea.setWrapStyleWord(true);   // Activate WrapStyleWord function(start a line without break a word)
        JScrollPane scroller = new JScrollPane();   // set the scrollbar
        scroller.setBounds(18,72,484,500);
        mainFrame.getContentPane().add(scroller);
        scroller.setViewportView(outputArea);
        scroller.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);//horizontal one appears as needed
        scroller.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);//vertical one always appears
        //set the close button
        mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainFrame.setVisible( true );
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
    }
    // Set and add the button to the frame.
    public static void setAndAddButton(JButton button, String name, Color color, int posx, int posy, int width, int height){
        button.setText(name);
        button.setBackground(color);
        button.setBounds(posx,posy,width,height);
        // Text displayed at the center
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.CENTER);
        mainFrame.getContentPane().add(button);
    }
    // Set and add the textArea to the frame.
    public static void setAndAddTextArea(JTextArea textArea, int posx, int posy, int width, int height,
                                         boolean inScroller, int fontSize){
        textArea.setFont(new Font("Dialog",Font.PLAIN,fontSize));
        textArea.setBounds(posx,posy,width,height);
        if(!inScroller)// if the TextArea doesn't have a scrollbar, add it to the frame.
            mainFrame.getContentPane().add(textArea);
    }
    // Set and add the component to the frame.
    public static void setAndAdd(JComponent component, int posx, int posy, int width, int height){
        component.setFont(new Font("Dialog",Font.PLAIN,15));
        component.setBounds(posx,posy,width,height);
        mainFrame.getLayeredPane().add(component);
    }
    public static void setMenu(){
        MenuBar mb = new MenuBar();
        // Create a menuBar
        mainFrame.setMenuBar(mb);
        Menu mFunction = new Menu("Functions");
        Menu mHelp = new Menu("Help");
        Menu mMode = new Menu("Mode");
        Menu mTheme = new Menu("Theme");
        // Add menu
        mb.add(mFunction);
        mb.add(mMode);
        mb.add(mTheme);
        mb.setHelpMenu(mHelp);
        // Create and add some menuItems
        setThemeMenu(mTheme);
        MenuItem miNewCrawl = new MenuItem("NewUrlCrawl");
        MenuItem miKeepCrawl = new MenuItem("KeepUrlCrawl");
        MenuItem miGetEA = new MenuItem("GetEA");
        MenuItem miGetPic = new MenuItem("GetPic");
        MenuItem miCountTag = new MenuItem("CountTag");
        MenuItem miHttpGet = new MenuItem("HttpGet");
        MenuItem miClear = new MenuItem("Clear");
        MenuItem miSave = new MenuItem("Save");
        MenuItem miQuit = new MenuItem("Quit");
        MenuItem miDynamic = new MenuItem("Dynamic");
        MenuItem miStatic = new MenuItem("√  Static");
        mFunction.add(miNewCrawl);
        mFunction.add(miKeepCrawl);
        mFunction.addSeparator();
        mFunction.add(miGetEA);
        mFunction.add(miGetPic);
        mFunction.add(miHttpGet);
        mFunction.add(miCountTag);
        mFunction.addSeparator();
        mFunction.add(miClear);
        mFunction.add(miSave);
        mFunction.add(miQuit);
        mMode.add(miDynamic);
        mMode.add(miStatic);
        // Add actionListener
        miNewCrawl.addActionListener((e)->{
            String input = inputArea.getText();
            urls.clear();
            urlsHaveBeenFound.clear();
            Crawler.crawl(input);
        });
        miKeepCrawl.addActionListener((e)->{
            if(urlOrigin != null){
                Crawler.crawl(urlOrigin);
            }
            else{
                String input = inputArea.getText();
                Crawler.crawl(input);
            }
        });
        miCountTag.addActionListener((e)->{
            String input = inputArea.getText();
            TagCounter.getTagCount(input);
        });
        miGetEA.addActionListener((e)->{
            String input = inputArea.getText();
            EmailGetter.getEmailAddr(input);
        });
        miGetPic.addActionListener((e)->{
            String input = inputArea.getText();
            PictureGetter.getPicture(input);
        });
        miSave.addActionListener((e)->{
            Saver.save();
        });
        miQuit.addActionListener((e)->{
            System.exit(0);
        });
        miHttpGet.addActionListener((e)->{
            String input = inputArea.getText();
            HttpGetter.httpGet(input);
        });
        miClear.addActionListener((e)->{
            urlOrigin = null;
            urls.clear();
            urlsHaveBeenFound.clear();
            outputArea.setText("");
            outputArea.paintImmediately(outputArea.getBounds());
        });

        isDynamic = false;
        miDynamic.addActionListener(e->{
            miDynamic.setLabel("√  Dynamic");
            miStatic.setLabel("Static");
            isDynamic = true;
        });
        miStatic.addActionListener(e->{
            miStatic.setLabel("√  Static");
            miDynamic.setLabel("Dynamic");
            isDynamic = false;
        });
    }
    // set the Theme menu
    public static int used = 3;
    public static void setThemeMenu(Menu m){
        String[] strs = new String[]{"Autumn","DustCoffee","EmeraldDusk","GraphiteAqua",
                "NebulaBrickWall","OfficeBlue","OfficsBlack","Twilight"};
        MenuItem[] miArray = new MenuItem[]{new MenuItem(strs[0]),new MenuItem(strs[1]),
                new MenuItem(strs[2]),new MenuItem(strs[3]),new MenuItem(strs[4]),
                new MenuItem(strs[5]),new MenuItem(strs[6]),new MenuItem(strs[7])};
        for(MenuItem mi : miArray){
            m.add(mi);
        }
        miArray[used].setEnabled(false);
        SubstanceSkin[]  skins = new SubstanceSkin[]{new AutumnSkin(),new DustCoffeeSkin(),new EmeraldDuskSkin(),
            new GraphiteAquaSkin(), new NebulaBrickWallSkin(), new OfficeBlue2007Skin(), new OfficeBlack2007Skin(),
            new TwilightSkin()};
        for(int i = 0; i < strs.length; ++i){
            int finalI = i;
            miArray[i].addActionListener((e)->{
                try {
                    SubstanceLookAndFeel.setSkin(skins[finalI]);
                    miArray[used].setEnabled(true);
                    miArray[finalI].setEnabled(false);
                    used = finalI;

                } catch (Exception ex) {
                    System.err.println("Something went wrong!");
                }
            });
        }
    }
   /* // Jlabel starts a new line automatically.
    static void JlabelSetText(JLabel jLabel, String longString)
            throws InterruptedException {
        StringBuilder builder = new StringBuilder("<html>");
        char[] chars = longString.toCharArray();
        FontMetrics fontMetrics = jLabel.getFontMetrics(jLabel.getFont());
        int start = 0;
        int len = 0;
        while (start + len < longString.length()) {
            while (true) {
                len++;
                if (start + len > longString.length())break;
                if (fontMetrics.charsWidth(chars, start, len)
                        > jLabel.getWidth()) {
                    break;
                }
            }
            builder.append(chars, start, len-1).append("<br/>");
            start = start + len - 1;
            len = 0;
        }
        builder.append(chars, start, longString.length()-start);
        builder.append("</html>");
        jLabel.setText(builder.toString());
    }*/
}

