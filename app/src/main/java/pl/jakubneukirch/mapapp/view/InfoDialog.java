package pl.jakubneukirch.mapapp.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.jakubneukirch.mapapp.R;
import pl.jakubneukirch.mapapp.data.model.api.PlaceDetails;

public class InfoDialog extends Dialog {

    @BindView(R.id.infoRecyclerView)
    RecyclerView recyclerView;

    private InfoRecyclerAdapter adapter = new InfoRecyclerAdapter();

    public InfoDialog(Context context) {
        super(context);
    }

    public InfoDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public InfoDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = View.inflate(getContext(), R.layout.dialog_info, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);
        ButterKnife.bind(this, view);
        setup();
    }

    private void setup() {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    public void setInfo(List<PlaceDetails> list) {
        adapter.setList(list);
    }

}
