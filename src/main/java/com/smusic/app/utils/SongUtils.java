package com.smusic.app.utils;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by sergey on 26.08.17.
 */
public class SongUtils {
    private static final String DEF_FILENAME = "defaultName";
    public static String[][] NORMILIZE_ARR = {{"[aeiouyhw]", ""}, {"[bfpv]", "1"}, {"[cgjkqsxz]", "2"}, {"[dt]", "3"}, {"[l]", "4"}, {"[mn]", "5"}, {"[r]", "6"}};


    public static String normalizeFileName(String filename) {
        String filenameNorm = filename.replaceAll("&#quote;", " ")
                .replaceAll("&#039;", " ")
                .replaceAll("[$#@&;{}\\[\\]\\']", "");
        if (!filenameNorm.isEmpty()) {
            return filenameNorm;
        } else {
            return DEF_FILENAME;
        }
    }

    public static String optimizeFileName(String filename) {
        return filename.replaceAll("[^\\wа-яА-Я]", "");
    }


    public static String calculateSoundex(String str) {

        String firstChar = "";
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                firstChar += str.charAt(i);
                break;
            }
        }

        return firstChar + Arrays.stream(Arrays.stream(NORMILIZE_ARR)
                .map(toRem -> (Function<String, String>) s -> s.replaceAll(toRem[0], toRem[1]))
                .reduce(Function.identity(), Function::andThen)
                .apply(str.substring(1, str.length())).split(""))
                .distinct()
                .collect(Collectors.joining()).substring(0, 3);


    }
}
