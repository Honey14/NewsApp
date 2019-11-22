package com.honeyapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.honeyapp.Adapter.ListOfNews;
import com.honeyapp.di.component.ApplicationComponent;

import com.honeyapp.di.component.DaggerMainActivityComponent;
import com.honeyapp.di.component.MainActivityComponent;
import com.honeyapp.di.module.MainActMvpModule;
import com.honeyapp.di.module.MainActivityContext;
import com.honeyapp.di.qualifier.ActivityContext;
import com.honeyapp.di.qualifier.ApplicationContext;
import com.honeyapp.mvp.MainContract;
import com.honeyapp.mvp.PresenterImp;
import com.honeyapp.pojo.Articles;

import com.honeyapp.room.ArticleDao;
import com.honeyapp.room.ArticleDb;
import com.honeyapp.room.NewsDetails;


import java.io.File;
import java.io.FileOutputStream;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContract.View, ListOfNews.setOnCardClick {
    @BindView(R.id.my_toolbar)
    Toolbar my_toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.recyclerView_news_list)
    RecyclerView recyclerView_news_list;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    MainActivityComponent mainActivityComponent;


    @Inject
    public ListOfNews listOfNewsAdapter;

    @Inject
    @ApplicationContext
    public Context mContext;

    @Inject
    @ActivityContext
    public Context activityContext;

    @Inject
    PresenterImp presenterImp;

    ArticleDao articleDao;
    List<NewsDetails> newsDetailsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ApplicationComponent applicationComponent =
                BaseApplication
                        .getInstance(this)
                        .getApplicationComponent();
        mainActivityComponent = DaggerMainActivityComponent.builder()
                .mainActivityContext(new MainActivityContext(MainActivity.this))
                .mainActMvpModule(new MainActMvpModule(this))
                .applicationComponent(applicationComponent)
                .build();
        mainActivityComponent.injectMainActivity(MainActivity.this);
        ButterKnife.bind(this);
        setSupportActionBar(my_toolbar);
        setFont();
        recyclerView_news_list.setLayoutManager(new LinearLayoutManager(activityContext));
        recyclerView_news_list.setAdapter(listOfNewsAdapter);
        presenterImp.loadData();

    }

    private void setFont() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/robotoslabbold.ttf");
        toolbar_title.setTypeface(typeface);
    }

    @Override
    public void showData(ArrayList<Articles> data) {


        if (data.size() > 0) {
            saveToInternalStorage(data);
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(mContext, "No Data Found", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveToInternalStorage(ArrayList<Articles> articles) {
        ArticleDb articleDb = ArticleDb.getInstance(getApplicationContext());
        ArrayList<NewsDetails> newsDetailsArrayList = new ArrayList<>();
        new storeImageFromUrl().execute(articles);
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDirectory", Context.MODE_PRIVATE);
        for (int i = 0; i < articles.size(); i++) {
            NewsDetails details = new NewsDetails(articles.get(i).getTitle(),
                    articles.get(i).getDescription(),
                    articles.get(i).getSource().getName(),
                    articles.get(i).getPublishedAt(),
                    directory.getAbsolutePath() + "/article" + i + ".jpg");
            newsDetailsArrayList.add(details);
        }

        if (newsDetailsArrayList.size() > 0)
            articleDao = articleDb.articleDao();
        new insertArticlesAsynctask(articleDao).execute(newsDetailsArrayList);
        return null;
    }

    @Override
    public void showError(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        ArticleDb articleDb = ArticleDb.getInstance(getApplicationContext());
        articleDao = articleDb.articleDao();
        new RetrieveArticlesAsynctask(articleDao).execute();
    }

    @Override
    public void showComplete() {

    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void launchIntent(NewsDetails articles) {
        Intent intent = new Intent(activityContext, DetailActivity.class);
        intent.putExtra("desc", articles.getDescription());
        intent.putExtra("name", articles.getName());
        intent.putExtra("title", articles.getTitle());
        intent.putExtra("date", articles.getPublishedAt());
        intent.putExtra("image", articles.getUrlToImage());
        startActivity(intent);
    }


    //Asynctask
    public class storeImageFromUrl extends AsyncTask<ArrayList<Articles>, Void, String> {


        @Override
        protected String doInBackground(ArrayList<Articles>... arrayLists) {
            File directory = null;
            for (ArrayList<Articles> articlesArrayList : arrayLists) {
                for (int i = 0; i < articlesArrayList.size(); i++) {
                    Bitmap bitmap = null;
                    try {
                        URL url = new URL(articlesArrayList.get(i).getUrlToImage());
                        Log.d("sdfgvsdg", articlesArrayList.get(i).getUrlToImage());
                        bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                    directory = cw.getDir("imageDirectory", Context.MODE_PRIVATE);
                    File mypath = new File(directory, "article" + i + ".jpg");
                    if (!mypath.exists()) {
                        Log.d("path", mypath.toString());
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(mypath);
                            if (bitmap != null) {
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.flush();
                                fos.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            return directory.getAbsolutePath();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public class insertArticlesAsynctask extends AsyncTask<ArrayList<NewsDetails>, Void, Void> {

        private ArticleDao articleDao;

        public insertArticlesAsynctask(ArticleDao articleDao) {
            this.articleDao = articleDao;
        }

        @Override
        protected Void doInBackground(ArrayList<NewsDetails>... lists) {
            if (isNetworkConnected()) {
                newsDetailsArrayList = articleDao.loadAllArticles();
                if (newsDetailsArrayList != null && newsDetailsArrayList.size() > 0) {
                    articleDao.nukeTable();

                }
                for (ArrayList<NewsDetails> l : lists) {
                    for (NewsDetails news1 : l) {
                        ArticleDb
                                .getInstance(activityContext)
                                .articleDao()
                                .insert(news1);
                    }
                }
                newsDetailsArrayList = articleDao.loadAllArticles();
            } else {
                newsDetailsArrayList = articleDao.loadAllArticles();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);

            if (newsDetailsArrayList != null && newsDetailsArrayList.size() > 0) {
                ArrayList<NewsDetails> newsDetails1 = new ArrayList<>(newsDetailsArrayList);
                listOfNewsAdapter.setNews(newsDetails1);
            } else {
                if (isNetworkConnected())
                    Toast.makeText(activityContext, "No data found", Toast.LENGTH_SHORT).show();
                else
                    showRetryDialog();
            }
        }
    }

    public class RetrieveArticlesAsynctask extends AsyncTask<Void, Void, List<NewsDetails>> {

        private ArticleDao articleDao;

        public RetrieveArticlesAsynctask(ArticleDao articleDao) {
            this.articleDao = articleDao;
        }

        @Override
        protected List<NewsDetails> doInBackground(Void... voids) {
            newsDetailsArrayList = articleDao.loadAllArticles();
            if (newsDetailsArrayList != null && newsDetailsArrayList.size() > 0) {
                return newsDetailsArrayList;
            } else
                return null;
        }

        @Override
        protected void onPostExecute(List<NewsDetails> newsDetails) {
            super.onPostExecute(newsDetails);
            progressBar.setVisibility(View.GONE);
            if (newsDetailsArrayList != null && newsDetailsArrayList.size() > 0) {
                ArrayList<NewsDetails> newsDetails1 = new ArrayList<>(newsDetailsArrayList);
                listOfNewsAdapter.setNews(newsDetails1);
            } else {
                if (isNetworkConnected())
                    Toast.makeText(activityContext, "No data found", Toast.LENGTH_SHORT).show();
                else
                    showRetryDialog();
            }
        }
    }

    private void showRetryDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("No Connection");
        builder.setMessage("Cannot connect to the internet!");
        builder.setPositiveButton("Retry", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!isNetworkConnected())
                    finish();
                else {
                    presenterImp.loadData();
                }
            }
        });
        builder.setNegativeButton("Exit", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}