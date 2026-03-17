package com.team900.lib.util;
import java.util.List;

public class HubFlipUtil {
    public static boolean isFlip(long time) {
        List<Long> flipTime = List.of(17L, 27L, 52L, 77L, 102L, 127L);
        return flipTime.contains((Long)(time));
    }
}
