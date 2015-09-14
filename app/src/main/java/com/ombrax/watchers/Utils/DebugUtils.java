package com.ombrax.watchers.Utils;

import android.app.FragmentManager;

/**
 * Created by Ombrax on 9/09/2015.
 */
public class DebugUtils {

    public static void printBackStack(FragmentManager fragmentManager) {
        int entryCount = fragmentManager.getBackStackEntryCount();
        if (entryCount > 0) {
            System.out.println("BACKSTACK: (" + entryCount + ")");
            for (int i = 0; i < entryCount; i++) {
                System.out.println((i + 1) + ". " + fragmentManager.getBackStackEntryAt(i).getName());
            }
        } else {
            System.out.println("BACKSTACK: Empty (0)");
        }
    }
}
