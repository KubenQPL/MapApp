package pl.jakubneukirch.mapapp.base;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class BasePresenter<P extends MvpView> implements Presenter<P> {

    protected P view = null;
    protected CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public BasePresenter() {
    }

    @Override
    public void attachView(P view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void onDestroy() {
        disposables.dispose();
    }
}
