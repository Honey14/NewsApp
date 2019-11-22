package com.honeyapp.mvp;

import com.honeyapp.pojo.Articles;
import java.util.ArrayList;

public interface MainContract {
    interface View {
        void showData(ArrayList<Articles> data);

        void showError(String message);

        void showComplete();

        void showProgress();

        void hideProgress();
    }

    interface Presenter {
        void loadData();

    }
}
