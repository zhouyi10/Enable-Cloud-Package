package com.enableets.edu.pakage.core.core.cache;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/28
 **/
@Data
@AllArgsConstructor
public class CacheItem {

    private String data;

    private DelayedItem item;
}
