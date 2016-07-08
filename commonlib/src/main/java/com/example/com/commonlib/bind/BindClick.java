package com.example.com.commonlib.bind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 绑定点击事件 注解<br>
 * 如果该方法的第一个参数是View，那么将传递所点击的View进来，其他情况的参数都传递null<br>
 * <br>
 * 在proguard配置文件里加入以下语句以防止字段被删除<br>
 * -keepclassmembers class * { @com.xxx.BindClick *; }
 * 
 * @author 苏腾
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindClick {

	/**
	 * 视图id
	 */
	int[] id();

}
