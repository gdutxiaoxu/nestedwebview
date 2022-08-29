package com.xj.webview.sample.behavior;

import com.google.android.material.appbar.AppBarLayout;

public class AppBarLayoutStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

    public enum State {
        EXPANDED,//展开
        COLLAPSED,//折叠
        INTERMEDIATE//中间状态
    }

    private State mCurrentState = State.INTERMEDIATE;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED);
            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED);
            }
            mCurrentState = State.COLLAPSED;
        } else {
            if (mCurrentState != State.INTERMEDIATE) {
                onStateChanged(appBarLayout, State.INTERMEDIATE);
            }
            mCurrentState = State.INTERMEDIATE;
        }
    }

    public void onStateChanged(AppBarLayout appBarLayout, State state) {

    }


}
