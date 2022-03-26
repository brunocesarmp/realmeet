package br.com.sw2you.realmeet.util;

import java.util.List;

public final class StringUtils {

    private StringUtils() {
    }

    public static String join(List<String> list) {
        return String.join(",", list);
    }

}
