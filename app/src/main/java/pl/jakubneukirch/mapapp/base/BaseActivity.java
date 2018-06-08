package pl.jakubneukirch.mapapp.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

public abstract class BaseActivity<P extends MvpView, T extends BasePresenter<P>> extends AppCompatActivity implements MvpView {

    @Inject
    protected T presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
        attachViewToPresenter();
    }

    public abstract void injectDependencies();

    @SuppressWarnings("unchecked")
    private void attachViewToPresenter() {
        presenter.attachView((P) this);
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void showMessage(int stringId) {
        showMessage(getString(stringId));
    }

    @Override
    public void showMessage(String text) {
        Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG).show();
    }
}