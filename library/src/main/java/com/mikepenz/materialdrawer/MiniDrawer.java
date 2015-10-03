package com.mikepenz.materialdrawer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mikepenz.materialdrawer.adapter.BaseDrawerAdapter;
import com.mikepenz.materialdrawer.adapter.DrawerAdapter;
import com.mikepenz.materialdrawer.interfaces.ICrossfader;
import com.mikepenz.materialdrawer.model.MiniDrawerItem;
import com.mikepenz.materialdrawer.model.MiniProfileDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialize.util.UIUtils;

import java.util.ArrayList;

/**
 * Created by mikepenz on 15.07.15.
 * Don't count this for real yet. it's just a quick try on creating a Gmail like panel
 */
public class MiniDrawer {
    private LinearLayout mContainer;
    private RecyclerView mRecyclerView;
    private DrawerAdapter mDrawerAdapter;

    private Drawer mDrawer;

    public MiniDrawer withDrawer(@NonNull Drawer drawer) {
        this.mDrawer = drawer;
        return this;
    }

    private AccountHeader mAccountHeader;

    public MiniDrawer withAccountHeader(@NonNull AccountHeader accountHeader) {
        this.mAccountHeader = accountHeader;
        return this;
    }

    private ICrossfader mCrossFader;

    public MiniDrawer withCrossFader(@NonNull ICrossfader crossFader) {
        this.mCrossFader = crossFader;
        return this;
    }

    private boolean mInnerShadow = false;

    public MiniDrawer withInnerShadow(boolean innerShadow) {
        this.mInnerShadow = innerShadow;
        return this;
    }

    private boolean mInRTL = false;

    public MiniDrawer withInRTL(boolean inRTL) {
        this.mInRTL = inRTL;
        return this;
    }


    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public DrawerAdapter getDrawerAdapter() {
        return mDrawerAdapter;
    }

    public Drawer getDrawer() {
        return mDrawer;
    }

    public AccountHeader getAccountHeader() {
        return mAccountHeader;
    }

    public ICrossfader getCrossFader() {
        return mCrossFader;
    }

