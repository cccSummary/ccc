package project.example.com.mymusicproject.model;

import java.util.List;

/**
 * Created by Administrator on 2016/7/7.
 */
public class SearchData {

    /**
     * songCount : 1
     * songs : [{"album":{"status":1,"copyrightId":0,"name":"神的游戏","artist":{"alias":[],"picUrl":null,"id":0,"name":""},"publishTime":1344528000000,"id":32311,"size":10},"status":1,"copyrightId":0,"name":"玫瑰色的你","mvid":5102,"alias":[],"artists":[{"alias":[],"picUrl":null,"id":10557,"name":"张悬"}],"duration":297927,"id":326695}]
     */

    private int songCount;
    /**
     * album : {"status":1,"copyrightId":0,"name":"神的游戏","artist":{"alias":[],"picUrl":null,"id":0,"name":""},"publishTime":1344528000000,"id":32311,"size":10}
     * status : 1
     * copyrightId : 0
     * name : 玫瑰色的你
     * mvid : 5102
     * alias : []
     * artists : [{"alias":[],"picUrl":null,"id":10557,"name":"张悬"}]
     * duration : 297927
     * id : 326695
     */

    private List<SongsBean> songs;

    public int getSongCount() {
        return songCount;
    }

    public void setSongCount(int songCount) {
        this.songCount = songCount;
    }

    public List<SongsBean> getSongs() {
        return songs;
    }

    public void setSongs(List<SongsBean> songs) {
        this.songs = songs;
    }

    public static class SongsBean {
        /**
         * status : 1
         * copyrightId : 0
         * name : 神的游戏
         * artist : {"alias":[],"picUrl":null,"id":0,"name":""}
         * publishTime : 1344528000000
         * id : 32311
         * size : 10
         */

        private AlbumBean album;
        private int status;
        private int copyrightId;
        private String name;
        private int mvid;
        private int duration;
        private int id;
        private List<?> alias;
        /**
         * alias : []
         * picUrl : null
         * id : 10557
         * name : 张悬
         */

        private List<ArtistsBean> artists;

        public AlbumBean getAlbum() {
            return album;
        }

        public void setAlbum(AlbumBean album) {
            this.album = album;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getCopyrightId() {
            return copyrightId;
        }

        public void setCopyrightId(int copyrightId) {
            this.copyrightId = copyrightId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getMvid() {
            return mvid;
        }

        public void setMvid(int mvid) {
            this.mvid = mvid;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<?> getAlias() {
            return alias;
        }

        public void setAlias(List<?> alias) {
            this.alias = alias;
        }

        public List<ArtistsBean> getArtists() {
            return artists;
        }

        public void setArtists(List<ArtistsBean> artists) {
            this.artists = artists;
        }

        public static class AlbumBean {
            private int status;
            private int copyrightId;
            private String name;
            /**
             * alias : []
             * picUrl : null
             * id : 0
             * name :
             */

            private ArtistBean artist;
            private long publishTime;
            private int id;
            private int size;

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getCopyrightId() {
                return copyrightId;
            }

            public void setCopyrightId(int copyrightId) {
                this.copyrightId = copyrightId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public ArtistBean getArtist() {
                return artist;
            }

            public void setArtist(ArtistBean artist) {
                this.artist = artist;
            }

            public long getPublishTime() {
                return publishTime;
            }

            public void setPublishTime(long publishTime) {
                this.publishTime = publishTime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public static class ArtistBean {
                private Object picUrl;
                private int id;
                private String name;
                private List<?> alias;

                public Object getPicUrl() {
                    return picUrl;
                }

                public void setPicUrl(Object picUrl) {
                    this.picUrl = picUrl;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public List<?> getAlias() {
                    return alias;
                }

                public void setAlias(List<?> alias) {
                    this.alias = alias;
                }
            }
        }

        public static class ArtistsBean {
            private Object picUrl;
            private int id;
            private String name;
            private List<?> alias;

            public Object getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(Object picUrl) {
                this.picUrl = picUrl;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<?> getAlias() {
                return alias;
            }

            public void setAlias(List<?> alias) {
                this.alias = alias;
            }
        }
    }
}
