# 我的第一个GitHub项目

这是项目 [云资源](https://github.com/abbenyyyyyy/YunZiYuan.git) ，
欢迎访问。



## 简介

一个整合百度云盘上面的电影资源的应用。
通过查看此应用所拥有的电影资源，然后打开该电影百度云盘播放和下载链接。PS：此应用所拥有的电影资源均是我编写Python爬虫在`http://www.2kbt.com/`收集的。

## APP截图
-![joy.png]()
+![joy.png](http://img.wdjimg.com/mms/screenshot/2/24/bb2ec85c230a7eeecdc4399bc6892242_320_535.jpeg)
+![joy.png](http://img.wdjimg.com/mms/screenshot/6/f5/3dad64a4c3359af5692072b08cb03f56_320_535.jpeg)

## 开发文档

### 需求

就是自己想做一个APP练手，然后发觉百度云盘在线播放电影的便利之处和缺少手机APP提供百度云盘上面的电影资讯的矛盾，所以就想做这个APP。

### 功能结构
-![joy.png]()
+![joy.png](http://img.wdjimg.com/mms/screenshot/2/24/bb2ec85c230a7eeecdc4399bc6892242_320_535.jpeg)

### 项目结构
基本基于‘MVC模式’进行设计，Application类和业务处理类均采用‘单例模式’以避免CG回收后失去其实例以及节约系统资源。
* app——Application Activity 的顶级父类
    MyApplication——继承Application，以便用HashMap存储全局变量``Movies``；
	
	AllScrollView——自定义的scrollView,复写OnLayout方法以便实现水平两列图片墙高度不对称；
	
	OpeningActivity——启动APP时候的启动画面，用一个线程控制更新客户端的资源xml文件以及跳转到``MainActivity``；
	
	MainActivity——主Activity，布局为屏幕上方一个FrameLayout，底部一个RadioGroup，通过对RadioGroup的监听实现Fragment对FrameLayout的填充；
	
	MovieActivity——显示电影具体信息的Activity；
	
	ClearCacheActivity——点击actionbar中清除缓存后跳转的Activity，显示清除缓存的提示语并为了避免在清除缓存后还下载电影封面所建立的Activity。
* modle——数据层,数据模型
    Movie——存储一部电影信息的模型：电影类型、名字、封面URL、简介、截图URL、百度云URL、百度云提取码；
	
	Movies——存储某一特定类电影信息的模型，上面各种的数组。
* view——
	FirstFragment——主Activity中的第一页，对应主Activity底部RadioGroup第一个RadioButton，对应“所有电影”;
	
	SecondFragment——主Activity中的第二页，对应主Activity底部RadioGroup第二个RadioButton，对应“欧美电影”;
	
	ThirdFragment——主Activity中的第三页，对应主Activity底部RadioGroup第三个RadioButton，对应“日韩电影”;
	
	FourthFragment——主Activity中的第四页，对应主Activity底部RadioGroup第四个RadioButton，对应“国产电影”。
* controller——业务处理
	XmlParser——下载、更新电影资源信息以及解析客户端的电影资源信息到模型，服务端的电影资源信息保存格式为``xml``；
	
	ImageLoader——实现图片的缓存，通过``BitmapFactory.Option``按采样率来缩放图片以适应屏幕的宽度

## 依赖库与SDK

- [appcompat-v7](https://developer.android.com/tools/support-library/features.html#v7-appcompat)/[cardview-v7](https://developer.android.com/tools/support-library/features.html#v7-cardview)

## 克隆版本库

操作示例：

    $ git clone git@github.com:abbenyyyyyy/YunZiYuan.git