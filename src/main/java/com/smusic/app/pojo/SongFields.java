package com.smusic.app.pojo;

/**
 * Created by sergey on 28.04.17.
 */
public enum SongFields {
    FILE_ID("file_id"), DURATION("duration"), SINGER("singer"), SONG_NAME("song"), LINK("link"), RATE("rate"), SIZE("size");
    public final String fieldName;

    SongFields(String fieldName) {
        this.fieldName = fieldName;
    }

    public static SongFields getValueFrom(String stringVal) {
        for (SongFields value : SongFields.values()) {
            if (value.fieldName.equals(stringVal)) {
                return value;
            }
        }
        return null;
    }

}
