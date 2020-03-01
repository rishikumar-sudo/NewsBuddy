package com.erishi.newsbuddy.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.erishi.newsbuddy.R;
import com.erishi.newsbuddy.fragment.HomeFragment;
import com.erishi.newsbuddy.models.Model;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
  private   List<Model> list;
  private Context context;

    public HomeAdapter(List<Model> list, Context context) {
        this.list = list;
        this.context = context;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_list,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int position) {

        Model model = list.get(position);
        String urlimg = model.getImage();
        Log.d(TAG, model.getAuthor());
       // myViewHolder.btn.setText(model.getTitle());
        myViewHolder.title.setText(model.getTitle());
        Glide.with(context)
                .load(urlimg)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        myViewHolder.progressBar.setVisibility(View.VISIBLE);
                        myViewHolder.imageView.setImageResource(R.drawable.ic_favorite_black_24dp);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        myViewHolder.progressBar.setVisibility(View.INVISIBLE);
                        
                    
                        return false;
                    }
                })
                .into(myViewHolder.imageView);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void onrefresh(List<Model> list) {
        this.list.clear();
        this.list =list;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        final ImageView imageView;
        final ProgressBar progressBar;

        final CardView cardView;
        final TextView title;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.home_imgview);
            progressBar=itemView.findViewById(R.id.progress);

            title=itemView.findViewById(R.id.title);
            cardView=itemView.findViewById(R.id.cardview);

        }
    }
}
