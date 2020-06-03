# MultiFunctionCrawler
#### 一、程序功能与特色

##### 基于Google Chrome 83 version的百度文库论文爬取与关系分析

* 使用方法：使用者只需在input文本框中输入初始百度文库中论文的URL，调整好参数，然后点击CrawlEssay按钮，即可运行爬虫。引用量在MinCitedThreshold以下的论文会被淘汰，MaxBranch是从每篇论文爬取的最多分支数，MaxEssayCnt是爬取论文的最大数量。

* 在爬行过程中，爬到的文献会展示在下方的输出文本框中。爬行结束后，点击SaveEssay即可将爬到的论文相关信息保存至默认文件夹saved的一个文件中。点击MakeGraph按钮即可生成文献引用关系图，在弹窗中展示出来。之后再点击SaveGraph即可将图片保存至默认文件夹saved，点击Quit则不保存文件。

  **CrawlEssay：**从输入URL开始爬取百度文库论文。

  **SaveEssay：**保存爬取到的论文结果。

  **MakeGraph：**可视化，产生爬取到的论文的引用关系图，并用颜色标记其引用量。


##### 其他爬虫功能

除了对百度文库论文的爬取之外，程序还有一些辅助功能，放在Functions菜单中：

- **NewUrlCrawl**：在input文本框中输入初始URL，点击NewUrlCrawl即可进行一次新的网页爬取。每次爬取一定量的网页。
- **KeepUrlCrawl**：继续当前爬取，增加爬取网页的数量。如果中途转而使用了其他功能，则不会记录以往的爬行结果。
- **CountTag**：计数网页中的tag。
- **GetEA**：爬取该网页下的email地址。
- **GetPic**：爬取该网页中的图片地址，以便后续下载。
- **HttpGet**：以GET的方式请求获取URL内容。
- **Save**：保存以上功能的结果到默认文件夹中。文件的名字由处理过的URL+功能名+模式名构成。
- **Clear**：清空输出文本框中的内容。
- **Quit**：退出程序。

##### 其他爬取功能爬取模式选取

​	另外，该程序还添加了模式选取功能，放在Mode菜单中。选定Dynamic，则是从GET获得的URL内容中进行爬取；选定Static，则是直接Download网页内容。这一部分还可以进一步扩展。

##### 界面主题更换

​	最后，我们还为该程序添加了一个主题更换的功能，放在菜单Theme中。点击相应主题即可更换。

#### 二、程序使用注意事项

* 程序适配性：由于百度文库许多内容不能直接爬取，在这里我们利用selenium，借助chromedriver（适配Chrome 83）来模拟人的爬取过程。因为chromedriver具有一定的适配限制，所以该功能可能不能在没有安装Google Chrome 83机器上使用。
* 路径设置：由于graphviz和chromedrive文件体积略大，打包至jar中显得累赘且不便于调用，故采用单独存放。而由于测试时和使用jar包时工作路径不同，故需要进行不同的设置
  * **请不要移动graphviz和chromedrive的位置！**

  * 测试时EssayCrawler 67行路径设为"..\\\chromedriver.exe"，MakeGraph 33行路径设为"..\\\\graphvis\\\\bin\\\\dot.exe"
  * 打包成jar之前，将上述两处".."改为"."