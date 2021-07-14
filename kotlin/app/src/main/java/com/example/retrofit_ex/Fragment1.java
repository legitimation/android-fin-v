package com.example.retrofit_ex;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
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


public class Fragment1 extends Fragment {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
//    private String BASE_URL = "http://172.10.18.137:80";
    private String BASE_URL = "http://172.10.18.137:80";
    String name;
    String email;
    Button Signout;
    TextView NAME;
    TextView EMAIL;

    private RecyclerView per_postRV1, per_postRV2;
    private PostVAdapter postRVAdapter;
    ArrayList<PostInfo> Postlist=new ArrayList<PostInfo>();
    ArrayList<PostInfo> personalPostlist=new ArrayList<PostInfo>();
    ArrayList<PostInfo> likedPostlist=new ArrayList<PostInfo>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent= getActivity().getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        Log.d("정보",name + email);




        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_1, container, false);

        NAME = view.findViewById(R.id.name);
        EMAIL = view.findViewById(R.id.email);
        NAME.setText("User: " + name);
        EMAIL.setText("Email: " + email);

        Signout=view.findViewById(R.id.Signout);

        Signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete 선택").setMessage("정말 탈퇴하시겠습니까?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("name", name);
                        Log.d("회원 삭제", name);
                        Call<Void> call = retrofitInterface.deleteUser(map);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.code() == 200) {
                                    Toast.makeText(getActivity(),
                                            "Deleted successfully, return to login", Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(getContext(), LoginActivity.class);
                                    startActivity(intent);
                                } else if (response.code() == 400) {
                                    Toast.makeText(getActivity(),
                                            "There's no such User", Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(getActivity(), t.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Toast.makeText(getActivity(), "Cancel Click", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        Call<ResponseBody> call = retrofitInterface.getPost();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //List<JSONObject> result= response.body();
                //showPostResult
                if (response.code() == 500) {
                    Toast.makeText(getActivity(),
                            "database has failed", Toast.LENGTH_LONG).show();
                } else if (response.code() == 200) {
                    Toast.makeText(getActivity(),
                            "Uploaded successfully", Toast.LENGTH_LONG).show();

                    Gson gson = new Gson();
                    try {
                        String jsonString = response.body().string();
                        Log.d("받아온 제이슨", jsonString);
                        List<PostInfo> list = gson.fromJson(jsonString, new TypeToken<List<PostInfo>>() {}.getType());

                        for (int i = 0; i < list.size(); i++) {
                            Postlist.add(new PostInfo(list.get(i).getName(), list.get(i).getTitle(), list.get(i).getContent(), list.get(i).getNamesofliked()));
                        }


                        for (int i = 0; i < Postlist.size(); i++) {
                            if (name.equals(Postlist.get(i).getName())) {
                                personalPostlist.add(new PostInfo(Postlist.get(i).getName(), Postlist.get(i).getTitle(), Postlist.get(i).getContent(), Postlist.get(i).getNamesofliked()));
                            }
                        }

                        for (int i = 0; i < Postlist.size(); i++) {
                            if (Postlist.get(i).getNamesofliked().contains(name)) {
                                likedPostlist.add(new PostInfo(Postlist.get(i).getName(), Postlist.get(i).getTitle(), Postlist.get(i).getContent(), Postlist.get(i).getNamesofliked()));
                            }
                        }


                        Button myposts=getView().findViewById(R.id.myposts);
                        myposts.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                View view = getLayoutInflater().inflate(R.layout.f1_myposts, null);

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setView(view).show();

                                per_postRV1 = view.findViewById(R.id.idRVContacts);
                                //on below line we are setting layout mnager.
                                per_postRV1.setLayoutManager(new LinearLayoutManager(getContext()));
                                per_postRV1.addItemDecoration(new DividerItemDecoration(getView().getContext(), 1));

                                postRVAdapter = new PostVAdapter(personalPostlist, name);
                                //on below line we are setting adapter to our recycler view.
                                per_postRV1.setAdapter(postRVAdapter);
                            }
                        });

                        Button likedposts=getView().findViewById(R.id.likedposts);
                        likedposts.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                View view = getLayoutInflater().inflate(R.layout.f1_likedposts, null);

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setView(view).show();

                                per_postRV2 = view.findViewById(R.id.idRVContacts);
                                //on below line we are setting layout mnager.
                                per_postRV2.setLayoutManager(new LinearLayoutManager(getContext()));
                                per_postRV2.addItemDecoration(new DividerItemDecoration(getView().getContext(), 1));

                                postRVAdapter = new PostVAdapter(likedPostlist, name);
                                //on below line we are setting adapter to our recycler view.
                                per_postRV2.setAdapter(postRVAdapter);
                            }
                        });


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });


    }
    public void onResume() {
        super.onResume();
        personalPostlist=new ArrayList<PostInfo>();
        likedPostlist=new ArrayList<PostInfo>();
        Postlist=new ArrayList<PostInfo>();
    }
}