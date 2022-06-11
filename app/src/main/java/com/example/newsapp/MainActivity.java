package com.example.newsapp;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface, SensorEventListener {

    //Pemanggilan untuk fitur sensor
    //Pemanggilan Sensor Instance
    private SensorManager mSensorManager;

    private TextView mTextTempSensor;
    private Sensor mTemperatureSensor;

    //NewsAPI = --9ee42810f982424cbc8fb13cd90d85e7--
    //Pemanggilan untuk News App
    private RecyclerView newsRV, categoryRV;
    private ProgressBar loadingPB;
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryRVModal> categoryRVModalsArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private NewsRVAdapter newsRVAdapter;

    //Memanggil menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    //Memanggil menu bahasa
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_language){
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Pendefinisian Sensor
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        mTextTempSensor = findViewById(R.id.temperatureBar);
        mTemperatureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);


        //Pendefinisian modul berita
        newsRV = findViewById(R.id.idRVNews);
        categoryRV = findViewById(R.id.idRVCategories);
        loadingPB = findViewById(R.id.idPBLoading);

        articlesArrayList = new ArrayList<>();
        categoryRVModalsArrayList = new ArrayList<>();
        newsRVAdapter = new NewsRVAdapter(articlesArrayList, this);
        categoryRVAdapter = new CategoryRVAdapter(categoryRVModalsArrayList, this, this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);

        getCategories();


        getNews("All");
        newsRVAdapter.notifyDataSetChanged();

    }


    private void getCategories(){
        categoryRVModalsArrayList.add(new CategoryRVModal("All","https://images.unsplash.com/photo-1484100356142-db6ab6244067?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTF8fHJhbmRvbXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=600&q=60"));
        categoryRVModalsArrayList.add(new CategoryRVModal("Dev.Suggestion","https://images.unsplash.com/photo-1484100356142-db6ab6244067?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTF8fHJhbmRvbXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=600&q=60"));
        categoryRVModalsArrayList.add(new CategoryRVModal("Technology","https://images.unsplash.com/photo-1550751827-4bd374c3f58b?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTZ8fHRlY2hub2xvZ2llc3xlbnwwfHwwfHw%3D&auto=format&fit=crop&w=600&q=60"));
        categoryRVModalsArrayList.add(new CategoryRVModal("Science","https://images.unsplash.com/photo-1628595351029-c2bf17511435?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTF8fHNjaWVuY2V8ZW58MHx8MHx8&auto=format&fit=crop&w=600&q=60"));
        categoryRVModalsArrayList.add(new CategoryRVModal("Sports","https://images.unsplash.com/photo-1574629810360-7efbbe195018?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NzV8fHNwb3J0c3xlbnwwfHwwfHw%3D&auto=format&fit=crop&w=600&q=60"));
        categoryRVModalsArrayList.add(new CategoryRVModal("General","https://images.unsplash.com/photo-1432821596592-e2c18b78144f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8Z2VuZXJhbHxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=600&q=60"));
        categoryRVModalsArrayList.add(new CategoryRVModal("Business","https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8N3x8YnVzaW5lc3N8ZW58MHx8MHx8&auto=format&fit=crop&w=600&q=60"));
        categoryRVModalsArrayList.add(new CategoryRVModal("Entertainment","https://images.unsplash.com/photo-1567593810070-7a3d471af022?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Nnx8ZW50ZXJ0YWlubWVudHxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=600&q=60"));
        categoryRVModalsArrayList.add(new CategoryRVModal("Health","https://images.unsplash.com/photo-1535914254981-b5012eebbd15?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTZ8fGhlYWx0aHxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=600&q=60"));
    }

    private void getNews(String category) {
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        //Jika Kategori adalah dari API Ubuntu Server (Developer Suggestion)
        if (category == "Dev.Suggestion"){
            Toast.makeText(MainActivity.this, "Developer News Suggestion", Toast.LENGTH_SHORT).show();
            String url = "https://8000";
            String MAIN_URL = "https://8000";
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MAIN_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
            Call<NewsModal> call;

        //Jika Kategori adalah dari bawaan Aplikasi NewsApp
        }else {
            String categoryURL = "https://newsapi.org/v2/top-headlines?country=id&category=" + category + "&apiKey=9ee42810f982424cbc8fb13cd90d85e7";
            String url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=9ee42810f982424cbc8fb13cd90d85e7";
            String BASE_URL = "https://newsapi.org/";
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
            Call<NewsModal> call;
            if (category.equals("All")) {
                call = retrofitAPI.getAllNews(url);
            } else {
                call = retrofitAPI.getNewsByCategory(categoryURL);
            }

            call.enqueue(new Callback<NewsModal>() {
                @Override
                public void onResponse(Call<NewsModal> call, Response<NewsModal> response) {
                    NewsModal newsModal = response.body();
                    loadingPB.setVisibility(View.GONE);
                    ArrayList<Articles> articles = newsModal.getArticles();
                    for (int i = 0; i < articles.size(); i++) {
                        articlesArrayList.add(new Articles(articles.get(i).getAuthor(), articles.get(i).getTitle(), articles.get(i).getDescription(), articles.get(i).getUrlToImage(),
                                articles.get(i).getUrl(), articles.get(i).getContent()));
                    }
                    newsRVAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<NewsModal> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Failed to get news", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    @Override
    public void onCategoryClick(int position) {
        String category = categoryRVModalsArrayList.get(position).getCategory();
        getNews(category);
    }

    //Memulai Kerja Sensor
    @Override
    protected void onStart() {
        super.onStart();

        if (mTemperatureSensor != null){
            mSensorManager.registerListener(this, mTemperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int type = sensorEvent.sensor.getType();
        float result = sensorEvent.values[0];

        switch (type) {
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                mTextTempSensor.setText(getResources().getString(R.string.temp_text, result));
                break;

            default:
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}