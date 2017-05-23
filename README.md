# 我的第一个GitHub项目

此项目不再维护，请直接查看改进版[云资源ESR](https://github.com/abbenyyyyyy/yunziyuanESR) 

这是Android项目 [云资源](https://github.com/abbenyyyyyy/YunZiYuan.git) ，
欢迎访问。

APK下载地址 [云资源APK](http://pan.baidu.com/share/link?shareid=1753143903&uk=1946758570&from=homehot)

## 简介

一个整合百度云盘上面的电影资源的应用。
通过查看此应用所拥有的电影资源，然后打开该电影百度云盘播放和下载链接。PS：此应用所拥有的电影资源均是我编写Python爬虫在`http://www.2kbt.com/`收集的。

## APP截图

![joy.png](http://img1.ph.126.net/Gujhjr0K-lEhAmB4_cClpQ==/6630734613678014239.jpg)
![joy.png](http://img1.ph.126.net/Z2W29hEes4f6feLfP4XfxQ==/6619066596282626107.jpg)
![joy.png](http://img1.ph.126.net/qpbrXbedMy2GzwjivqCHqg==/6630525706468034365.jpg)

## 开发文档

### 需求

就是自己想做一个APP练手，然后发觉百度云盘在线播放电影的便利之处和缺少手机APP提供百度云盘上面的电影资讯的矛盾，所以就想做这个APP。

### 功能结构

![joy.png](http://img0.ph.126.net/wVAZmM8_8Etdwhkq_QheBQ==/6619291996166320959.png)

### 项目结构
基本基于‘MVC模式’进行设计，Application类和业务处理类均采用‘单例模式’以避免CG回收后失去其实例以及节约系统资源。
* app——Application Activity 的顶级父类
    
	1.MyApplication——继承Application，以便用HashMap存储全局变量``Movies``；
	
	2.AllScrollView——自定义的scrollView,复写OnLayout方法以便实现水平两列图片墙高度不对称；
	
	3.OpeningActivity——启动APP时候的启动画面，用一个线程控制更新客户端的资源xml文件以及跳转到``MainActivity``；
	
	4.MainActivity——主Activity，布局为屏幕上方一个FrameLayout，底部一个RadioGroup，通过对RadioGroup的监听实现Fragment对FrameLayout的填充；
	
	5.MovieActivity——显示电影具体信息的Activity；
	
	6.SearchActivity——点击``MainActivity``ActionBar中的``SearchView``后跳转的搜索电影Activity；
	
	7.ClearCacheActivity——点击actionbar中清除缓存后跳转的Activity，显示清除缓存的提示语并为了避免在清除缓存后还下载电影封面所建立的Activity。
	
* modle——数据层,数据模型
    
	1.Movie——存储一部电影信息的模型：电影类型、名字、封面URL、简介、截图URL、百度云URL、百度云提取码；
	
	2.Movies——存储某一特定类电影信息的模型，上面各种的数组。
	
* view——视图层，在这里Menu、Fragment和Activity的布局文件没有列出
	
	1.FirstFragment——主Activity中的第一页，对应主Activity底部RadioGroup第一个RadioButton，对应“所有电影”;
	
	2.SecondFragment——主Activity中的第二页，对应主Activity底部RadioGroup第二个RadioButton，对应“欧美电影”;
	
	3.ThirdFragment——主Activity中的第三页，对应主Activity底部RadioGroup第三个RadioButton，对应“日韩电影”;
	
	4.FourthFragment——主Activity中的第四页，对应主Activity底部RadioGroup第四个RadioButton，对应“国产电影”。
* controller——业务处理
	
	1.XmlParser——下载、更新电影资源信息以及解析客户端的电影资源信息到模型，服务端的电影资源信息保存格式为``xml``；
	
	2.ImageLoader——实现图片的缓存，通过``BitmapFactory.Option``按采样率来缩放图片以适应屏幕的宽度。

### 客户端文件存储方式与位置

客户端保存的文件有三种：1.电影资讯文件（XML文件）；2.电影资讯文件的版本号文件（txt文件）；3.电影封面文件（JPG文件）。

为了减少流量请求，所以电影资讯采用XML文件保存在客户端，下面为一部电影的资讯保存例子：

![joy.png](http://img1.ph.126.net/b5WtZ-Lsr1c5MAeMJNergA==/6619538286770938837.png)

这样，只有客户端第一次启动APP和服务端有更新的时候才会重新下载保存电影资讯的XML文件。

另外基于同样的理由，电影封面下载后保存在客户端SD\YunZiYuan下面。

### 网络请求写法,服务端
* 网络请求写法

1. 刚启动APP时候向服务端查询是否有电影资源文件的更新，如果有，就下载并代替客户的电影资源文件，此处的网络请求是采用[阿里云OSS服务（对象存储服务）文档](http://www.aliyun.com/product/oss/?spm=5176.383338.1906226.7.ZVHR1e)的API；

2. 当电影封面在缓存中找不到，再在SD\YunZiYuan中找不到，就会触发``AllScrollView``中的``LoadImageTask``，其继承``AsyncTask``异步下载图片；

3. 另外当用户点击``MovieActivity``中的显示电影截图时候，会触发``MovieActivity``中的``LoadPrintscreen``，也是继承``AsyncTask``异步下载图片。

* 服务端

采用[阿里云OSS服务（对象存储服务）](http://www.aliyun.com/product/oss/?spm=5176.383338.1906226.7.ZVHR1e)，具体可查看[阿里云OSS服务（对象存储服务）文档](http://www.aliyun.com/product/oss/?spm=5176.383338.1906226.7.ZVHR1e)。

## 依赖库与SDK

- [appcompat-v7](https://developer.android.com/tools/support-library/features.html#v7-appcompat)
- [阿里云OSS](https://docs.aliyun.com/?spm=5176.383663.201.106.nhr9sN#/pub/oss/sdk/android-sdk&preface)

## 我的心得体会

### 心得
* ``AllScrollView``，实现自定义的ScrollView

为了实现一个手机界面两列电影封面的不平衡，所以我放弃了``GridView``而采用自定义的ScrollView，通过自定义的ScrollView学习了View的工作机制：``measure--layout--draw``三大流程，
以及在自定义的ScrollView中处理滑动冲突，因为怕ScrollView的自带滑动机制与``OnScrollListener``是否冲突，所以我这里是实现了``View.OnTouchListener``接口，通过Handler机制处理滑动响应。
在onTouch方法中每当监听到手指离开屏幕，就会发送手指离开屏幕时候的自定义ScrollView对象给Handler,Handler接收到Message,进行处理通过``ScrollView.getScrollY()``比较判断当前滑动状态。

* 图片的缓存实现，避免OOM

1. 图片的内存缓存是使用了LRU算法，另外``AllScrollView.loadMoreImages()``方法实现整页加载以减少加载图片次数；
2. 还有通过``AllScrollView.checkVisibility()``进行图片可见性检查，检查目前照片墙上的所有图片，判断出哪些是可见的，哪些是不可见。然后将那些不可见的图片都替换成一张空图，这样就可以保证APP始终不会占用过高的内存。当这些图片又重新变为可见的时候，
只需要再从LruCache中将这些图片重新取出即可。
3. 当然图片在加载到ImageView前通过``ImageLoader.decodeSampledBitmapFromResource``进行压缩适配，其基于``BitmapFactory.Option``按采样率来缩放图片以适应屏幕的宽度。

### 体会

这是我第一个自己构想并实现的Android项目，在实现的过程学到很多，也遇到挺多困难，包括通过学习Python爬虫收集电影资源、查看Android Developer理解View的工作流程以便实现自定义ScrollView，查看Android Developer实现``SearchView``等等，
但是都一一克服，挺有满足感的。另外基于时间的问题，代码的重构，还有许多构想还没在这个项目实现，如线程池的引入，DiskLruCache的实现等等。

### 联系方式

Email:407523391@qq.com
