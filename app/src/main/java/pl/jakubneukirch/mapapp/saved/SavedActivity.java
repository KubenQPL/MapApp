package pl.jakubneukirch.mapapp.saved;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.jakubneukirch.mapapp.R;
import pl.jakubneukirch.mapapp.app.MapApp;
import pl.jakubneukirch.mapapp.base.BaseActivity;
import pl.jakubneukirch.mapapp.data.model.dto.RouteDto;
import pl.jakubneukirch.mapapp.di.ActivityModule;
import pl.jakubneukirch.mapapp.main.MainActivity;
import pl.jakubneukirch.mapapp.view.SpinnerButton;

import static pl.jakubneukirch.mapapp.main.MainActivity.KEY_ROUTE_ID;

public class SavedActivity extends BaseActivity<SavedView, SavedPresenter> implements SavedView {

    @BindView(R.id.savedToolbar)
    Toolbar savedToolbar;
    @BindView(R.id.savedRecyclerView)
    RecyclerView savedRecyclerView;
    @BindView(R.id.spinnerButton)
    SpinnerButton spinnerButton;

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
        setupSpinnerButton();
    }

    private void setupRecyclerView() {
        adapter.setOnItemClickListener((parent, view, position, id) -> presenter.routeSelected(position));
        savedRecyclerView.setAdapter(adapter);
        savedRecyclerView.setLayoutManager(
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.VERTICAL,
                        false
                )
        );
    }

    private void setupSpinnerButton() {
        spinnerButton.setItems(getResources().getStringArray(R.array.spinner_screens));
        spinnerButton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.itemScreenSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void openRoute(long routeId) {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(KEY_ROUTE_ID, routeId);
        startActivity(intent);
    }

    @Override
    public void goBack() {
        onBackPressed();
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
