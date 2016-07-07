package project.example.com.mymusicproject;

/**
 * @auther ccc
 * created at 2016/6/30 16:54
 ***/

public class Constants {

    /**
     * 参数
     */
    public static final String PARAM_DATA = "param_data";
    public static final String ACTION_MEDIA_PLAY_PAUSE = "project.example.com.mymusicproject.ACTION_MEDIA_PLAY_PAUSE";
    public static final String ACTION_MEDIA_NEXT = "project.example.com.mymusicproject.ACTION_MEDIA_NEXT";
    public static final String ACTION_MEDIA_PREVIOUS = "project.example.com.mymusicproject.ACTION_MEDIA_PREVIOUS";
    public static final String FROM_NOTIFICATION = "from_notification";

    public static final String FILENAME_MP3 = ".mp3";
    public static final String FILENAME_LRC = ".lrc";
    public static final String BASE_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting";
    public static final String METHOD_GET_MUSIC_LIST = "baidu.ting.billboard.billList";
    public static final String METHOD_DOWNLOAD_MUSIC = "baidu.ting.song.play";
    public static final String METHOD_ARTIST_INFO = "baidu.ting.artist.getInfo";//作者信息
    public static final String METHOD_SEARCH_MUSIC = "baidu.ting.search.catalogSug";//搜索歌曲
    public static final String METHOD_LRC = "baidu.ting.song.lry";//下载歌词
    public static final String PARAM_METHOD = "method";
    public static final String PARAM_SONG_ID = "songid";
    public static final String PARAM_QUERY = "query";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_SIZE = "size";
    public static final String PARAM_OFFSET = "offset";
}
