package com.honeyapp.di.component;

import android.content.Context;

import com.honeyapp.MainActivity;
import com.honeyapp.di.module.AdapterModule;
import com.honeyapp.di.module.MainActMvpModule;
import com.honeyapp.di.qualifier.ActivityContext;
import com.honeyapp.di.scopes.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(modules = {AdapterModule.class, MainActMvpModule.class},dependencies = ApplicationComponent.class)
public interface MainActivityComponent {

    @ActivityContext
    Context getContext();

    void injectMainActivity(MainActivity mainActivity);
}
