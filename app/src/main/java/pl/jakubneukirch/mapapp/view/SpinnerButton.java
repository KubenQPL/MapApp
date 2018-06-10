package pl.jakubneukirch.mapapp.view;

import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import pl.jakubneukirch.mapapp.R;

public class SpinnerButton extends AppCompatSpinner {

    private static final int SPINNER_STYLE = R.style.AppTheme_Spinner;
    private static final int[] STATE_EXPANDED = {R.attr.state_expanded};

    private boolean isExpanded = false;
    public String[] items = null;
    Point displaySize = new Point();
    private OnItemSelectedListener onItemSelectedListener;

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
        setup();
    }

    public SpinnerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    public SpinnerButton(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
        setup();
    }

    public SpinnerButton(Context context, AttributeSet attrs, int defStyleAttr, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);
        setup();
    }

    private void setup() {

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
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
