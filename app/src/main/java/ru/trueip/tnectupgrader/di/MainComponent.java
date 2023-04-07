package ru.trueip.tnectupgrader.di;


import javax.inject.Singleton;

import dagger.Component;
import ru.trueip.tnectupgrader.app.main_screen.MainPresenter;
import ru.trueip.tnectupgrader.base.BaseContract;
import ru.trueip.tnectupgrader.base.BasePresenter;
import ru.trueip.tnectupgrader.base.receivers.BaseReceiver;
import ru.trueip.tnectupgrader.di.modules.CacheModule;
import ru.trueip.tnectupgrader.di.modules.ContextModule;


@Singleton
@Component(modules = {CacheModule.class, ContextModule.class})
public interface MainComponent {

    void inject(BasePresenter<BaseContract> cBasePresenter);

    void inject(MainPresenter mainPresenter);

    void inject(BaseReceiver baseReceiver);

}
