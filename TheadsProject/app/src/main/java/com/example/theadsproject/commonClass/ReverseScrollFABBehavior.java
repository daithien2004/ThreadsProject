package com.example.theadsproject.commonClass;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ReverseScrollFABBehavior extends FloatingActionButton.Behavior {

    private boolean isFabVisible = false; // Bắt đầu ở trạng thái ẩn nếu bạn muốn

    public ReverseScrollFABBehavior() {
        super();
    }

    public ReverseScrollFABBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                       FloatingActionButton child,
                                       View directTargetChild, View target,
                                       int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout,
                               FloatingActionButton child,
                               View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed,
                               int type, int[] consumed) {
        super.onNestedScroll(coordinatorLayout, child, target,
                dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);

        // Scroll xuống → hiện FAB nếu chưa hiện
        if (dyConsumed > 0 && !isFabVisible) {
            child.show();
            isFabVisible = true;
        }
        // Scroll lên → ẩn FAB nếu đang hiện
        else if (dyConsumed < 0 && isFabVisible) {
            child.hide();
            isFabVisible = false;
        }
    }
}
