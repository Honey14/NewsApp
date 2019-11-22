package com.honeyapp.di.module;



import com.honeyapp.Adapter.ListOfNews;
import com.honeyapp.MainActivity;

import com.honeyapp.di.scopes.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module(includes = {MainActivityContext.class})
public class AdapterModule {

    @Provides
    @ActivityScope
    public ListOfNews getNewsList(ListOfNews.setOnCardClick clickListener) {
        return new ListOfNews(clickListener);
    }

    @Provides
    @ActivityScope
    public ListOfNews.setOnCardClick getClickListener(MainActivity mainActivity) {
        return mainActivity;
    }
}
