package pl.jakubneukirch.mapapp.base;

public interface Presenter<P extends MvpView> {
    void attachView(P view);

    void detachView();

    void onDestroy();
}
