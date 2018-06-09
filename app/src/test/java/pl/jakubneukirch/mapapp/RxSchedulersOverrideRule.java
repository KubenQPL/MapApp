package pl.jakubneukirch.mapapp;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.concurrent.Callable;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class RxSchedulersOverrideRule implements TestRule {
    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RxAndroidPlugins.setInitMainThreadSchedulerHandler((Callable<Scheduler> handler) -> Schedulers.trampoline());
                RxJavaPlugins.setComputationSchedulerHandler((Scheduler scheduler) -> Schedulers.trampoline());
                RxJavaPlugins.setIoSchedulerHandler((Scheduler scheduler) -> Schedulers.trampoline());
                RxJavaPlugins.setNewThreadSchedulerHandler((Scheduler scheduler) -> Schedulers.trampoline());
                RxJavaPlugins.setSingleSchedulerHandler((Scheduler scheduler) -> Schedulers.trampoline());

                try {
                    base.evaluate();
                } finally {
                    RxJavaPlugins.reset();
                    RxAndroidPlugins.reset();
                }
            }
        };
    }
}
