package org.eclipse.paho.android.service;

import android.annotation.SuppressLint;
import android.content.Context;

import com.jiongbull.jlog.Logger;
import com.jiongbull.jlog.constant.LogLevel;
import com.jiongbull.jlog.constant.LogSegment;
import com.jiongbull.jlog.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 作    者：Luffy（张阳）
 * 版    本：1.0
 * 创建日期：2018/5/15
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class LoggerPrintInstance {

    public static void init(Context context) {
        List<String> logLevels = new ArrayList<>();
        logLevels.add(LogLevel.VERBOSE);
        logLevels.add(LogLevel.DEBUG);
        logLevels.add(LogLevel.INFO);
        logLevels.add(LogLevel.JSON);
        logLevels.add(LogLevel.WARN);
        logLevels.add(LogLevel.ERROR);
        logLevels.add(LogLevel.WTF);

        sLogger = Logger.Builder.newBuilder(context, "jlog")
                /* Properties below are default value, you can modify them or not. */
                .setWriteToFile(true)
                .setLogDir("QNCBLibraryLog")
                .setLogPrefix("")
                .setLogSegment(LogSegment.ONE_HOUR)
                .setLogLevelsForFile(logLevels)
                .setZoneOffset(TimeUtils.ZoneOffset.P0800)
                .setTimeFormat("yyyy-MM-dd HH:mm:ss")
                .setPackagedLevel(0)
                .setStorage(null)
                .build();
    }

    @SuppressLint("StaticFieldLeak")
    private static Logger sLogger;

    public static Logger getLogger() {
        return sLogger;
    }

}
