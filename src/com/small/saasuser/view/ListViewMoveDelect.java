package com.small.saasuser.view;

/**
 * 左滑删除控制类
 */
import com.small.saasuser.entity.PlanEntity;
import com.small.saasuser.entity.TravelPlanEntity;
import com.small.saasuser.utils.LogUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class ListViewMoveDelect extends ListView {

	private static final String TAG = "ListViewMoveDelect";

	private SlideView mFocusedItemView;

	public ListViewMoveDelect(Context context) {
		super(context);
	}

	public ListViewMoveDelect(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListViewMoveDelect(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void shrinkListItem(int position) {
		View item = getChildAt(position);

		if (item != null) {
			try {
				((SlideView) item).shrink();
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}
	}
	//滑动事件监听处理
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			int x = (int) event.getX();
			int y = (int) event.getY();
			int position = pointToPosition(x, y);
			LogUtil.i(TAG, "postion=" + position);
			if (position != INVALID_POSITION) {
				PlanEntity data = (PlanEntity) getItemAtPosition(position);
				mFocusedItemView = data.slideView;
				LogUtil.i(TAG, "FocusedItemView=" + mFocusedItemView);
			}
		}
		default:
			break;
		}

		if (mFocusedItemView != null) {
			mFocusedItemView.onRequireTouchEvent(event);
		}

		return super.onTouchEvent(event);
	}

}
