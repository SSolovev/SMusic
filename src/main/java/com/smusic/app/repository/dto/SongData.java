package com.smusic.app.repository.dto;

import com.smusic.app.pojo.Song;
import com.smusic.app.utils.SongUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by sergey on 25.08.17.
 */
@Entity
@Table(name = "song_data")
public class SongData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "song_id")
    private long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "upload_date")
    private Date uploadDate;

    @Column(name = "path")
    private String path;

    @Column(name = "artist")
    private String artist;

    @Column(name = "song_name")
    private String songName;

    @Column(name = "opt_song_name")
    private String optimizedName;

    @Column(name = "fonetic_hash")
    private String foneticHash;

    @Column(name = "duration")
    private int duration;

    @Column(name = "quality")
    private String quality;

    @Column(name = "size")
    private String size;

    public SongData() {
    }

    public SongData(Song songObj, String path, String userName, Date date) {
        this.userName = userName;
        this.uploadDate = date;
        this.path = path;
        this.artist = SongUtils.normalizeFileName(songObj.getSinger());
        this.songName = SongUtils.normalizeFileName(songObj.getSongName());
        this.optimizedName = SongUtils.optimizeFileName(artist + songName);
        this.foneticHash = SongUtils.calculateSoundex(optimizedName);
        this.duration = songObj.getDuration();
        this.quality = songObj.getRate();
        this.size = songObj.getSize();

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getOptimizedName() {
        return optimizedName;
    }

    public void setOptimizedName(String optimizedName) {
        this.optimizedName = optimizedName;
    }

    public String getFoneticHash() {
        return foneticHash;
    }

    public void setFoneticHash(String foneticHash) {
        this.foneticHash = foneticHash;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "SongData{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", uploadDate=" + uploadDate +
                ", songName='" + songName + '\'' +
                ", optimizedName='" + optimizedName + '\'' +
                ", songPath='" + path + '\'' +
                ", duration=" + duration +
                ", quality='" + quality + '\'' +
                '}';
    }
}
