# MultiFunctionCrawler

* **一.程序功能与特色**

​    该程序是一个多功能的爬虫，主要用于百度文库论文的爬取并生成其引用关系图。
​    使用者只需在input文本框中输入初始百度文库中论文的URL，然后点击CrawlEssay按钮，即可运行爬虫。在爬行过程中，爬到的文献会展示在下方的输出文本框中。爬行结束后，点击SaveEssay即可将爬到的论文相关信息保存至默认文件夹saved的一个文件中。点击MakeGraph按钮即可生成文献关系图，在弹窗中展示出来。之后再点击SaveGraph即可将图片保存至默认文件夹saved，点击Quit则不保存文件。

CrawlEssay：从输入URL开始爬取百度文库论文。

SaveEssay：保存爬取到的论文结果。

MakeGraph：可视化，产生爬取到的论文的引用关系图，并用颜色标记其引用量。

SaveGraph：保存产生的论文引用关系图。

​    除此之外，程序还有一些辅助功能，放在Functions菜单中：

NewUrlCrawl：在input文本框中输入初始URL，点击NewUrlCrawl即可进行网页爬取。每次爬取一定量的网页。

KeepUrlCrawl：继续当前爬取，增加爬取网页的数量。如果中途转而使用了其他功能，则不会记录以往的爬行结果。

CountTag：计数网页中的tag。

GetEA：爬取该网页下的email地址。

GetPic：爬取该网页中的图片地址，以便后续下载，这里没有进一步拓展图片下载功能。

HttpGet：以GET的方式请求获取URL内容。

Save：保存以上功能的结果到一个默认文件夹中。文件的名字由处理过的URL+功能名+模式名构成。

Clear：清空输出文本框中的内容。

Quit：退出程序。

​	另外，该程序还添加了模式选取功能，放在Mode菜单中。选定Dynamic，则是从GET获得的URL内容中进行爬取；选定Static，则是直接Download网页内容。这一部分还可以进一步扩展。

​	最后，我们还为该程序添加了一个主题更换的功能，放在菜单Theme中。点击相应主题即可更换。

* **二.程序结构**

  Project的结构大致如下：

  MultiFunctionCrawler

  ​	.idea

  ​	lib

  ​	out

  ​	saved

  ​	src

  ​		AuxiliaryFunctions

  ​		Kits

  其中lib是引用的外部库，saved是程序保存文件的默认文件夹，src是代码的主要部分。

  * lib

    lib中包含的外部库主要是三个部分：

    ①laf-widget-7.2.1.jar, substance-6.2.jar, trident.jar：主要用于图形界面的美化和主题的变动。

    ②selenium-server-standalone：主要用于解析网页中JavaScript的动态生成部分，保证论文的爬取顺利进行。

    ③Graphviz：主要用于图的可视化，生成论文引用关系图。

  * saved

    saved是程序保存文件的默认文件夹。

  * src

    * AuxiliaryFunctions：包括两个类

      Download：下载网页

      Parsers：内容解析，包括parseToGetUrls，parseToGetEmailAddr，parseToGetPicture，parseToGetInfo等方法，以及相应正则表达式字段

    * Kits：包括两个类

      HttpKit：用于以Get方式请求获取URL的内容的工具类

      UrlKit：用于URL可行性的检验等的工具类

    * 主要功能类

      和主要功能相关的类直接放在src中：

      CrawlerFrame：程序框架

      Essay&EssayCrawler：论文类和论文爬取类

      Crawler：网页爬取类

      EmailGetter：email：地址爬取类

      PictureGetter：图片爬取类

      HttpGetter：以Get方式请求获取URL内容类

      TagCounter：Tag计数类

      Saver：保存功能类

      DisplayGraphFrame：图片展示框架类

      

* **三.代码风格**

  * 程序可读性：
    * 每个类之前基本都有相应的整体注释。字段和方法以及方法内部大体步骤，都有注释说明。类中代码基本按照字段，构造函数，其他方法的顺序编写，相似的方法放在一起。
    * 变量名/函数名/类名注意单词首字母大写，控件名基本使用匈牙利命名法。
    * 程序结构比较清晰，相关功能的类尽量放在一个文件夹中。
  * 程序可扩充性：设置了相关的静态变量和参数常量，便于后续程序的扩充。
  * 程序健壮性：
    * 在爬取之前会进行URL的检验，相关方法放在UrlKit中。一个是可行性检验，另一个是网页相关内容的检验，这里借助HttpKit类实现。
    * 部分方法前添加了卫语句
    * 对输入输出和外部库的调用等，添加了异常的捕获与处理。
    * JUnit测试由于程序的性质，不太方便进行。
  * 程序易用性：
    * 引入外部库以助于进一步美化界面，给使用者较好的视觉体验。
  * 基本功能已经调试通过。

* **四.可改进之处**

  * 辅助功能在Dynamic动态爬取模式下的内容获取与爬取可以进一步优化。
  * 可以添加图片下载功能，对爬取的图片地址进行后续处理。
  * 可以选用更现代更好看的美化库。
  * 可以对爬取内容在输出文本框中的展示进行文本高亮处理。这里可以扩充一个文本编辑器类。
  * 论文的引用关系图的生成可以选用更好的库。
  * 可以尝试对论文的引用关系展示做一个动态的效果。

* **五.任务分工**

  * **朱大卫**：主要负责获取动态网页，论文爬取以及生成关系图。
  * **郑钦源**：主要负责将得到的关系图用Graphviz生成相应图片，展示其引用关系和被引用度。
  * **钟郅能**：主要负责GUI部分、美化以及代码的其他小型功能。

* **六.致谢**

  * 感谢各位组员的通力合作！
