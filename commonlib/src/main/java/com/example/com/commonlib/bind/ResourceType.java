package com.example.com.commonlib.bind;

import android.support.annotation.Keep;

/**
 * 绑定资源类型
 * 
 * @see BindResource
 * @author 苏腾
 */
@Keep
public enum ResourceType {
	String, StringArray, Integer, IntArray, Boolean, Drawable, Color, ColorStateList, Dimension, DimensionPixelOffset, DimensionPixelSize, Text, TextArray, Movie, Xml
}