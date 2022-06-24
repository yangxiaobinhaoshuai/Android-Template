#-keep public class me.yangxiaobin.kotlin.compose.lib.AbsComposableFragment{ *; }

#-keep public class me.yangxiaobin.qrcode.CameraFragment{ *;}


#-if public class me.yangxiaobin.qrcode.CameraFragment
#-keepclasseswithmembers public class me.yangxiaobin.qrcode.CameraFragment {
#    public <init>();
#}
