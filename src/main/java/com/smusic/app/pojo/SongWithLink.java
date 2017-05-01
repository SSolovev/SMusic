package com.smusic.app.pojo;

/**
 * Created by sergey on 01.05.17.
 */
public class SongWithLink {
    private String urlSong;
    private Song song;

    public SongWithLink() {
    }

    public SongWithLink(String urlSong, Song song) {
        this.urlSong = urlSong;
        this.song = song;
    }

    public String getUrlSong() {
        return urlSong;
    }

    public void setUrlSong(String urlSong) {
        this.urlSong = urlSong;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SongWithLink that = (SongWithLink) o;

        if (urlSong != null ? !urlSong.equals(that.urlSong) : that.urlSong != null) return false;
        return song != null ? song.equals(that.song) : that.song == null;
    }

    @Override
    public int hashCode() {
        int result = urlSong != null ? urlSong.hashCode() : 0;
        result = 31 * result + (song != null ? song.hashCode() : 0);
        return result;
    }
}
