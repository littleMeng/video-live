###项目介绍
视频直播源从斗鱼接口抓取，直播源分析来自[斗鱼API](http://430.io/-xie-dou-yu-tv-web-api-some-douyutv-api/)，其中利用到的MD5加密分析来自[MD5](https://github.com/0987363/douyutv-fix/blob/3dd6b9762a4cf5d359170b4a912457a0d4b5f5e5/DouYu-kodi-fix/APIHelper.py)，斗鱼弹幕提供官方API，具体实现是从斗鱼官网提供的java版本修改得到（TextUtils替换StringUtils、添加弹幕消息处理接口），视频播放器使用[vitamio](https://www.vitamio.org/)，弹幕实现使用[弹幕烈焰使](https://github.com/Bilibili/DanmakuFlameMaster)。

#video-live

####项目构建（android studio）
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
* 斗鱼dota2频道直播聚合
* 对应房间弹幕显示

###具体功能
* 直播页面——下拉刷新，返回20条直播页面信息，点击进入指定房间开始视频直播，显示弹幕

###待添加功能
* 视频直播页面添加返回按钮、弹幕的显示/隐藏/显示位置控制（控制按钮均自动隐藏，触摸屏幕显示）

###依赖
```java
    dependencies {
        compile fileTree(dir: 'libs', include: ['*.jar'])
        testCompile 'junit:junit:4.12'
        compile 'com.android.support:appcompat-v7:23.4.0'
        compile project(path: ':vitamio')
        compile 'in.srain.cube:ultra-ptr:1.0.11'
        compile 'com.mcxiaoke.volley:library:1.0.19'
        compile 'com.astuetz:pagerslidingtabstrip:1.0.1'
        compile 'com.google.code.gson:gson:2.6.2'
        compile 'com.android.support:cardview-v7:23.4.0'
        compile 'com.android.support:recyclerview-v7:23.3.0'
        compile 'com.github.ctiao:DanmakuFlameMaster:0.4.6'
    }
```

###BUG

---

###联系方式
* 邮  箱 : yikaishao@163.com
* Github : https://github.com/littleMeng
