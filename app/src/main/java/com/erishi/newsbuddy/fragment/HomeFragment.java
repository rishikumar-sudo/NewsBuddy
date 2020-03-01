package com.erishi.newsbuddy.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erishi.newsbuddy.R;
import com.erishi.newsbuddy.activity.MainActivity;
import com.erishi.newsbuddy.adapter.HomeAdapter;
import com.erishi.newsbuddy.models.Model;

import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment  {

    private ArrayList<Model> list=new ArrayList<>();
    RecyclerView recyclerView;
    private HomeAdapter adapter;
    Bundle bundle;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
       recyclerView=view.findViewById(R.id.recyclerview);
      recyclerView.setNestedScrollingEnabled(false);
                adapter=new HomeAdapter(list,getActivity());
                LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
        if (getArguments() != null) {
            list=getArguments().getParcelableArrayList("key");
            adapter.notifyDataSetChanged();
            adapter.onrefresh(list);
        }

        return view;
    }






}
