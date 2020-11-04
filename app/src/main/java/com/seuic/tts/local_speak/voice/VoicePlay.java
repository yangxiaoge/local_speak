package com.seuic.tts.local_speak.voice;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 志尧
 * @date on 2018-01-12 15:09
 * @email 1417337180@qq.com
 * @describe 音频播放
 * @ideas
 */
public class VoicePlay {

    private ExecutorService mExecutorService;
    private Context mContext;

    private VoicePlay(Context context) {
        this.mContext = context;
        this.mExecutorService = Executors.newCachedThreadPool();
    }

    private volatile static VoicePlay mVoicePlay = null;

    /**
     * 单例
     *
     * @return
     */
    public static VoicePlay with(Context context) {
        if (mVoicePlay == null) {
            synchronized (VoicePlay.class) {
                if (mVoicePlay == null) {
                    mVoicePlay = new VoicePlay(context);
                }
            }
        }
        return mVoicePlay;
    }

    /**
     * 播放指定语音文件
     *
     * @param resourceId 语音文件id
     */
    public void playVoice(int resourceId) {
        mExecutorService.execute(() -> start(resourceId));
    }

    /**
     * 开始播报指定资源语音文件
     *
     * @param resourceId
     */
    private void start(final int resourceId) {
        synchronized (VoicePlay.this) {

            MediaPlayer mMediaPlayer = new MediaPlayer();
            final CountDownLatch mCountDownLatch = new CountDownLatch(1);
            AssetFileDescriptor assetFileDescription = null;

            try {
                assetFileDescription = FileUtils.getRawResourceFileDescription(mContext,
                        resourceId);
                mMediaPlayer.setDataSource(
                        assetFileDescription.getFileDescriptor(),
                        assetFileDescription.getStartOffset(),
                        assetFileDescription.getLength());
                mMediaPlayer.prepareAsync();
                mMediaPlayer.setOnPreparedListener(mediaPlayer -> mMediaPlayer.start());
                mMediaPlayer.setOnCompletionListener(mediaPlayer -> {
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mCountDownLatch.countDown();
                });

            } catch (Exception e) {
                e.printStackTrace();
                mCountDownLatch.countDown();
            } finally {
                if (assetFileDescription != null) {
                    try {
                        assetFileDescription.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                mCountDownLatch.await();
                notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void startWithCallback(int resourceId, VoiceCallback callback) {
        synchronized (VoicePlay.this) {

            MediaPlayer mMediaPlayer = new MediaPlayer();
            AssetFileDescriptor assetFileDescription = null;

            try {
                assetFileDescription = FileUtils.getRawResourceFileDescription(mContext,
                        resourceId);
                mMediaPlayer.setDataSource(
                        assetFileDescription.getFileDescriptor(),
                        assetFileDescription.getStartOffset(),
                        assetFileDescription.getLength());
                mMediaPlayer.prepareAsync();
                mMediaPlayer.setOnPreparedListener(mediaPlayer -> mMediaPlayer.start());
                mMediaPlayer.setOnCompletionListener(mediaPlayer -> {
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    if (callback != null) {
                        callback.complete();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                if (callback != null) {
                    callback.error(e.getMessage());
                }
            } finally {
                if (assetFileDescription != null) {
                    try {
                        assetFileDescription.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            notifyAll();
        }

    }

    public interface VoiceCallback {
        void complete();

        void error(String msg);
    }
}
