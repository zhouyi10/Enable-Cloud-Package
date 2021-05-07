package com.enableets.edu.sdk.ppr.cache;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Delayed queue
 * @author walle_yu@enable-ets.com
 * @since 2020/06/28
 **/
public class DelayedItem implements Delayed {

    private String key;

    private Long liveTime;

    private Long removeTime;

    public DelayedItem(String key, Long liveTime) {
        this.key = key;
        this.liveTime = liveTime;
        this.removeTime = TimeUnit.MILLISECONDS.convert(liveTime, TimeUnit.MILLISECONDS) + System.currentTimeMillis();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(removeTime - System.currentTimeMillis(), unit);
    }

    @Override
    public int compareTo(Delayed o) {
        if (o == null) return 1;
        if (o == this) return 0;
        if (o instanceof DelayedItem){
            DelayedItem delayedItem = (DelayedItem) o;
            if (liveTime > delayedItem.liveTime){
                return 1;
            }else if (liveTime == delayedItem.liveTime){
                return 0;
            }else{
                return -1;
            }
        }
        Long diff = getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS);
        return diff > 0 ? 1:diff == 0? 0:-1;
    }

    @Override
    public int hashCode(){
        return key.hashCode();
    }

    @Override
    public boolean equals(Object obj){
        if (obj instanceof DelayedItem){
            return obj.hashCode() == hashCode() ? true : false;
        }
        return false;
    }

    public String getKey() {
        return key;
    }
}
