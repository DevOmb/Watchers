package com.ombrax.watchers.Activities;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.ombrax.watchers.Controllers.DomainController;
import com.ombrax.watchers.Controllers.MenuController;
import com.ombrax.watchers.Manager.SettingsManager;
import com.ombrax.watchers.Manager.ToolbarManager;
import com.ombrax.watchers.Database.WatchersDatabase;
import com.ombrax.watchers.Enums.MenuItemType;
import com.ombrax.watchers.Enums.MenuType;
import com.ombrax.watchers.Fragments.WatchAddFragment;
import com.ombrax.watchers.Fragments.WatchListArchiveFragment;
import com.ombrax.watchers.Fragments.WatchListDefaultFragment;
import com.ombrax.watchers.Fragments.WatchSettingsFragment;
import com.ombrax.watchers.Interfaces.Handler.ISortMenuEnableHandler;
import com.ombrax.watchers.Interfaces.Listener.IOnMenuItemClickListener;
import com.ombrax.watchers.Interfaces.Listener.IOnListItemEditListener;
import com.ombrax.watchers.Interfaces.Handler.IMenuCloseHandler;
import com.ombrax.watchers.Models.WatchModel;
import com.ombrax.watchers.R;
import com.ombrax.watchers.Views.Menu.MainMenuView;
import com.ombrax.watchers.Views.Menu.MenuView;
import com.ombrax.watchers.Views.Menu.SortMenuView;

public class MainActivity extends AppCompatActivity implements IOnListItemEditListener<WatchModel>, IOnMenuItemClickListener, IMenuCloseHandler, ISortMenuEnableHandler {

    //region declaration
    //region controller
    private DomainController dc;
    private MenuController mc;
    //endregion

    //region inner field
    private boolean isSecondaryMenuShowing;
    private MenuItem sortMenuItem;
    //endregion

    //region view
    private FloatingActionButton addButton;
    private SlidingMenu menuDrawer;
    private MainMenuView mainMenu;
    private SortMenuView sortMenu;
    //endregion
    //endregion

