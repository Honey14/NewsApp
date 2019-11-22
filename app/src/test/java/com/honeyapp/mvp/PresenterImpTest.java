package com.honeyapp.mvp;

import com.honeyapp.pojo.HeadStart;
import com.honeyapp.retrofit.Interactor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class PresenterImpTest {

    private PresenterImp presenterImp;
    @Mock
    MainContract.View mView;
    @Mock
    Interactor interactor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenterImp = new PresenterImp(interactor, mView);
    }

    @Test
    public void loadData_onNext_shouldGetArticles() {
        HeadStart start = new HeadStart();
        when(interactor.getNews(anyString(), anyString(),
                anyString(), anyString())).thenReturn(Observable.just(start));
//
    }

}