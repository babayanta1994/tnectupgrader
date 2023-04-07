package ru.trueip.tnectupgrader.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ru.trueip.tnectupgrader.repository.Cache;

/**
 * Created by user on 13-Sep-17.
 */

@Module(includes = ContextModule.class)
public class CacheModule {

    @Provides
    public Cache provideCache(Context context) {
        return new Cache(context);
    }
}
