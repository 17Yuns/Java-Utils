package com.yiqiyuns.StorageUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用 ConcurrentHashMap 全局性存储数据，一次性存储，多次读取
 * 支持存储任意类型的数据
 *
 * @author 17Yuns
 * @version 1.0
 */
@SuppressWarnings("all")
public class StorageHashMapUtil {

    // 使用 ConcurrentHashMap 存储数据，支持任意类型
    private ConcurrentHashMap<String, Object> dataMap = new ConcurrentHashMap<>();

    // 私有化构造器，防止外部创建实例
    private StorageHashMapUtil() {}

    // 使用内部类实现单例模式
    private static class SingletonHelper {
        private static final StorageHashMapUtil INSTANCE = new StorageHashMapUtil();
    }

    // 获取唯一实例
    public static StorageHashMapUtil getInstance() {
        return SingletonHelper.INSTANCE;
    }

    // 存储数据，支持任意类型
    public <T> void saveData(String key, T value) {
        dataMap.put(key, value);
    }

    // 获取数据，返回 Object 类型，需要进行类型转换
    public Object getData(String key) {
        return dataMap.get(key);
    }

    // 删除数据
    public void removeData(String key) {
        dataMap.remove(key);
    }

    // 获取数据并转换为指定类型
    public <T> T getData(String key, Class<T> clazz) {
        Object value = dataMap.get(key);
        if (value != null && clazz.isInstance(value)) {
            return clazz.cast(value);
        }
        return null;
    }
}
