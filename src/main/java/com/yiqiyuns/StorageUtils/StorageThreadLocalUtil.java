package com.yiqiyuns.StorageUtils;

/**
 * 使用 ThreadLocal 存储数据，保证线程安全
 *
 * @author 17Yuns
 * @version 1.0
 */
@SuppressWarnings("all")
public class StorageThreadLocalUtil<T> {
    // 使用泛型 ThreadLocal<T>
    private static final ThreadLocal<Object> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 获取当前线程的存储值
     *
     * @return 存储在 ThreadLocal 中的值
     */
    public T get() {
        return (T) THREAD_LOCAL.get();
    }

    /**
     * 设置当前线程的数据
     *
     * @param value 要存储的数据
     */
    public void set(T value) {
        THREAD_LOCAL.set(value);
    }

    /**
     * 清除当前线程的存储值
     * 防止内存泄漏
     */
    public void remove() {
        THREAD_LOCAL.remove();
    }
}
