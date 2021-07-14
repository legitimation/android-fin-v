package com.example.retrofit_ex;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostVAdapter extends RecyclerView.Adapter<PostVAdapter.ViewHolder> {
    private ArrayList<PostInfo> mList;
    Context context;
    public MainActivity activity;
    String userName;
    ArrayList<String> namesofliked= new ArrayList<>();
    CheckBox likeImg;

    private final RetrofitInterface retrofitInterface;
    ArrayList<PostInfo> Postlist=new ArrayList<>();

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, title, content, like;
        CheckBox likeImg;

        public ViewHolder(View itemView) {
            super(itemView) ;
            // 뷰 객체에 대한 참조. (hold strong reference)
            this.name = itemView.findViewById(R.id.name) ;
            this.title = itemView.findViewById(R.id.title);
            this.content = itemView.findViewById(R.id.content);
            this.like = itemView.findViewById(R.id.like);
            this.likeImg=itemView.findViewById(R.id.likeImg);
        }
    }
    public PostVAdapter(ArrayList<PostInfo> list, String userName) {
        this.mList = list ;
        this.userName=userName;

//        String BASE_URL = "http://172.10.18.137:80";
        String BASE_URL = "http://172.10.18.137:80";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.post_rc_item, parent, false) ;
        ViewHolder vh = new ViewHolder(view) ;
        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PostInfo modal = mList.get(position);
        holder.name.setText(modal.getName()) ;
        holder.title.setText(modal.getTitle()) ;
        holder.content.setText(modal.getContent()) ;
        namesofliked=modal.getNamesofliked();
        holder.like.setText(Integer.toString(namesofliked.size()-1));

        this.likeImg=holder.likeImg;

        likeImg.setChecked(false);
        if (namesofliked.contains(userName)){
            likeImg.setChecked(true);
            Log.d("디폴트 들", userName+"  "+namesofliked);
        }

        holder.likeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (likeImg.isChecked()) {
                    Log.d("좋아요", "체크 됨, userName : "+userName+", name list :"+namesofliked.toString());
                    if (modal.getNamesofliked().contains(userName)) {
                        Toast.makeText(context, "이미 누른 좋아요 입니다.", Toast.LENGTH_LONG).show();
                        Log.d("좋아요","이미 누름");
                    }
                    else {
                        modal.getNamesofliked().add(userName);
                        Log.d("좋아요 한 이름 리스트" + userName, namesofliked.toString());

                        holder.like.setText(Integer.toString(namesofliked.size() - 1));

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("name", holder.name.getText());
                        map.put("title", holder.title.getText());
                        map.put("content", holder.content.getText());
                        map.put("namesofliked", namesofliked);

                        Log.d("해시 리스트 : ", map.toString());
                        Call<UpdateResult> call = retrofitInterface.executeUpdate(map);

                        call.enqueue(new Callback<UpdateResult>() {
                            @Override
                            public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
                                if (response.code() == 200) {
                                    Log.d("좋아요", "Success");
                                } else if (response.code() == 404) {
                                    Log.d("좋아요", "Post not found");
                                }
                            }

                            @Override
                            public void onFailure(Call<UpdateResult> call, Throwable t) {
                                Log.d("오류", t.getMessage());
                            }
                        });

                    }

                }
                //체크 안됐을 때
                else {
                    Log.d("좋아요", "체크 안 됨");
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on below line we are opening a new activity and passing data to it.
                Log.d("PostVAdapter", "리사이클러뷰 아이템 누름"+position);

                if (userName.equals(modal.getName())) {
                    Intent i = new Intent(holder.itemView.getContext(), PostDetailActivity_my.class);
                    Log.d("이름 같", userName+", "+modal.getName());
                    i.putExtra("name", modal.getName());
                    i.putExtra("title", modal.getTitle());
                    i.putExtra("content", modal.getContent());
                    i.putExtra("userName",userName);
                    i.putExtra("namesofliked", modal.getNamesofliked());
                    //on below line we are starting a new activity,
                    holder.itemView.getContext().startActivity(i);
                } else{
                    Intent i = new Intent(holder.itemView.getContext(), PostDetailActivity_else.class);
                    Log.d("이름 다름",userName+", "+modal.getName());
                    i.putExtra("name", modal.getName());
                    i.putExtra("title", modal.getTitle());
                    i.putExtra("content", modal.getContent());
                    i.putExtra("userName",userName);
                    i.putExtra("namesofliked", modal.getNamesofliked());
                    //on below line we are starting a new activity,
                    holder.itemView.getContext().startActivity(i);
                }
            }
        });

    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }


}