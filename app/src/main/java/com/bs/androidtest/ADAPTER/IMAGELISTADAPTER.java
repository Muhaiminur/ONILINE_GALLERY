package com.bs.androidtest.ADAPTER;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bs.androidtest.LIBRARY.Utility;
import com.bs.androidtest.MODEL.GET_IMAGELIST;
import com.bs.androidtest.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class IMAGELISTADAPTER extends RecyclerView.Adapter<IMAGELISTADAPTER.Todo_View_Holder> {
    Context context;
    List<GET_IMAGELIST> getImagelists;
    Utility utility;


    public IMAGELISTADAPTER(List<GET_IMAGELIST> to, Context c) {
        getImagelists = to;
        context = c;
        utility = new Utility(context);
    }

    public class Todo_View_Holder extends RecyclerView.ViewHolder {
        CardView list_view;
        ImageView list_image;
        TextView list_title;

        public Todo_View_Holder(View view) {
            super(view);
            list_view = view.findViewById(R.id.recycler_image);
            list_image = view.findViewById(R.id.list_image);
            list_title = view.findViewById(R.id.list_name);
        }
    }

    @Override
    public IMAGELISTADAPTER.Todo_View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_imagelist, parent, false);
        return new IMAGELISTADAPTER.Todo_View_Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final IMAGELISTADAPTER.Todo_View_Holder holder, int position) {
        final GET_IMAGELIST bodyResponse = getImagelists.get(position);
        try {
            holder.list_title.setText(bodyResponse.getAuthor());
            Glide.with(context).load(bodyResponse.getDownloadUrl()).apply(utility.Glide_Cache_On()).into(holder.list_image);
            holder.list_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        NavOptions.Builder navBuilder = new NavOptions.Builder();
                        navBuilder.setEnterAnim(R.anim.slide_in).setExitAnim(R.anim.slide_out).setPopEnterAnim(R.anim.fade_in).setPopExitAnim(R.anim.fade_out);
                        Bundle bundle = new Bundle();
                        bundle.putString("image_id", bodyResponse.getDownloadUrl());
                        Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                        NavController c = NavHostFragment.findNavController(navhost);
                        //c.popBackStack();
                        c.navigate(R.id.nav_imagedetails, bundle, navBuilder.build());

                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    @Override
    public int getItemCount() {
        return getImagelists.size();
    }

}