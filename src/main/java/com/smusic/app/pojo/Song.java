package com.smusic.app.pojo;

/**
 * Created by sergey on 28.04.17.
 */
public class Song {
    private String id;
    private Integer duration;
    private String singer;
    private String songName;
    private String link;
    private String rate;
    private String size;

    public Song(String id, Integer duration, String singer, String songName, String link, String rate, String size) {
        this.id = id;
        this.duration = duration;
        this.singer = singer;
        this.songName = songName;
        this.link = link;
        this.rate = rate;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getSinger() {
        return singer;
    }

    public String getSongName() {
        return songName;
    }

    public String getLink() {
        return link;
    }

    public String getRate() {
        return rate;
    }

    public String getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Song song = (Song) o;

        if (id != null ? !id.equals(song.id) : song.id != null) return false;
        if (duration != null ? !duration.equals(song.duration) : song.duration != null) return false;
        if (singer != null ? !singer.equals(song.singer) : song.singer != null) return false;
        if (songName != null ? !songName.equals(song.songName) : song.songName != null) return false;
        if (link != null ? !link.equals(song.link) : song.link != null) return false;
        if (rate != null ? !rate.equals(song.rate) : song.rate != null) return false;
        return size != null ? size.equals(song.size) : song.size == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (singer != null ? singer.hashCode() : 0);
        result = 31 * result + (songName != null ? songName.hashCode() : 0);
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + (rate != null ? rate.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id='" + id + '\'' +
                ", duration=" + duration +
                ", singer='" + singer + '\'' +
                ", songName='" + songName + '\'' +
                ", link='" + link + '\'' +
                ", rate='" + rate + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
