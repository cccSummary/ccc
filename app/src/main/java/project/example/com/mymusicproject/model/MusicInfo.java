package project.example.com.mymusicproject.model;

import java.io.Serializable;

import project.example.com.mymusicproject.Constants;

/**
 * Created by Administrator on 2016/6/21.
 * 实体bean
 */
public class MusicInfo implements Serializable {
    private long id;
    private String title;
    private String album;
    private long albumId;
    private int duration;
    private long size;
    private String artist;
    private String url;
    private boolean isSelect;//是否播放

    public String getFileName() {
        return fileName;
    }

    public void setFileName() {
        this.fileName = title + Constants.FILENAME_MP3;
    }

    // 文件名
    private String fileName;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    // 歌曲类型 本地/网络
    private Type type;

    /**
     * 对比本地歌曲是否相同
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MusicInfo)) {
            return false;
        }
        return this.getId() == ((MusicInfo) o).getId();
    }

    public enum Type {
        LOCAL,
        ONLINE
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public MusicInfo() {

    }

    public MusicInfo(long pId, String pTitle) {
        id = pId;
        title = pTitle;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
