package com.mrcd.xrouter.core;

import android.util.LruCache;
import com.mrcd.xrouter.utils.LogUtils;
import java.lang.reflect.Method;

/**
 * 绑定数据的工具类
 */
public class DataBinder {

    private static final String BINDER_SUFFIX = "$$DataBinder";

    /**
     * 缓存数量为16
     */
    private static LruCache<String, Object> sBinderMap = new LruCache<>(2 << 3);

    public static void invokeBindDataMethod(Object target) {
        invokeBindDataMethod(target, target.getClass());
    }

    public static void invokeBindDataMethod(Object target, Class targetClass) {
        Class[] parameterTypes = {target.getClass(), IntentWrapper.class};
        Object[] params = new Object[]{target, IntentWrapper.getInstance()};
        invokeMethod(target, targetClass, "bindData", parameterTypes, params);
    }

    public static void invokeReleaseDataMethod(Object target) {
        invokeReleaseDataMethod(target, target.getClass());
    }

    public static void invokeReleaseDataMethod(Object target, Class targetClass) {
        Class[] parameterTypes = {target.getClass(), IntentWrapper.class};
        Object[] params = {target, IntentWrapper.getInstance()};
        invokeMethod(target, targetClass, "releaseData", parameterTypes, params);
    }

    /**
     * 通过反射创建binder对象，并调用其中的方法进行绑定数据，释放数据
     *
     * @param target     binder所关联的activity对象
     * @param methodName 方法名
     * @param paramType  参数类型
     * @param args       参数值
     */
    private static void invokeMethod(Object target, Class targetClazz, String methodName, Class[] paramType, Object[] args) {
        LogUtils.d("类型>>>>" + targetClazz);
        String className = targetClazz.getName();
        try {
            String binderClassName = className.concat(BINDER_SUFFIX);
            Class binderClass = Class.forName(binderClassName);
            Object instance = getBinder(binderClass);
            if (null == instance) {
                LogUtils.e("未找到数据绑定对象");
                return;
            }
            Method bindMethod = binderClass.getDeclaredMethod(methodName, paramType);
            bindMethod.invoke(instance, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取binder对象，内部进行了LRU缓存，最大为16
     *
     * @param binderClass binder类型
     * @return binder对象
     */
    private static Object getBinder(Class binderClass) {
        try {
            String className = binderClass.getName();
            Object instance = sBinderMap.get(className);
            if (null == instance) {
                instance = binderClass.newInstance();
                sBinderMap.put(className, instance);
            }
            return instance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
