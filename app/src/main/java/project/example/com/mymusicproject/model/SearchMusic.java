package project.example.com.mymusicproject.model;

import java.util.List;

/**
 * JavaBean 搜索歌词
 */
public class SearchMusic {
    private List<JSong> song;

    public List<JSong> getSong() {
        return song;
    }

    public void setSong(List<JSong> song) {
        this.song = song;
    }

    public static class JSong {
        String songname;
        String artistname;
        String songid;

        public String getSongname() {
            return songname;
        }

        public void setSongname(String songname) {
            this.songname = songname;
        }

        public String getArtistname() {
            return artistname;
        }

        public void setArtistname(String artistname) {
            this.artistname = artistname;
        }

        public String getSongid() {
            return songid;
        }

        public void setSongid(String songid) {
            this.songid = songid;
        }
    }
}