package p.gorden.pdalibrary.tableLayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Smartown on 2017/7/19.
 */
public class FreeScrollView extends FrameLayout {

    private GestureDetector gestureDetector;

    public FreeScrollView(Context context) {
        super(context);
        init();
    }

    public FreeScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FreeScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FreeScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 1) {
            throw new RuntimeException("FreeScrollView must contain only one child!");
        }
    }

    private void init() {
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View view = getChildAt(0);
                if (view instanceof TableLayout) {
                    ((TableLayout) view).onClick(e.getX() + getScrollX(), e.getY() + getScrollY());
                }
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                View view = getChildAt(0);
                int childHeight = view.getHeight();
                int childWidth = view.getWidth();
                int toX, toY;

                Log.e("FreeScrollView", "onScroll childWidth:" + childWidth);
                Log.e("FreeScrollView", "onScroll width:" + getWidth());
                Log.e("FreeScrollView", "onScroll distanceX:" + distanceX);
                Log.e("FreeScrollView", "onScroll scrollX:" + getScrollX());

                Log.e("FreeScrollView", "child X:" + view.getX());
                Log.e("FreeScrollView", "child translationX:" + view.getTranslationX());
                Log.e("FreeScrollView", "child Y:" + view.getY());
                Log.e("FreeScrollView", "child translationY:" + view.getTranslationY());
                Log.e("FreeScrollView", "X:" + getX());
                Log.e("FreeScrollView", "translationX:" + getTranslationX());
                Log.e("FreeScrollView", "Y:" + getY());
                Log.e("FreeScrollView", "translationY:" + getTranslationY());

                if (distanceX > 0) {
                    // 横向可以滑动
                    if (childWidth > getWidth()) {
                        if (getScrollX() + getWidth() >= childWidth) {
                            toX = childWidth - getWidth();
                        } else {
                            toX = (int) (getScrollX() + distanceX);
                        }
                    } else {
                        toX = 0;
                    }
                } else {
                    if (getScrollX() + distanceX < 0) {
                        toX = 0;
                    } else {
                        toX = (int) (getScrollX() + distanceX);
                    }
                }
                if (distanceY > 0) {
                    if (childHeight > getHeight()) {
                        if (getScrollY() + getHeight() >= childHeight) {
                            toY = childHeight - getHeight();
                        } else {
                            toY = (int) (getScrollY() + distanceY);
                        }
                    } else {
                        toY = 0;
                    }
                } else {
                    if (getScrollY() + distanceY < 0) {
                        toY = 0;
                    } else {
                        toY = (int) (getScrollY() + distanceY);
                    }
                }
                scrollTo(toX, toY);
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

}
