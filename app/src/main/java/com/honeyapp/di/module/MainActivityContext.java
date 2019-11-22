package com.honeyapp.di.module;

import android.content.Context;

import com.honeyapp.MainActivity;
import com.honeyapp.di.qualifier.ActivityContext;
import com.honeyapp.di.scopes.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityContext {
    private MainActivity mainActivity;

    public Context context;

    public MainActivityContext(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        context = mainActivity;
    }

    @Provides
    @ActivityScope
    public MainActivity giveMainActivity() {
        return mainActivity;
    }

    @Provides
    @ActivityScope
    @ActivityContext
    public Context giveContext() {
        return context;
    }
}
