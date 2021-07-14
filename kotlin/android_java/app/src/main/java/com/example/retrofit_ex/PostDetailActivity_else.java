package com.example.retrofit_ex;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostDetailActivity_else extends AppCompatActivity {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://172.10.18.137:80";

    private RecyclerView rc;

    ArrayList<String> namesofliked;
    String name, title, content,userName;
    TextView Vname, Vtitle, Vcontent, Vliked;
    EditText writeComment;
    Button commentBtn;

    private CommentVAdapter commentVAdapter;

    ArrayList<CommentInfo> commentList=new ArrayList<CommentInfo>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f3_elseview);
        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        title=intent.getStringExtra("title");
        content=intent.getStringExtra("content");
        userName=intent.getStringExtra("userName");
        namesofliked=intent.getStringArrayListExtra("namesofliked");


        Vname=findViewById(R.id.name);
        Vtitle=findViewById(R.id.title);
        Vcontent=findViewById(R.id.content);
        Vliked=findViewById(R.id.like);

        Vname.setText("name : "+name);
        Vtitle.setText("title : "+title);
        Vcontent.setText("content : \n"+content);
        Vliked.setText(""+namesofliked.size());

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        setCommentRC();
    }

    @Override
    protected void onStart() {
        super.onStart();
        writeComment=findViewById(R.id.writeComment);
        commentBtn=findViewById(R.id.commentBtn);

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeComment();
            }
        });
    }

    private void writeComment() {
        String comment=writeComment.getText().toString();
        Log.d("쓰기", comment);
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", userName);
        map.put("title", title);
        map.put("comment", comment);

        Log.d("맵", map.toString());
        Call<Void> call = retrofitInterface.executeCommentPost(map);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(PostDetailActivity_else.this,
                            "Uploaded successfully", Toast.LENGTH_LONG).show();
                } else if (response.code() == 400) {
                    Toast.makeText(PostDetailActivity_else.this,
                            "이미 작성 했어요", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(PostDetailActivity_else.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setCommentRC() {

        HashMap<String, Object> map = new HashMap<>();
        map.put("title", title);
        Call<ResponseBody> call = retrofitInterface.getComment(map);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //List<JSONObject> result= response.body();
                //showPostResult
                if (response.code() == 500) {
                    Log.d("세상에", "database has failed");
                } else if (response.code() == 200) {
                    Log.d("세상에", "Uploaded successfully");

                    Gson gson = new Gson();
                    try {
                        String jsonString = response.body().string();
                        List<CommentInfo> list = gson.fromJson(jsonString, new TypeToken<List<CommentInfo>>() {}.getType());
                        commentList = new ArrayList<CommentInfo>();
                        for (int i = 0; i < list.size(); i++) {
                            commentList.add(new CommentInfo(userName, list.get(i).getTitle(), list.get(i).getComment()));
                        }
                        ArrayList<CommentInfo> titlecommentList= new ArrayList<CommentInfo>();
                        for (int i = 0; i < commentList.size(); i++) {
                            if (title.equals(commentList.get(i).getTitle())) {
                                titlecommentList.add(new CommentInfo(commentList.get(i).getName(), commentList.get(i).getTitle(), commentList.get(i).getComment()));
                            }
                        }

                        rc = findViewById(R.id.idRVContacts);
                        //on below line we are setting layout manger.
                        rc.setLayoutManager(new LinearLayoutManager(PostDetailActivity_else.this));
                        rc.addItemDecoration(new DividerItemDecoration(PostDetailActivity_else.this, 1));

                        commentVAdapter = new CommentVAdapter(titlecommentList, userName);
                        //on below line we are setting adapter to our recycler view.
                        rc.setAdapter(commentVAdapter);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("뭐", t.getMessage());
            }
        });
    }
}
