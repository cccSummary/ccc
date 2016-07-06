package project.example.com.mymusicproject.util;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import project.example.com.mymusicproject.Constants;
import project.example.com.mymusicproject.MusicApplication;
import project.example.com.mymusicproject.R;
import project.example.com.mymusicproject.model.MusicInfo;

/**
 * 文件工具类
 */
public class FileUtils {
    private static String getAppDir() {
        return Environment.getExternalStorageDirectory() + "/MyMusic/";
    }

    public static String getLrcDir() {
        String dir = getAppDir() + "Lyric/";
        return mkdirs(dir);
    }

    /**
     * 获取歌词路径
     * 先从已下载文件夹中查找，如果不存在，则从歌曲文件所在文件夹查找
     */
    public static String getLrcFilePath(MusicInfo music) {
        String lrcFilePath  =getLrcDir() + FileUtils.getLrcFileName(music.getArtist(), music.getTitle());
//        String lrcFilePath = getLrcDir() + music.getTitle() + Constants.FILENAME_LRC;
        if (fileIsExists(lrcFilePath)) {//判断文件是否存在
            lrcFilePath = music.getUrl().replace(Constants.FILENAME_MP3, Constants.FILENAME_LRC);
        }
        return lrcFilePath;
    }

    private static String mkdirs(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }

    public static String getMp3FileName(String artist, String title) {
        artist = stringFilter(artist);
        title = stringFilter(title);
        if (TextUtils.isEmpty(artist)) {
            artist = MusicApplication.getInstance().getString(R.string.unknown);
        }
        if (TextUtils.isEmpty(title)) {
            title = MusicApplication.getInstance().getString(R.string.unknown);
        }
        return artist + " - " + title + Constants.FILENAME_MP3;
    }

    public static String getLrcFileName(String artist, String title) {
        artist = stringFilter(artist);
        title = stringFilter(title);
        if (TextUtils.isEmpty(artist)) {
            artist = MusicApplication.getInstance().getString(R.string.unknown);
        }
        if (TextUtils.isEmpty(title)) {
            title = MusicApplication.getInstance().getString(R.string.unknown);
        }
        return artist+"-"+title + Constants.FILENAME_LRC;
    }

    /**
     * 过滤特殊字符(\/:*?"<>|)
     */
    private static String stringFilter(String str) {
        if (str == null) {
            return null;
        }
        String regEx = "[\\/:*?\"<>|]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 时间转换
     **/
    public static String formatTime(String pattern, long milli) {
        int m = (int) (milli / (60 * 1000));
        int s = (int) ((milli / 1000) % 60);
        String mm = String.format("%02d", m);
        String ss = String.format("%02d", s);
        return pattern.replace("mm", mm).replace("ss", ss);
    }

    /**
     * 保存歌词文件
     **/
    public static void saveLrcFile(String path, String content) {
        try {
            FileWriter writer = new FileWriter(path);
            writer.flush();
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * 判断文件是否存在
     *
     * @param strFile
     * @return
     */

    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }
}