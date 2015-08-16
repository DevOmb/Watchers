package com.ombrax.watchers.Utils;

/**
 * Created by Ombrax on 23/06/2015.
 */
public class StringUtils {

    //region method
    public static String preZeroInt(int value){
        return String.format((value > 9 ? "%d" : "0%d"), value);
    }
    //endregion
}
