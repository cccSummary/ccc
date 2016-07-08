package project.example.com.mymusicproject.bind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 绑定资源 注解<br>
 * <br>
 * 在proguard配置文件里加入以下语句以防止字段被删除<br>
 * -keepclassmembers class * { @com.xxx.BindResource *; }
 * 
 * @author 苏腾
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindResource {

	/**
	 * 资源id
	 * 
	 * @return
	 */
	int id();

	/**
	 * 资源类型
	 * 
	 * @return
	 */
	ResourceType type();
}
