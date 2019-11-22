package com.honeyapp.di.module;

import com.honeyapp.di.scopes.ActivityScope;
import com.honeyapp.mvp.MainContract;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActMvpModule {
    private final MainContract.View mView;


    public MainActMvpModule(MainContract.View mView) {
        this.mView = mView;
    }

    @Provides
    @ActivityScope
    MainContract.View getmView(){
        return mView;
    }
}
