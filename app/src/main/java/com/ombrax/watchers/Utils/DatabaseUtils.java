package com.ombrax.watchers.Utils;

import android.util.Log;

import com.ombrax.watchers.Enums.MenuType;
import com.ombrax.watchers.Models.SortModel;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.IllegalFormatException;
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

    public static String formatSortModelToString(SortModel sortModel){
        int ordinal = MenuType.SORT.children().indexOf(sortModel.getSortType());
        int order = formatBooleanToInteger(sortModel.isAscending());
        return String.format("%d%d", ordinal, order);
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
            Log.d("FORMAT", "Failed to parse String " + string + " to Date, returning null.");
        }
        return null;
    }

    public static SortModel formatStringToSortModel(String string){
        if(string.length() == 2) {
            int ordinal = Integer.parseInt(string.substring(0,1));
            boolean isAscending = formatIntegerToBoolean(Integer.parseInt(string.substring(1)));
            return new SortModel(MenuType.SORT.children().get(ordinal), isAscending);
        }else{
            throw new IllegalArgumentException("The provided String is not the proper format");
        }
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

