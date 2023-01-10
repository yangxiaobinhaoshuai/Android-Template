package com.yxb.simple.adapters

import android.view.GestureDetector.SimpleOnGestureListener
import android.view.ScaleGestureDetector

/**
 * @see ScaleGestureDetector.OnScaleGestureListener
 */
typealias ScaleGestureListenerAdapter = ScaleGestureDetector.SimpleOnScaleGestureListener


/**
 * @see android.view.GestureDetector.OnGestureListener
 */
typealias OnGestureListenerAdapter = SimpleOnGestureListener