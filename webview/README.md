

# webView 优化

webView 加载流程： webView 初始化 (9%) -> DNS 解析及连接(55%) -> 下载 Html 页面及解析(28%) -> 加载静态资源(8%) -> Display

优化点：
静态资源缓存
HTML 预取缓存
利用 webview 初始化时间并行做一些事情

http 缓存机制
![img.png](pics/http_cache.png)

webView 缓存机制
![img.png](pics/webview_cache.png)



- [Tencent vasSonic](https://github.com/Tencent/VasSonic/blob/master/sonic-android/docs/Sonic%20Quick%E6%A8%A1%E5%BC%8F%E5%AE%9E%E7%8E%B0%E5%8E%9F%E7%90%86.md)

- [飞书 webview 优化](https://mp.weixin.qq.com/s/3t82acKigomPrqLq7-gelQ)

