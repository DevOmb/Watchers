package com.ombrax.watchers.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ombrax on 23/06/2015.
 */
public class DatabaseUtils {

    //region constant
    private static final String SEPARATOR = ",";
    private static final SimpleDateFormat DTF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //endregion

    //region format
    //region persistence format
    public static String formatIntListToString(List<Integer> list) {
        if(list == null){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Integer i : list) {
            sb.append(i).append(SEPARATOR);
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static String formatDateToString(Date date) {
        return date == null ? null : DTF.format(date);
    }

    public static int formatBooleanToInteger(boolean value) {
        return value ? 1 : 0;
    }
    //endregion

    //region workable format
    public static List<Integer> formatStringToIntList(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        List<Integer> data = new ArrayList<>();
        String[] splitStringData = string.split(SEPARATOR);
        for (String s : splitStringData) {
            Integer intItem = Integer.parseInt(s);
            data.add(intItem);
        }
        return data;
    }

    public static boolean formatIntegerToBoolean(int i) {
        switch (i) {
            case 0:
                return false;
            case 1:
                return true;
            default:
                throw new ClassCastException("Can't cast Integer value (" + i + ") to Boolean. Only 0 and 1 are allowed");
        }
    }

    public static Date formatStringToDate(String string) {
        if(string == null || string.isEmpty()){
            return null;
        }
        try {
            return DTF.parse(string);
        } catch (NullPointerException|ParseException e) {
            System.out.println("Failed to parse String " + string + " to Date, returning null.");
        }
        return null;
    }
    //endregion
    //endregion

    //region query args
    public static <T> String[] asArgs(T... objects) {
        List<String> varArgs = new ArrayList<>();
        for (T obj : objects) {
            if (obj instanceof Integer) {
                varArgs.add(Integer.toString((Integer) obj));
            } else if (obj instanceof Boolean) {
                varArgs.add(Integer.toString(formatBooleanToInteger((Boolean) obj)));
            } else if (obj instanceof String) {
                varArgs.add((String) obj);
            } else if (obj instanceof Date) {
                varArgs.add(formatDateToString((Date) obj));
            } else {
                throw new UnsupportedOperationException("Illegal operation: Cast " + obj.getClass() + " to String");
            }
        }
        return varArgs.toArray(new String[objects.length]);
    }
    //endregion
}

