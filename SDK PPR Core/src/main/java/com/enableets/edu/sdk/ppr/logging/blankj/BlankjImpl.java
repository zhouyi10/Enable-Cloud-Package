package com.enableets.edu.sdk.ppr.logging.blankj;

import com.blankj.utilcode.util.LogUtils;
import com.enableets.edu.sdk.ppr.logging.Log;
import com.enableets.edu.sdk.ppr.logging.LogFactory;

/**
 * Blankj Log For Android implement
 *
 * @author caleb_liu@enable-ets.com
 * @create 2020/06/22
 **/

public class BlankjImpl implements Log {

    private String tagHand = LogFactory.MARKER + ": ";

    private String tag = "";

    private int mStackOffset = 0;

    public BlankjImpl(String clazz) {
        tag = tagHand + clazz;
    }

    @Override
    public boolean isDebugEnabled() {
        return LogUtils.getConfig().isLog2ConsoleSwitch();
    }

    @Override
    public boolean isInfoEnabled() {
        return LogUtils.getConfig().isLog2ConsoleSwitch();
    }

    @Override
    public void error(String s, Throwable e) {
        this.resetConfig(() -> LogUtils.eTag(tag, s, e));
    }

    @Override
    public void error(String s) {
        this.resetConfig(() -> LogUtils.eTag(tag, s));
    }

    @Override
    public void debug(String s) {
        this.resetConfig(() -> LogUtils.dTag(tag, s));
    }

    @Override
    public void info(String s) {
        this.resetConfig(() -> LogUtils.iTag(tag, s));
    }

    @Override
    public void warn(String s) {
        this.resetConfig(() -> LogUtils.wTag(tag, s));
    }

    /**
     * Due to the encapsulation of a layer, the path of the LogUtils package name is incorrect, and the offset is set to solve
     * @param c function
     * @return void
     * @Date 2020/06/23 17:04
     * @Author caleb_liu@enable-ets.com
     */
    private void resetConfig(Runnable c) {
        mStackOffset = LogUtils.getConfig().getStackOffset();
        LogUtils.getConfig().setStackOffset(mStackOffset + 4);
        c.run();
        LogUtils.getConfig().setStackOffset(mStackOffset);
    }
}
