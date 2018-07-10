//package com.small.saasuser.utils;
//
//import com.small.saasuser.activity.R;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Rect;
//import android.graphics.drawable.Drawable;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//
// 
//
///**
// *
// */
//public class ListItemDecoration extends RecyclerView.ItemDecoration {
//
//    private Drawable drawable;
//
//    private int mOrienation;
//
//    public ListItemDecoration(Context context, int oriention){
//
//        this.mOrienation=oriention;
//        drawable=context.getResources().getDrawable(android.R.drawable.divider_horizontal_dark);
//
//    }
//
//    @Override
//    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        super.onDraw(c, parent, state);
//        if(mOrienation== LinearLayoutManager.HORIZONTAL){
//            
//            drawHoriztional(c,parent);
//        }else{
//
//            drawVertical(c,parent);
//            
//        }       
//              
//        
//    }
//
//    private void drawVertical(Canvas c, RecyclerView parent) {
//
//        int left = parent.getPaddingLeft();
//        int right = parent.getWidth() - parent.getPaddingRight();
//
//        int childCount = parent.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View child = parent.getChildAt(i);
//            RecyclerView v = new RecyclerView(parent.getContext());
//            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
//                    .getLayoutParams();
//            int top = child.getBottom() + params.bottomMargin;
//            int bottom = top + drawable.getIntrinsicHeight();
//            drawable.setBounds(left, top, right, bottom);
//            drawable.draw(c);
//        }
//
//    }
//
//    private void drawHoriztional(Canvas c, RecyclerView parent) {
//
//        int top=parent.getPaddingTop();
//        int bottom=parent.getHeight()-parent.getPaddingBottom();
//
//        int childCount=parent.getChildCount();
//        for(int i=0;i<childCount;i++){
//
//            View child=parent.getChildAt(i);
//            RecyclerView.LayoutParams parmas= (RecyclerView.LayoutParams) child.getLayoutParams();
//            int left=child.getRight()+parmas.rightMargin;
//            int right=left+drawable.getIntrinsicHeight();
//            drawable.setBounds(left,top,right,bottom);
//            drawable.draw(c);
//
//        }
//
//
//    }
//
//    @Override
//    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
//    }
//}
