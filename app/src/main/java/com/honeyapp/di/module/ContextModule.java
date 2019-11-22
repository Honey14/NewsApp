package com.honeyapp.di.module;

import android.content.Context;

import com.honeyapp.di.qualifier.ApplicationContext;
import com.honeyapp.di.scopes.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {
    private Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    @ApplicationScope
    @ApplicationContext
    public Context giveContext(){
        return context;
    }
}