    /**
     * build the MiniDrawer
     *
     * @param ctx
     * @return
     */
    public View build(Context ctx) {
        mContainer = new LinearLayout(ctx);
        if (mInnerShadow) {
            if (!mInRTL) {
                mContainer.setBackgroundResource(R.drawable.material_drawer_shadow_left);
            } else {
                mContainer.setBackgroundResource(R.drawable.material_drawer_shadow_right);
            }
        }

        //create and append recyclerView
        mRecyclerView = new RecyclerView(ctx);
        mContainer.addView(mRecyclerView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //set the itemAnimator
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //some style improvements on older devices
        mRecyclerView.setFadingEdgeLength(0);
        //set the drawing cache background to the same color as the slider to improve performance
        //mRecyclerView.setDrawingCacheBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(mActivity, R.attr.material_drawer_background, R.color.material_drawer_background));
        mRecyclerView.setClipToPadding(false);
        //additional stuff
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        //adapter
        mDrawerAdapter = new DrawerAdapter();
        mRecyclerView.setAdapter(mDrawerAdapter);

        //if the activity with the drawer should be fullscreen add the padding for the navigationBar
        if (mDrawer != null && mDrawer.mDrawerBuilder != null && (mDrawer.mDrawerBuilder.mFullscreen || mDrawer.mDrawerBuilder.mTranslucentNavigationBar)) {
            mRecyclerView.setPadding(0, 0, 0, UIUtils.getNavigationBarHeight(ctx));
        }

        //set the adapter with the items
        createItems();

        return mContainer;
    }

    /**
     * call this method to trigger the onProfileClick on the MiniDrawer
     */
    public void onProfileClick() {
        //crossfade if we are cross faded
        if (mCrossFader != null) {
            if (mCrossFader.isCrossfaded()) {
                mCrossFader.crossfade();
            }
        }

        //update the current profile
        if (mAccountHeader != null) {
            IProfile profile = mAccountHeader.getActiveProfile();
            if (profile instanceof ProfileDrawerItem) {
                mDrawerAdapter.setDrawerItem(0, new MiniProfileDrawerItem((ProfileDrawerItem) profile));
            }
        }
    }

    /**
     * call this method to trigger the onItemClick on the MiniDrawer
     *
     * @param selectedDrawerItem
     * @return
     */
    public boolean onItemClick(IDrawerItem selectedDrawerItem) {
        //We only need to clear if the new item is selectable
        if (selectedDrawerItem.isSelectable()) {
            //crossfade if we are cross faded
            if (mCrossFader != null) {
                if (mCrossFader.isCrossfaded()) {
                    mCrossFader.crossfade();
                }
            }

            //get the identifier
            int identifier = selectedDrawerItem.getIdentifier();

            //update everything
            if (mDrawer != null) {
                for (IDrawerItem drawerItem : mDrawerAdapter.getDrawerItems()) {
                    drawerItem.withSetSelected(drawerItem.getIdentifier() == identifier);
                }
                mDrawerAdapter.notifyDataSetChanged();
            }

            return false;
        } else {
            return true;
        }
    }

    /**
     * update a MiniDrawerItem (after updating the main Drawer) via its identifier
     *
     * @param identifier the identifier of the item which was updated
     */
    public void updateItem(int identifier) {
        if (mDrawer != null && mDrawerAdapter != null && mDrawerAdapter.getDrawerItems() != null && identifier != -1) {
            IDrawerItem drawerItem = mDrawer.getDrawerItem(identifier);

            if (drawerItem instanceof PrimaryDrawerItem) {
                for (int i = 0; i < mDrawerAdapter.getDrawerItems().size(); i++) {
                    if (mDrawerAdapter.getDrawerItems().get(i).getIdentifier() == drawerItem.getIdentifier()) {
                        mDrawerAdapter.setDrawerItem(i, new MiniDrawerItem((PrimaryDrawerItem) drawerItem));
                    }
                }
            }
        }
    }

    /**
     * creates the items for the MiniDrawer
     */
    public void createItems() {
        mDrawerAdapter.clearDrawerItems();

        if (mAccountHeader != null) {
            IProfile profile = mAccountHeader.getActiveProfile();
            if (profile instanceof ProfileDrawerItem) {
                mDrawerAdapter.addDrawerItem(new MiniProfileDrawerItem((ProfileDrawerItem) profile));
            }
        }

        if (mDrawer != null) {
            if (mDrawer.getDrawerItems() != null) {
                ArrayList<IDrawerItem> drawerItems = mDrawer.getDrawerItems();
                if (mDrawer.switchedDrawerContent()) {
                    drawerItems = mDrawer.getOriginalDrawerItems();
                }

                //migrate to miniDrawerItems
                for (IDrawerItem drawerItem : drawerItems) {
                    if (drawerItem instanceof PrimaryDrawerItem) {
                        mDrawerAdapter.addDrawerItem(new MiniDrawerItem((PrimaryDrawerItem) drawerItem));
                    }
                }
            }
        }

        //listener
        mDrawerAdapter.setOnClickListener(new BaseDrawerAdapter.OnClickListener() {
            @Override
            public void onClick(View v, int position, IDrawerItem item) {
                if (item instanceof MiniDrawerItem) {
                    if (mDrawerAdapter != null && item.isSelectable()) {
                        mDrawer.setSelection(item, true);
                    }
                } else if (item instanceof MiniProfileDrawerItem) {
                    if (mAccountHeader != null) {
                        if (!mAccountHeader.isSelectionListShown()) {
                            mAccountHeader.toggleSelectionList(v.getContext());
                        }
                    }
                    if (mCrossFader != null) {
                        mCrossFader.crossfade();
                    }
                }
            }
        });
        mRecyclerView.scrollToPosition(0);
    }
}
