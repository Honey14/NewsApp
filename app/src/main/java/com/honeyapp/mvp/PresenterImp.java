package com.honeyapp.mvp;


import com.honeyapp.pojo.Articles;
import com.honeyapp.pojo.HeadStart;
import com.honeyapp.retrofit.Interactor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PresenterImp implements MainContract.Presenter {
    Interactor interactor;
    MainContract.View mView;

    @Inject
    public PresenterImp(Interactor interactor, MainContract.View mView) {
        this.interactor = interactor;
        this.mView = mView;
    }

    @Override
    public void loadData() {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        mView.showProgress();
        interactor
                .getNews("bitcoin", timeStamp,
                        "publishedAt", "ae635d0fc0c44126ace908a795116db1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HeadStart>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(HeadStart headStart) {
                        ArrayList<Articles> articles = headStart.getArticles();
                        mView.showData(articles);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError("Please try again!");
                    }

                    @Override
                    public void onComplete() {
                        mView.showComplete();
                        mView.hideProgress();
                    }
                });
    }
}
