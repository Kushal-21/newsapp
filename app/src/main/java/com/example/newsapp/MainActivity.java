package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.CategoryClickInterface{

    private RecyclerView newsRV,categoryRV;
    private ProgressBar loadingPB;
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryModel> categoryModelArrayList;
    private CategoryAdapter categoryAdapter;
    private NewsAdapter newsAdapter;
    //private EditText textQuery;
    //private Button buttonSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsRV = findViewById(R.id.news);
        categoryRV = findViewById(R.id.categories);
        loadingPB = findViewById(R.id.loading);
        articlesArrayList = new ArrayList<>();
        categoryModelArrayList = new ArrayList<>();
        newsAdapter = new NewsAdapter(articlesArrayList,this);
        categoryAdapter = new CategoryAdapter(categoryModelArrayList,this, this);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsAdapter);
        categoryRV.setAdapter(categoryAdapter);
        //textQuery = findViewById(R.id.textQuery);
        //buttonSearch = findViewById(R.id.buttonSearch);
        getCategories();
        getNews("All");
        newsAdapter.notifyDataSetChanged();

       // buttonSearch.setOnClickListener(new View.OnClickListener() {
           // @Override
            //public void onClick(View v) {

           // }
       // });



    }
    //method for getting data for category section
    private void getCategories(){
        categoryModelArrayList.add(new CategoryModel("All","https://images.unsplash.com/photo-1603855873822-0931a843ee3a?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTJ8fGFsbHxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60"));
        categoryModelArrayList.add(new CategoryModel("Technology","https://images.unsplash.com/photo-1488590528505-98d2b5aba04b?ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8dGVjaG5vbG9neXxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60"));
        categoryModelArrayList.add(new CategoryModel("Science","https://images.unsplash.com/photo-1532634993-15f421e42ec0?ixid=MnwxMjA3fDB8MHxzZWFyY2h8NDZ8fHNjaWVuY2V8ZW58MHx8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60"));
        categoryModelArrayList.add(new CategoryModel("Entertainment","https://images.unsplash.com/photo-1499364615650-ec38552f4f34?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8ZW50ZXJ0YWlubWVudHxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60"));
        categoryModelArrayList.add(new CategoryModel("Sports","https://images.unsplash.com/photo-1562077772-3bd90403f7f0?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTF8fGNyaWNrZXR8ZW58MHx8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60"));
        categoryModelArrayList.add(new CategoryModel("Business","https://images.unsplash.com/photo-1611095790444-1dfa35e37b52?ixid=MnwxMjA3fDF8MHxzZWFyY2h8MXx8YnVzaW5lc3N8ZW58MHx8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60"));
        categoryModelArrayList.add(new CategoryModel("General","https://images.unsplash.com/photo-1494059980473-813e73ee784b?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NXx8Z2VuZXJhbHxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=600&q=60"));
        categoryModelArrayList.add(new CategoryModel("Health","https://images.unsplash.com/photo-1526256262350-7da7584cf5eb?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MzB8fGhlYWx0aHxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60"));
        categoryModelArrayList.add(new CategoryModel("Politics","https://images.unsplash.com/photo-1529107386315-e1a2ed48a620?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8cG9saXRpY3N8ZW58MHx8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60"));
        categoryAdapter.notifyDataSetChanged();

    }
    //method for getting data for news section
    private void getNews(String category){
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryURL = "https://newsapi.org/v2/top-headlines?country=in&category="+ category +"&apiKey=20a7d5c4ca9f4c24b98b34cdffdd2a1d";
        String url = "https://newsapi.org/v2/top-headlines?country=in&exclusiveDomains=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=20a7d5c4ca9f4c24b98b34cdffdd2a1d";
        String BASE_URL = "https://newsapi.org/";
        //calling of retrofit class
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //calling retrofit interface class
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<NewsModel> call;
        if(category.equals("All")){
            call = retrofitAPI.getAllNews(url);
        }else {
            call = retrofitAPI.getNewsByCategory(categoryURL);
        }

        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                NewsModel newsModel = response.body();
                loadingPB.setVisibility(View.GONE);
                ArrayList<Articles> articles = newsModel.getArticles();
                for(int i=0; i< articles.size(); i++) {
                    articlesArrayList.add(new Articles(articles.get(i).getTitle(), articles.get(i).getDescription(), articles.get(i).getUrlToImage(),
                            articles.get(i).getUrl(), articles.get(i).getContent()));
                }
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Fail to get the News",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCategoryClick(int position) {
        String category = categoryModelArrayList.get(position).getCategory();
        getNews(category);
    }
}