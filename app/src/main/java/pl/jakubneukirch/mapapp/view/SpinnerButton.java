package pl.jakubneukirch.mapapp.view;

import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import pl.jakubneukirch.mapapp.R;

public class SpinnerButton extends AppCompatSpinner {

    public String[] items = null;

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
        getSelectedView().setVisibility(View.GONE);
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Display display = ((WindowManager)getContext().getSystemService(Service.WINDOW_SERVICE)).getDefaultDisplay();
        setDropDownWidth(display.getWidth());
    }
}
