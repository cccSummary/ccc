package project.example.com.mymusicproject.model;

import java.io.Serializable;

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
    private  boolean isSelect;//是否播放

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public MusicInfo(){

    }

    public MusicInfo(long pId, String pTitle){
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
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }

//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeLong(id);
//        dest.writeString(title);
//        dest.writeString(album);
//        dest.writeLong(albumId);
//        dest.writeString(artist);
//        dest.writeString(url);
//        dest.writeInt(duration);
//        dest.writeLong(size);
//        Log.v("","shit"+albumId);
//
//    }
//
//    public static final Creator<MusicInfo>
//            CREATOR = new Creator<MusicInfo>() {
//
//        @Override
//        public MusicInfo[] newArray(int size) {
//            return new MusicInfo[size];
//        }
//
//        @Override
//        public MusicInfo createFromParcel(Parcel source) {
//            MusicInfo musicInfo = new MusicInfo();
//            musicInfo.setId(source.readLong());
//            musicInfo.setTitle(source.readString());
//            musicInfo.setAlbum(source.readString());
//            musicInfo.setAlbumId(source.readLong());
//            musicInfo.setArtist(source.readString());
//            musicInfo.setUrl(source.readString());
//            musicInfo.setDuration(source.readInt());
//            musicInfo.setSize(source.readLong());
//            return musicInfo;
//
//        }
//    };
}
