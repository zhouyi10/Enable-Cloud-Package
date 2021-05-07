package com.blankj.utilcode.util;

import java.util.Arrays;

/**
 * android Logging support
 *
 * @Date 2020/06/23$ 08:55$
 * @Author caleb_liu@enable-ets.com
 **/

public final class LogUtils {

    private static final Config CONFIG = new Config();

    public static Config getConfig() {
        return CONFIG;
    }

    public static void v(final Object... contents) {
//        throw new UnsupportedOperationException(Arrays.toString(contents));
    }

    public static void vTag(final String tag, final Object... contents) {
//        throw new UnsupportedOperationException(tag + ", " + Arrays.toString(contents));
    }

    public static void d(final Object... contents) {
//        throw new UnsupportedOperationException(Arrays.toString(contents));
    }

    public static void dTag(final String tag, final Object... contents) {
//        throw new UnsupportedOperationException(tag + ", " + Arrays.toString(contents));
    }

    public static void i(final Object... contents) {
//        throw new UnsupportedOperationException(Arrays.toString(contents));
    }

    public static void iTag(final String tag, final Object... contents) {
//        throw new UnsupportedOperationException(tag + ", " + Arrays.toString(contents));
    }

    public static void w(final Object... contents) {
//        throw new UnsupportedOperationException(Arrays.toString(contents));
    }

    public static void wTag(final String tag, final Object... contents) {
//        throw new UnsupportedOperationException(tag + ", " + Arrays.toString(contents));
    }

    public static void e(final Object... contents) {
//        throw new UnsupportedOperationException(Arrays.toString(contents));
    }

    public static void eTag(final String tag, final Object... contents) {
//        throw new UnsupportedOperationException(tag + ", " + Arrays.toString(contents));
    }

    public static void a(final Object... contents) {
//        throw new UnsupportedOperationException(Arrays.toString(contents));
    }

    public static void aTag(final String tag, final Object... contents) {
//        throw new UnsupportedOperationException(tag + ", " + Arrays.toString(contents));
    }


    public static final class Config {
        private String mDefaultDir;// The default storage directory of log.
        private String mDir;       // The storage directory of log.
        private String mFilePrefix = "util";// The file prefix of log.
        private String mFileExtension = ".txt";// The file extension of log.
        private boolean mLogSwitch = true;  // The switch of log.
        private boolean mLog2ConsoleSwitch = true;  // The logcat's switch of log.
        private String mGlobalTag = "";    // The global tag of log.
        private boolean mTagIsSpace = true;  // The global tag is space.
        private boolean mLogHeadSwitch = true;  // The head's switch of log.
        private boolean mLog2FileSwitch = false; // The file's switch of log.
        private boolean mLogBorderSwitch = true;  // The border's switch of log.
        private boolean mSingleTagSwitch = true;  // The single tag of log.
        private int mConsoleFilter = 2;     // The console's filter of log.
        private int mFileFilter = 2;     // The file's filter of log.
        private int mStackDeep = 1;     // The stack's deep of log.
        private int mStackOffset = 0;     // The stack's offset of log.
        private int mSaveDays = -1;    // The save days of log.

        public String getmDefaultDir() {
            return mDefaultDir;
        }

        public void setmDefaultDir(String mDefaultDir) {
            this.mDefaultDir = mDefaultDir;
        }

        public String getmDir() {
            return mDir;
        }

        public void setmDir(String mDir) {
            this.mDir = mDir;
        }

        public String getmFilePrefix() {
            return mFilePrefix;
        }

        public void setmFilePrefix(String mFilePrefix) {
            this.mFilePrefix = mFilePrefix;
        }

        public String getmFileExtension() {
            return mFileExtension;
        }

        public void setmFileExtension(String mFileExtension) {
            this.mFileExtension = mFileExtension;
        }

        public boolean ismLogSwitch() {
            return mLogSwitch;
        }

        public void setmLogSwitch(boolean mLogSwitch) {
            this.mLogSwitch = mLogSwitch;
        }

        public boolean ismLog2ConsoleSwitch() {
            return mLog2ConsoleSwitch;
        }

        public void setmLog2ConsoleSwitch(boolean mLog2ConsoleSwitch) {
            this.mLog2ConsoleSwitch = mLog2ConsoleSwitch;
        }

        public String getmGlobalTag() {
            return mGlobalTag;
        }

        public void setmGlobalTag(String mGlobalTag) {
            this.mGlobalTag = mGlobalTag;
        }

        public boolean ismTagIsSpace() {
            return mTagIsSpace;
        }

        public void setmTagIsSpace(boolean mTagIsSpace) {
            this.mTagIsSpace = mTagIsSpace;
        }

        public boolean ismLogHeadSwitch() {
            return mLogHeadSwitch;
        }

        public void setmLogHeadSwitch(boolean mLogHeadSwitch) {
            this.mLogHeadSwitch = mLogHeadSwitch;
        }

        public boolean ismLog2FileSwitch() {
            return mLog2FileSwitch;
        }

        public void setmLog2FileSwitch(boolean mLog2FileSwitch) {
            this.mLog2FileSwitch = mLog2FileSwitch;
        }

        public boolean ismLogBorderSwitch() {
            return mLogBorderSwitch;
        }

        public void setmLogBorderSwitch(boolean mLogBorderSwitch) {
            this.mLogBorderSwitch = mLogBorderSwitch;
        }

        public boolean ismSingleTagSwitch() {
            return mSingleTagSwitch;
        }

        public void setmSingleTagSwitch(boolean mSingleTagSwitch) {
            this.mSingleTagSwitch = mSingleTagSwitch;
        }

        public int getmConsoleFilter() {
            return mConsoleFilter;
        }

        public void setmConsoleFilter(int mConsoleFilter) {
            this.mConsoleFilter = mConsoleFilter;
        }

        public int getmFileFilter() {
            return mFileFilter;
        }

        public void setmFileFilter(int mFileFilter) {
            this.mFileFilter = mFileFilter;
        }

        public int getmStackDeep() {
            return mStackDeep;
        }

        public void setmStackDeep(int mStackDeep) {
            this.mStackDeep = mStackDeep;
        }

        public int getStackOffset() {
            return mStackOffset;
        }

        public void setStackOffset(final int mStackOffset) {
            this.mStackOffset = mStackOffset;
        }

        public int getmSaveDays() {
            return mSaveDays;
        }

        public void setmSaveDays(int mSaveDays) {
            this.mSaveDays = mSaveDays;
        }

        public boolean isLog2ConsoleSwitch() {
            return false;
        }
    }
}
