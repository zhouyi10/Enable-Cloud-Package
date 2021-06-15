package com.enableets.edu.pakage.framework.core;

import java.util.List;

/**
 * 定义交卷数据存储队列
 * @author walle_yu@enable-ets.com
 * @since 2021/01/28
 **/
public interface IJedisCache<T> {

    public void push(List<T> list);

    public List<T> poll(int size);

    public int size();

    public boolean isEmpty();

    public void empty();

    public void put(String key, String value);

    public String get(String key);

    public void del(String key);
}
