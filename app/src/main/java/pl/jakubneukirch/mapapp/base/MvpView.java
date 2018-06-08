package pl.jakubneukirch.mapapp.base;

public interface MvpView {
    void showMessage(int stringId);

    void showMessage(String text);
}
