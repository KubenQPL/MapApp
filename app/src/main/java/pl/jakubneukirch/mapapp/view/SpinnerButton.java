package pl.jakubneukirch.mapapp.view;

import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import pl.jakubneukirch.mapapp.R;

public class SpinnerButton extends AppCompatSpinner {

    private static final int[] STATE_EXPANDED = {R.attr.state_expanded};

    private boolean isExpanded = false;
    public String[] items = null;
    Point displaySize = new Point();

    private OnItemSelectedListener onItemSelectedListener;

    private int foregroundResId = -1;
    private Drawable foregroundDrawable = null;

    public SpinnerButton(Context context) {
        super(context);
        setup();
    }

    public SpinnerButton(Context context, int mode) {
        super(context, mode);
        setup();
    }

    public SpinnerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (Build.VERSION.SDK_INT < 23) {
            foregroundResId = getForegroundResIdAttribute(attrs, 0);
        }
        setup();
    }

    public SpinnerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (Build.VERSION.SDK_INT < 23) {
            foregroundResId = getForegroundResIdAttribute(attrs, defStyleAttr);
        }
        setup();
    }

    public SpinnerButton(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
        if (Build.VERSION.SDK_INT < 23) {
            foregroundResId = getForegroundResIdAttribute(attrs, defStyleAttr);
        }
        setup();
    }

    public SpinnerButton(Context context, AttributeSet attrs, int defStyleAttr, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);
        if (Build.VERSION.SDK_INT < 23) {
            foregroundResId = getForegroundResIdAttribute(attrs, defStyleAttr);
        }
        setup();
    }

    private void setup() {
        if(Build.VERSION.SDK_INT >= 23){
            setPopupBackgroundDrawable(null);
        } else {
            loadForegroundDrawable();
        }
    }

    private void loadForegroundDrawable() {
        if (foregroundResId != -1) {
            foregroundDrawable = getResources().getDrawable(foregroundResId);
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        foregroundDrawable.setState(getDrawableState());
        invalidate();
    }

    private int getForegroundResIdAttribute(AttributeSet attrs, int defStyle) {
        final int[] foregroundAttr = {android.R.attr.foreground};
        TypedArray array = getContext().obtainStyledAttributes(attrs, foregroundAttr, defStyle, 0);
        int foregroundRes = array.getResourceId(0, -1);
        array.recycle();
        return foregroundRes;
    }

    public void setItems(String[] items) {
        this.items = items;
        setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner_item, items));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getSelectedView() != null) {
            getSelectedView().setVisibility(View.GONE);
        }
        super.onDraw(canvas);
        if (Build.VERSION.SDK_INT < 23 && foregroundDrawable != null) {
            foregroundDrawable.draw(canvas);
        }
    }

    @Override
    public void setOnItemSelectedListener(@Nullable OnItemSelectedListener listener) {
        this.onItemSelectedListener = listener;
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
        if (onItemSelectedListener != null) {
            onItemSelectedListener.onItemSelected(null, null, position, 0);
        }
    }

    @Override
    public boolean performClick() {
        isExpanded = true;
        return super.performClick();
    }

    public void performClose() {
        isExpanded = false;
        refreshDrawableState();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (isExpanded && hasWindowFocus) {
            performClose();
        }
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isExpanded) {
            mergeDrawableStates(drawableState, STATE_EXPANDED);
        }
        return drawableState;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final Display display = ((WindowManager) getContext().getSystemService(Service.WINDOW_SERVICE)).getDefaultDisplay();
        display.getSize(displaySize);
        setDropDownWidth(displaySize.x);
        foregroundDrawable.setBounds(0,0,getWidth(), getHeight());
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
