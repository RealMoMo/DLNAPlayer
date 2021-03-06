package com.geniusgithub.mediaplayer.main;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.geniusgithub.mediaplayer.R;
import com.geniusgithub.mediaplayer.base.BaseActivity;
import com.geniusgithub.mediaplayer.component.IToolBar;
import com.geniusgithub.mediaplayer.component.ToolEntry;
import com.geniusgithub.mediaplayer.browse.view.BrowserMediaFragment;
import com.geniusgithub.mediaplayer.util.AlwaysLog;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFrameActivity extends BaseActivity{

    public static final String TAG = MainFrameActivity.class.getSimpleName();

    private Context mContext;


    private View mRootView;
    private MainPresenter mMainPresenter;
    private MainContract.IView mMainView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainfram_layout);
        mContext = this;

        initData();
    }

    @Override
    protected void onDestroy(){
        mMainPresenter.onDestroy();
        super.onDestroy();

    }

    private void initData() {
        mRootView = findViewById(R.id.ll_root);

        mMainPresenter = new MainPresenter();
        mMainView = new MainView();
        mMainView.setupView(mRootView);
        mMainPresenter.bindView(mMainView);
        mMainPresenter.onCreate(this);



    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mMainView.toggleDrawLayout();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        boolean back = mMainView.onBackPressed();
        if (!back) {
            super.onBackPressed();
        }
    }







    private class MainFragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments;
        private List<String> mTitles;

        public MainFragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
            super(fm);
            mFragments = fragments;
            mTitles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }
    }





     class MainView implements  MainContract.IView, IToolBar, MainNavigationView.INavClickListener{

         private MainContract.IPresenter mPresenter;
         private View mRootView;

         @BindView(R.id.toolbar)
         Toolbar mToolbar;

         @BindView(R.id.dl_main_drawer)
         DrawerLayout mDrawerLayout;

         @BindView(R.id.tabs)
         TabLayout mTabLayout;

         @BindView(R.id.viewpager)
         ViewPager mViewPager;

         @BindView(R.id.nv_main_navigation)
          MainNavigationView mNavigationView;

         private ActionBarDrawerToggle mDrawerToggle;
         private TabLayout.Tab mTabLibrary;
         private BrowserMediaFragment mMediaServiceFragment;

         @Override
        public void bindPresenter(MainContract.IPresenter presenter) {
            mPresenter = presenter;
        }

        @Override
        public void setupView(View rootView) {
            mRootView = rootView;
            ButterKnife.bind(this, mRootView);
            initToolBar();
            initDrawLayout();
            setupViewPager();
        }

        @Override
        public void updateToolTitle(ToolEntry entry) {
            mToolbar.setTitle(entry.title);
            mToolbar.setSubtitle(entry.subTitle);
        }

         @Override
         public void updateLocalAddress(String value) {
            mNavigationView.updateLocalAddress(value);
         }

         @Override
         public void toggleDrawLayout() {
             if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                 mDrawerLayout.closeDrawers();
             } else {
                 mDrawerLayout.openDrawer(GravityCompat.START);
             }

         }

        @Override
        public boolean onBackPressed() {
            return mMediaServiceFragment.onBackPressed();
        }


        @Override
        public void onStartClick() {
            mPresenter.onStart();
        }

        @Override
        public void onRestartClick() {
            mPresenter.onRestart();
        }

        @Override
        public void onStopClick() {
            mPresenter.onStop();
        }

        @Override
        public void onExitClick() {
            mPresenter.onExit();
            finish();
        }


         private void initToolBar() {
            mToolbar.setTitle("DLNA");
            mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
            setSupportActionBar(mToolbar);


            final ActionBar ab = getSupportActionBar();
            ab.setDisplayHomeAsUpEnabled(true);


        }

        private void initDrawLayout() {

            mDrawerToggle = new ActionBarDrawerToggle(MainFrameActivity.this, mDrawerLayout, mToolbar, R.string.open, R.string.close) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);


                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);

                }
            };
            mDrawerToggle.syncState();
            mDrawerLayout.addDrawerListener(mDrawerToggle);


            if (mNavigationView != null) {
                mNavigationView.setmNavListener(this);
            }
        }


         private void setupViewPager() {
             List<String> titles = new ArrayList<String>();
             titles.add("LIBRARY");
             mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));

             mMediaServiceFragment = new BrowserMediaFragment();
             mMediaServiceFragment.bindToolbar(this);
             List<Fragment> fragments = new ArrayList<Fragment>();
             fragments.add(mMediaServiceFragment);

             MainFragmentAdapter adapter = new MainFragmentAdapter(getSupportFragmentManager(), fragments, titles);
             mViewPager.setAdapter(adapter);

             mTabLayout.setupWithViewPager(mViewPager);
             mTabLayout.setTabsFromPagerAdapter(adapter);
             mViewPager.setOffscreenPageLimit(mTabLayout.getTabCount());
         }




    }
}