package project.example.com.mymusicproject.bind;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import project.example.com.mymusicproject.R;


/**
 * 视图、资源、事件 绑定工具
 * 
 * @author 苏腾
 */
public class BindUtil {

	/**
	 * 进行绑定
	 * 
	 * @param target 要绑定的目标对象，并充当监听器
	 * @param view 要绑定视图所在父视图
	 */
	public static void bind(Object target, View view) {
		bind(target, new BindHelperImpl1(view), target);
	}

	/**
	 * 进行绑定
	 * 
	 * @param target 要绑定的目标对象
	 * @param view 要绑定视图所在父视图
	 * @param listener 监听器
	 */
	public static void bind(Object target, View view, Object listener) {
		bind(target, new BindHelperImpl1(view), listener);
	}

	/**
	 * 进行绑定
	 * 
	 * @param target 要绑定的目标对象
	 * @param activity 要绑定视图所在Activity，并充当监听器
	 */
	public static void bind(Object target, Activity activity) {
		bind(target, new BindHelperImpl2(activity), activity);
	}

	/**
	 * 进行绑定
	 * 
	 * @param target 要绑定的目标对象
	 * @param activity 要绑定视图所在Activity
	 * @param listener 监听器
	 */
	public static void bind(Object target, Activity activity, Object listener) {
		bind(target, new BindHelperImpl2(activity), listener);
	}

	/**
	 * 进行绑定
	 * 
	 * @param target 要绑定的目标对象
	 * @param bindHelper 视图绑定帮助类
	 * @param listener 监听器
	 */
	private static void bind(Object target, BindHelper bindHelper, Object listener) {
		ArrayList<Field> allFields = new ArrayList<>();
		Class<?> cls = target.getClass();
		while (cls != Object.class && cls != Activity.class) {
			Field[] fields = cls.getDeclaredFields();
			if (fields != null) {
				allFields.addAll(Arrays.asList(fields));
			}
			cls = cls.getSuperclass();
		}
		Resources resources = bindHelper.getContext().getResources();

		for (Field field : allFields) {
			// 检查属性是否有视图注解
			BindView bv = field.getAnnotation(BindView.class);
			if (bv != null && (View.class == field.getType() || View.class.isAssignableFrom(field.getType()))) {
				if (!field.isAccessible()) {// 如果属性不能访问则修改访问权限
					field.setAccessible(true);
				}
				View view = bindHelper.findViewById(bv.id());
				if (view != null) {
					try {
						field.set(target, view);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (bv.click())
						view.setOnClickListener(getListener(View.OnClickListener.class, listener));
					if (bv.longclick())
						view.setOnLongClickListener(getListener(View.OnLongClickListener.class, listener));
					if (bv.touch())
						view.setOnTouchListener(getListener(View.OnTouchListener.class, listener));
					if (bv.key())
						view.setOnKeyListener(getListener(View.OnKeyListener.class, listener));
					if (bv.focuschange())
						view.setOnFocusChangeListener(getListener(View.OnFocusChangeListener.class, listener));
					if (bv.contextmenu())
						view.setOnCreateContextMenuListener(getListener(View.OnCreateContextMenuListener.class, listener));
				}
				continue;
			}

			// 检查属性是否有资源注解
			BindResource br = field.getAnnotation(BindResource.class);
			if (br != null) {
				if (!field.isAccessible()) {// 如果属性不能访问则修改访问权限
					field.setAccessible(true);
				}
				try {
					field.set(target, getResource(resources, br, field.getName()));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		ArrayList<Method> allMethods = new ArrayList<>();
		cls = target.getClass();
		while (cls != Object.class) {
			Method[] methods = cls.getDeclaredMethods();
			if (methods != null) {
				allMethods.addAll(Arrays.asList(methods));
			}
			cls = cls.getSuperclass();
		}
		for (Method method : allMethods) {
			BindClick bc = method.getAnnotation(BindClick.class);
			if (bc == null) {
				continue;
			}
			for (int i = 0; i < bc.id().length; i++) {
				View view = bindHelper.findViewById(bc.id()[i]);
				if (view == null) {
					continue;
				}
				MethodInfo info = new MethodInfo(target, method, view);
				view.setTag(R.id.tagkey, info);
				view.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						MethodInfo info = (MethodInfo) v.getTag(R.id.tagkey);
						if (info != null) {
							info.invoke();
						}
					}
				});
			}
		}
	}

	private static Object getResource(Resources resources, BindResource air, String fieldName) {
		int resid = air.id();
		switch (air.type()) {
			case String:
				return resources.getString(resid);
			case StringArray:
				return resources.getStringArray(resid);
			case Integer:
				return resources.getInteger(resid);
			case IntArray:
				return resources.getIntArray(resid);
			case Boolean:
				return resources.getBoolean(resid);
			case Drawable:
				return resources.getDrawable(resid);
			case Color:
				return resources.getColor(resid);
			case ColorStateList:
				return resources.getColorStateList(resid);
			case Dimension:
				return resources.getDimension(resid);
			case DimensionPixelSize:
				return resources.getDimensionPixelSize(resid);
			case DimensionPixelOffset:
				return resources.getDimensionPixelOffset(resid);
			case Text:
				return resources.getText(resid);
			case TextArray:
				return resources.getTextArray(resid);
			case Movie:
				return resources.getMovie(resid);
			case Xml:
				return resources.getXml(resid);
			default:
				break;
		}
		// 默认抛出异常
		throw new IllegalArgumentException("资源属性:" + fieldName + " 自动绑定失败,检查资源id是否正确");
	}

	private static <T> T getListener(Class<T> cls, Object listener) {
		return cls.isAssignableFrom(listener.getClass()) ? cls.cast(listener) : null;
	}
}

/**
 * 视图绑定帮助实现1
 * 
 * @author 苏腾
 */
class BindHelperImpl1 implements BindHelper {

	private View mView;

	public BindHelperImpl1(View view) {
		mView = view;
	}

	@Override
	public View findViewById(int resId) {
		return mView.findViewById(resId);
	}

	@Override
	public Context getContext() {
		return mView.getContext();
	}

}

/**
 * 视图绑定帮助实现2
 * 
 * @author 苏腾
 */
class BindHelperImpl2 implements BindHelper {

	private Activity mActivity;

	public BindHelperImpl2(Activity activity) {
		mActivity = activity;
	}

	@Override
	public View findViewById(int resId) {
		return mActivity.findViewById(resId);
	}

	@Override
	public Context getContext() {
		return mActivity;
	}

}

/**
 * 视图绑定帮助接口
 * 
 * @author 苏腾
 */
interface BindHelper {

	/**
	 * 根据资源id返回视图
	 * 
	 * @param resId
	 * @return
	 */
	public View findViewById(int resId);

	/**
	 * 获取所在上下文
	 * 
	 * @return
	 */
	public Context getContext();
}

/**
 * 方法信息
 * 
 * @author 苏腾
 */
class MethodInfo {

	Object target;

	Method method;

	Object[] params;

	MethodInfo(Object target, Method method, View view) {
		this.target = target;
		this.method = method;
		Class<?>[] pts = method.getParameterTypes();
		if (pts != null && pts.length > 0) {
			params = new Object[pts.length];
			if (pts[0] != null && pts[0] == View.class) {
				params[0] = view;
			}
		}
	}

	void invoke() {
		try {
			if (!method.isAccessible()) {
				method.setAccessible(true);
			}
			method.invoke(target, params);
		} catch (Exception e) {
		}
	}

}