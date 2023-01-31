package me.yangxiaobin.image_edit.lib.view

import android.graphics.Paint

data class PathArrows(
    var start: ArrowsCoordinate,    //开始坐标
    var end: ArrowsCoordinate,      //结束坐标
    var mColor: Int,                 //颜色
    var size: Int                   //大小
) {

    val mPaint: Paint = Paint().apply {
        color = mColor
    }
}


data class ArrowsCoordinate(
    val x: Float,
    val y: Float
)