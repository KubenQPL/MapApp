package pl.jakubneukirch.mapapp.saved;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.jakubneukirch.mapapp.R;
import pl.jakubneukirch.mapapp.app.MapApp;
import pl.jakubneukirch.mapapp.base.BaseActivity;
import pl.jakubneukirch.mapapp.data.model.dto.RouteDto;
import pl.jakubneukirch.mapapp.di.ActivityModule;

public class SavedActivity extends BaseActivity<SavedView, SavedPresenter> implements SavedView {

    @BindView(R.id.savedToolbar)
    Toolbar savedToolbar;
    @BindView(R.id.savedRecyclerView)
    RecyclerView savedRecyclerView;

    private final SavedRecyclerAdapter adapter = new SavedRecyclerAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        presenter.onCreate();
    }

    @Override
    public void setup() {
        ButterKnife.bind(this);
        setupToolbar();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        savedRecyclerView.setAdapter(adapter);
        savedRecyclerView.setLayoutManager(
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.VERTICAL,
                        false
                )
        );
    }

    @Override
    public void setRoutes(List<RouteDto> routes) {
        adapter.setList(routes);
    }

    @Override
    public void injectDependencies() {
        MapApp.appComponent
                .getActivityComponent(new ActivityModule(this))
                .inject(this);
    }

    @Override
    public void setupToolbar() {
        setSupportActionBar(savedToolbar);
    }
}
