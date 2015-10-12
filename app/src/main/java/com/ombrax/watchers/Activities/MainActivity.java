package com.ombrax.watchers.Activities;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.ombrax.watchers.Controllers.DomainController;
import com.ombrax.watchers.Controllers.MenuController;
import com.ombrax.watchers.Interfaces.Handler.IWatchCardAddFinishedHandler;
import com.ombrax.watchers.Manager.FragmentManager;
import com.ombrax.watchers.Manager.SettingsManager;
import com.ombrax.watchers.Manager.ToolbarManager;
import com.ombrax.watchers.Database.WatchersDatabase;
import com.ombrax.watchers.Enums.MenuItemType;
import com.ombrax.watchers.Enums.MenuType;
import com.ombrax.watchers.Fragments.WatchAddFragment;
import com.ombrax.watchers.Fragments.WatchListArchiveFragment;
import com.ombrax.watchers.Fragments.WatchListMainFragment;
import com.ombrax.watchers.Fragments.WatchSettingsFragment;
import com.ombrax.watchers.Interfaces.Handler.IMenuStateHandler;
import com.ombrax.watchers.Interfaces.Listener.IOnMenuItemClickListener;
import com.ombrax.watchers.Interfaces.Listener.IOnListItemEditListener;
import com.ombrax.watchers.Interfaces.Handler.IMenuCloseHandler;
import com.ombrax.watchers.Models.WatchModel;
import com.ombrax.watchers.R;
import com.ombrax.watchers.Views.Menu.MainMenuView;
import com.ombrax.watchers.Views.Menu.MenuView;
import com.ombrax.watchers.Views.Menu.SortMenuView;

public class MainActivity extends AppCompatActivity implements IOnListItemEditListener<WatchModel>, IWatchCardAddFinishedHandler, IOnMenuItemClickListener, IMenuCloseHandler, IMenuStateHandler {

    //region declaration
    //region inner field
    private DomainController dc;
    private MenuController mc;
    private FragmentManager fragmentManager;
    private ToolbarManager toolbarManager;

    private boolean isSecondaryMenuShowing;
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
                showFragment(new WatchAddFragment());
            }
        });

        showHomeFragment();
    }
    //endregion

    //region menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                menuDrawer.toggle();
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
        fragmentSetup();
        menuSetup();
        toolbarSetup();
        SettingsManager.getInstance().renew();
    }
    //endregion

    //region setup helper
    private void controllerSetup() {
        dc = DomainController.getInstance();
        dc.registerOnListItemEditObserver(this);
        dc.setWatchCardAddFinishedHandler(this);

        mc = MenuController.getInstance();
        mc.setOnMainMenuItemClickListener(this);
        mc.setMenuCloseHandler(this);
        mc.setMenuStateHandler(this);
    }

    private void fragmentSetup() {
        //FragmentManager.enableDebugging(true);
        fragmentManager = FragmentManager.getInstance();
        fragmentManager.initialize(getSupportFragmentManager(), R.id.container);
        fragmentManager.setGeneralBackStackRule(new FragmentManager.BackStackRule() {
            @Override
            public boolean addToBackStack(Fragment current) {
                return !(current instanceof WatchAddFragment) && !fragmentManager.areEqual(fragmentManager.getTopEntry(), current);
            }
        });
    }

    private void toolbarSetup() {
        toolbarManager = ToolbarManager.getInstance();
        toolbarManager.initialize(
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar),
                (AppBarLayout) findViewById(R.id.appbar_layout),
                (Toolbar) findViewById(R.id.toolbar)
        );
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

    //region fragment
    private void showHomeFragment() {
        if (!fragmentManager.isBackStackEmpty()) {
            fragmentManager.clearBackStack();
        } else {
            fragmentManager.showFragment(new WatchListMainFragment());
        }
    }

    private void showFragment(Fragment fragment) {
        fragmentManager.showFragment(fragment);
    }
    //endregion

    //region override
    @Override
    public void onBackPressed() {
        if (menuDrawer.isMenuShowing()) {
            menuDrawer.showContent();
        } else if (!fragmentManager.isBackStackEmpty()) {
            fragmentManager.popBackStack();
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
        showFragment(WatchAddFragment.editInstance(watchModel));
    }

    @Override
    public void handleWatchCardAddFinished() {
        showHomeFragment();
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
                        showHomeFragment();
                        break;
                    case ADD:
                        showFragment(new WatchAddFragment());
                        break;
                    case ARCHIVE:
                        showFragment(new WatchListArchiveFragment());
                        break;
                    case SETTINGS:
                        showFragment(new WatchSettingsFragment());
                        break;
                }
                menuDrawer.toggle();
            }
        }
    }

    @Override
    public void setMenuFlags(int flags) {
        if((flags & ENABLE_ALL) == ENABLE_ALL){
            menuDrawer.setMode(SlidingMenu.LEFT_RIGHT);
            menuDrawer.setSlidingEnabled(true);
            toolbarManager.setToolbarItemVisibility(R.id.action_sort, true);
        }
        if((flags & DISABLE_SECONDARY_MENU) == DISABLE_SECONDARY_MENU){
            menuDrawer.setMode(SlidingMenu.LEFT);
            toolbarManager.setToolbarItemVisibility(R.id.action_sort, false);
        }
        if((flags & DISABLE_SWIPE) == DISABLE_SWIPE){
            menuDrawer.setSlidingEnabled(false);
        }
        if((flags & ENABLE_SWIPE) == ENABLE_SWIPE){
            menuDrawer.setSlidingEnabled(true);
        }
    }

    @Override
    public void closeMenu() {
        menuDrawer.toggle();
    }

    //endregion
}