    //region create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        addButton = (FloatingActionButton) findViewById(R.id.fab);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(new WatchAddFragment());
            }
        });

        switchFragment(new WatchListDefaultFragment());
    }
    //endregion

    //region menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        sortMenuItem = menu.findItem(R.id.action_sort);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                menuDrawer.showMenu();
                return true;
            case R.id.action_sort:
                menuDrawer.showSecondaryMenu();
                return true;
        }
        return false;
    }
    //endregion

    //region setup
    private void init() {
        WatchersDatabase.initialize(this);
        controllerSetup();
        menuSetup();
        toolbarSetup();
        SettingsManager.getInstance().renew();
    }

    private void controllerSetup() {
        dc = DomainController.getInstance();
        dc.registerOnListItemEditObserver(this);

        mc = MenuController.getInstance();
        mc.setOnMainMenuItemClickListener(this);
        mc.setMenuCloseHandler(this);
        mc.setSortMenuEnableHandler(this);
    }
    //endregion

    //region fragment
    //TODO animate transaction (slide up)
    private void switchFragment(Fragment fragment) {
        //Check if same Fragment is present at top of BackStack
        if (topBackStackEntry(fragment)) {
            getSupportFragmentManager().popBackStackImmediate();
            return;
        }

        //Check if Fragment already exists
        Fragment existingFragment = getSupportFragmentManager().findFragmentByTag(getNameForFragment(fragment));
        if (existingFragment == null) {
            existingFragment = fragment;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, existingFragment, getNameForFragment(existingFragment));
        //Check all rules before adding to BackStack
        if (validForBackStack()) {
            transaction.addToBackStack(getNameForFragment(null));
        }
        transaction.commit();
    }

    private boolean topBackStackEntry(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();

        //Initial stage of the Activity
        if (manager.getBackStackEntryCount() == 0) {
            return false;
        }

        FragmentManager.BackStackEntry topEntry = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1);
        String entryName = topEntry.getName();

        //No two classes have the same classname, ensures that a match is always correct
        return entryName.equals(getNameForFragment(fragment));
    }

    private String getNameForFragment(Fragment fragment) {
        if (fragment == null) {
            //Get the currently displayed Fragment
            fragment = getSupportFragmentManager().findFragmentById(R.id.container);
            //If null, it means we are at the initial stage of the Activity
            if (fragment == null) {
                return null;
            }
        }
        //Every class can be differentiated by its unique classname
        return fragment.getClass().getSimpleName();
    }

    private boolean validForBackStack() {
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.container);
        //1. No Fragment present, can't be added to BackStack
        boolean rule_1 = current != null;
        //2. Ignore all instances of WatchAddFragment
        boolean rule_2 = !(current instanceof WatchAddFragment);
        return rule_1 && rule_2;
    }
    //endregion

    //region helper
    private void toolbarSetup() {
        ToolbarManager toolbarManager = ToolbarManager.getInstance();
        toolbarManager.initialize(
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar),
                (AppBarLayout) findViewById(R.id.appbar_layout),
                (Toolbar) findViewById(R.id.toolbar)
        );
        toolbarManager.setExpandingTitle(getString(R.string.app_name));
        toolbarManager.setMainActionItemDrawable(R.drawable.ic_menu);
        toolbarManager.attachToActivity(this);
    }

    private void menuSetup() {
        mainMenu = new MainMenuView(this);
        mainMenu.setMenuItemEnabled(MenuItemType.EDIT, false);
        sortMenu = new SortMenuView(this);

        menuDrawer = new SlidingMenu(this, SlidingMenu.SLIDING_CONTENT);
        menuDrawer.setBackgroundResource(MenuView.BACKGROUND);
        menuDrawer.setMode(SlidingMenu.LEFT_RIGHT);
        menuDrawer.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menuDrawer.setMenu(mainMenu);
        menuDrawer.setSecondaryMenu(sortMenu);
        menuDrawer.setBehindWidthRes(R.dimen.sliding_menu_width);
        menuDrawer.setFadeDegree(0.35f);
        menuDrawer.setSecondaryOnOpenListner(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                isSecondaryMenuShowing = true;
            }
        });
        menuDrawer.setOnClosedListener(new SlidingMenu.OnClosedListener() {
            @Override
            public void onClosed() {
                if (isSecondaryMenuShowing) {
                    isSecondaryMenuShowing = false;
                    mc.onSortMenuClosed();
                }
            }
        });
    }
    //endregion

    //region override
    @Override
    public void onBackPressed() {
        if (menuDrawer.isMenuShowing()) {
            menuDrawer.showContent();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        dc.unregisterOnListItemEditObserver(this);
        super.finish();
    }
    //endregion

    //region interface implementation
    @Override
    public void onListItemEdit(WatchModel watchModel) {
        switchFragment(WatchAddFragment.editInstance(watchModel));
    }

    @Override
    public void onMenuItemClick(MenuItemType menuItemType) {
        if (menuItemType == MenuItemType.EXIT) {
            this.finish();
            return;
        }
        if (menuItemType.childOf(MenuType.MAIN)) {
            if (mainMenu.hasNewSelection(menuItemType)) {
                switch (menuItemType) {
                    case HOME:
                        switchFragment(new WatchListDefaultFragment());
                        break;
                    case ADD:
                        switchFragment(new WatchAddFragment());
                        break;
                    case ARCHIVE:
                        switchFragment(new WatchListArchiveFragment());
                        break;
                    case SETTINGS:
                        switchFragment(new WatchSettingsFragment());
                        break;
                }
                menuDrawer.toggle();
            }
        }
    }

    @Override
    public void handleSortMenuEnable(boolean enable) {
        menuDrawer.setMode(enable ? SlidingMenu.LEFT_RIGHT : SlidingMenu.LEFT);
        if (sortMenuItem != null) {
            sortMenuItem.setVisible(enable);
        }
    }

    @Override
    public void closeMenu() {
        menuDrawer.toggle();
    }

    //endregion
}
