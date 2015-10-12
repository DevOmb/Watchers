package com.ombrax.watchers.Manager;

import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by Ombrax on 14/09/2015.
 */
public class FragmentManager {

    //region declaration
    //region inner field
    private Deque<Fragment> backStack;
    private boolean initialized;
    private boolean codeEnabled;
    //endregion

    //region variable
    private android.support.v4.app.FragmentManager internalFragmentManager;
    private int containerId;
    private BackStackRule backStackRule;
    //endregion
    //endregion

    //region singleton
    private static FragmentManager instance = new FragmentManager();

    public static FragmentManager getInstance() {
        return instance;
    }

    private FragmentManager() {
    }
    //endregion

    //region method
    public void initialize(android.support.v4.app.FragmentManager internalFragmentManager, int containerId) {
        this.internalFragmentManager = internalFragmentManager;
        this.containerId = containerId;
        initialized = true;
        renew();
    }

    public void renew() {
        backStack = new ArrayDeque<>();
    }

    public void popBackStack() {
        checkInit();
        if (!isBackStackEmpty()) {
            codeEnabled = true;
            showFragment(backStack.pop());
            if (DEBUG) {
                printBackStack();
            }
        }
    }

    public void clearBackStack() {
        clearBackStack(true);
    }

    public void clearBackStack(boolean popInclusive) {
        checkInit();
        if(DEBUG){
            Log.d(TAG, "Clearing BackStack ("+getBackStackEntryCount()+")");
        }
        if (!isBackStackEmpty() && popInclusive) {
            codeEnabled = true;
            Fragment initialFragment = backStack.peekLast();
            showFragment(initialFragment);
            if(DEBUG){
                Log.d(TAG, "Showing initial Fragment ["+initialFragment+"]");
            }
        }
        backStack.clear();
    }

    public void showFragment(Fragment fragment) {
        checkInit();
        if (!codeEnabled) {
            if(areEqual(getTopEntry(), fragment)){
                popBackStack();
                return;
            }
            handleAddBackStack();
        }
        codeEnabled = false;
        internalFragmentManager.beginTransaction().replace(containerId, fragment).commit();
    }

    public void setGeneralBackStackRule(BackStackRule backStackRule) {
        this.backStackRule = backStackRule;
    }

    public int getBackStackEntryCount() {
        return backStack.size();
    }

    public boolean isBackStackEmpty() {
        return backStack.isEmpty();
    }

    public Fragment getTopEntry() {
        return backStack.peek();
    }

    public boolean areEqual(Fragment f1, Fragment f2) {
        return (f1 == null || f2 == null) ? false : getFragmentIdentifier(f1).equals(getFragmentIdentifier(f2));
    }
    //endregion

    //region helper
    private void checkInit() {
        if (!initialized) {
            throw new NullPointerException("FragmentManager not initialized");
        }
    }

    private String getFragmentIdentifier(Fragment fragment) {
        return fragment == null ? null : fragment.getClass().getSimpleName();
    }

    private Fragment getCurrentFragment() {
        return internalFragmentManager.findFragmentById(containerId);
    }

    private void handleAddBackStack() {
        if (backStackRule != null) {
            Fragment current = getCurrentFragment();
            if (current != null && backStackRule.addToBackStack(current)) {
                backStack.push(current);
                if (DEBUG) {
                    printBackStack();
                }
            } else {
                if (DEBUG) {
                    Log.d(TAG, "[" + getFragmentIdentifier(current) + "] Evaluated as not valid for BackStack");
                }
            }
        }
    }
    //endregion

    //region debugging
    private static boolean DEBUG = false;
    private static final String TAG = "FRAGMENT_MANAGER";

    public static void enableDebugging(boolean debug) {
        DEBUG = debug;
    }

    private void printBackStack() {
        Log.d(TAG, "Printing BackStack content ... (" + getBackStackEntryCount() + ")");
        Log.d(TAG, "TOP");
        for (Fragment fragment : backStack) {
            Log.d(TAG, "  " + getFragmentIdentifier(fragment));
        }
        Log.d(TAG, "BOTTOM");
    }
    //endregion

    //region interface
    public interface BackStackRule {
        boolean addToBackStack(Fragment current);
    }
    //endregion
}

