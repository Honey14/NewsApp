package com.honeyapp.di.component;

import android.content.Context;

import com.honeyapp.BaseApplication;
import com.honeyapp.di.module.ContextModule;
import com.honeyapp.di.module.RetrofitModule;
import com.honeyapp.di.qualifier.ApplicationContext;
import com.honeyapp.di.scopes.ApplicationScope;
import com.honeyapp.retrofit.Interactor;

import dagger.Component;

@ApplicationScope
@Component(modules = {ContextModule.class, RetrofitModule.class})
public interface ApplicationComponent {

    Interactor getApiInteractor();

    @ApplicationContext
    Context getContext();

    void injectApp(BaseApplication myapp);
}
