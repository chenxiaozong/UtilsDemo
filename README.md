# UtilsDemo

#### 介绍
Android demo 封装android 应用常用工具类

#### 软件架构
##### 1. 日志工具类 com.chen.utilsdemo.utils/ChenLog10.java
地址：https://gitee.com/chenxiaozong/UtilsDemo
> 封装常用日志工具类：  

• 自动打印日志对应的类、行号  
• 拼接多个日志info字段使用","拼接  
• 控制I/V/D等级别日志打印  
• 日志前缀，版本号控制  

> 日志示例
```
//调用
ChenLog.i("日志打印，","tag1","info2")

//输出样式
02-14 22:22:16.306 17225-17225/com.chen.utilsdemo I/Version:1.0|chenxzong|MainActivity.onCreate(L:15): 日志打印，tag1info2
```


#### 码云特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  码云官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解码云上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是码云最有价值开源项目，是码云综合评定出的优秀开源项目
5.  码云官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  码云封面人物是一档用来展示码云会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
