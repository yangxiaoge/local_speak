package com.seuic.tts.local_speak.voice;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

import java.io.IOException;

/**
 * @author 志尧
 * @date on 2018-01-12 13:51
 * @email 1417337180@qq.com
 * @describe 文件相关的工具类
 * @ideas
 */
public class FileUtils {
    /**
     * Assets获取资源
     *
     * @param context
     * @param filename
     * @return
     * @throws IOException
     */
    public static AssetFileDescriptor getAssetFileDescription(Context context, String filename) throws IOException {
        AssetManager manager = context.getApplicationContext().getAssets();
        return manager.openFd(filename);
    }

    /**
     * Assets获取资源
     *
     * @param context
     * @param soundResId 语音资源文件id
     * @return
     */
    public static AssetFileDescriptor getRawResourceFileDescription(Context context, int soundResId) {
        return context.getApplicationContext().getResources().openRawResourceFd(soundResId);
    }

}
