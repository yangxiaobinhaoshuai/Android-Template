
# 扫码调研资料

### 要求
- cameraX 要求 minsdk version 21 ，占有 Android 设备总数 98%

### 步骤
1. Camera permission
2. preview
   1. 多码识别
   2. 自动聚焦
3. capture image
4. qrcode analysis

### 可选扫码实现
- [华为统一扫码服务](https://developer.huawei.com/consumer/cn/hms/huawei-scankit/)
- [Google mlKit barcode scanning](https://developers.google.com/ml-kit/vision/barcode-scanning)
- Zxing 默认实现



### 结论
- zxing 只做了最基础到扫码操作，复杂扫码环境下支持的并不是很好



### 参考
- [Camera google doc 概览](https://developer.android.com/training/camerax)
- [Zxing github](https://github.com/zxing/zxing)
- [Android 二维码扫描原理和优化方向](https://mp.weixin.qq.com/s/nqvGS9kco_bEi5MQUoqxuQ)
- [cameraX PreView](https://medium.com/androiddevelopers/display-a-camera-preview-with-previewview-86562433d86c)
