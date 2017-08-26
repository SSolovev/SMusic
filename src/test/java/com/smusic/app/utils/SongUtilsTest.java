package com.smusic.app.utils;

import junit.framework.TestCase;

/**
 * Created by sergey on 26.08.17.
 */
public class SongUtilsTest extends TestCase {

    public void testOptimizeFileName(){
        assertEquals("TSTakaБраTSTвоКофесигаретыmp3", SongUtils.optimizeFileName("★TST aka БраTSTво - Кофе,сигареты★.mp3"));
    }
}
