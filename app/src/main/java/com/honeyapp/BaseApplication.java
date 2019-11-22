package com.honeyapp;

import android.app.Activity;
import android.app.Application;

import com.honeyapp.di.component.ApplicationComponent;
import com.honeyapp.di.component.DaggerApplicationComponent;
import com.honeyapp.di.module.ContextModule;

public class BaseApplication extends Application {

    ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent
                .builder()
                .contextModule(new ContextModule(this)).build();
        applicationComponent.injectApp(this);
    }

    public static BaseApplication getInstance(Activity activity) {
        return (BaseApplication) activity.getApplication();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
