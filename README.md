#斗鱼官方获取直播源地址API加密算法更新了，需重新下载代码编译/apk安装，才能观看视频

#video-live

###项目介绍
视频直播源从斗鱼接口抓取，直播源分析来自[斗鱼API](https://github.com/soimort/you-get/blob/0984190f93bd0b5c55748c41ca657d1ba6bf5a6b/src/you_get/extractors/douyutv.py)，斗鱼弹幕提供官方API，具体实现是从斗鱼官网提供的java版本修改得到（TextUtils替换StringUtils、添加弹幕消息处理接口），视频播放器使用[vitamio](https://www.vitamio.org/)，弹幕实现使用[弹幕烈焰使](https://github.com/Bilibili/DanmakuFlameMaster)。[点击此处下载apk安装文件](https://github.com/littleMeng/video-live/raw/master/app-debug.apk)。

---

#ScreenShot
####播放页面
![image](https://raw.githubusercontent.com/littleMeng/video-live/master/screen-shot/play.png)

####首页
![image](https://raw.githubusercontent.com/littleMeng/video-live/master/screen-shot/head.png)

####分类
![image](https://raw.githubusercontent.com/littleMeng/video-live/master/screen-shot/classify.png)

####收藏
![image](https://raw.githubusercontent.com/littleMeng/video-live/master/screen-shot/heart.png)

---

###项目构建（android studio）
1. 下载[本项目](https://github.com/littleMeng/video-live)和[vitamio源码](https://www.vitamio.org/Download/)（选择android版本）并解压
2. 打开已存在项目，选择本项目解压后的文件夹
3. 添加vitamio模块，File->New->Import Module，选择vitamio源码路径，此时报错找不到ANDROID_BUILD_SDK_VERSION，对照项目app修改为对应版本即可
```java
    android {
        compileSdkVersion Integer.parseInt(project.ANDROID_BUILD_SDK_VERSION)
        buildToolsVersion project.ANDROID_BUILD_TOOLS_VERSION
        defaultConfig {
        minSdkVersion Integer.parseInt(project.ANDROID_BUILD_MIN_SDK_VERSION)
        targetSdkVersion Integer.parseInt(project.ANDROID_BUILD_TARGET_SDK_VERSION)
    }
```

###功能
* 斗鱼综合、dota2、LOL、炉石频道直播聚合
* 对应房间弹幕显示
* 输入关键字搜索房间

###具体功能
* 直播页面——下拉刷新&上拉加载更多，返回20条直播页面信息，点击进入指定房间开始视频直播，显示弹幕
* 收藏感兴趣房间

###TODO
* 引入mvp框架

###依赖
```java
    dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    testCompile 'junit:junit:4.12'
    compile project(path: ':InitActivity')
    compile 'com.android.support:appcompat-v7:23.4.0'
    compile 'in.srain.cube:ultra-ptr:1.0.11'
    compile 'com.mcxiaoke.volley:library:1.0.19'
    compile 'com.astuetz:pagerslidingtabstrip:1.0.1'
    compile 'com.google.code.gson:gson:2.6.2'
    compile 'com.android.support:cardview-v7:23.4.0'
    compile 'com.android.support:recyclerview-v7:23.4.0'
    compile 'com.github.ctiao:DanmakuFlameMaster:0.4.6'
    compile 'com.android.support:support-v4:23.4.0'
    compile 'de.hdodenhof:circleimageview:2.0.0'
    compile 'com.android.support:design:23.4.0'
    compile 'com.rengwuxian.materialedittext:library:2.1.4'
}
```

###BUG

---

###联系方式
* 邮  箱 : yikaishao@163.com
* Github : https://github.com/littleMeng
