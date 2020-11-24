package com.bs.androidtest.VIEW.FRAGMENT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bs.androidtest.ADAPTER.IMAGELISTADAPTER;
import com.bs.androidtest.LIBRARY.Utility;
import com.bs.androidtest.MODEL.GET_IMAGELIST;
import com.bs.androidtest.R;
import com.bs.androidtest.VIEWMODEL.ImageListViewViewModel;
import com.bs.androidtest.databinding.ImageListViewFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class ImageListView extends Fragment {
    ImageListViewFragmentBinding imageListViewFragmentBinding;
    Context context;
    Utility utility;
    List<GET_IMAGELIST> get_imagelists;
    IMAGELISTADAPTER imagelistadapter;

    private ImageListViewViewModel imageListViewViewModel;

    public static ImageListView newInstance() {
        return new ImageListView();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (imageListViewFragmentBinding == null) {
            imageListViewFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.image_list_view_fragment, container, false);
            try {
                if (context == null) {
                    context = getActivity();
                    utility = new Utility(context);
                }
                initial_list();
            } catch (Exception e) {
                Log.d("Error Line Number", Log.getStackTraceString(e));
            }
        }
        return imageListViewFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            context = getActivity();
            utility = new Utility(context);
            imageListViewViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(ImageListViewViewModel.class);
            observeprogressbar();
            if (get_imagelists != null && get_imagelists.size() == 0) {
                imageListViewViewModel.get_image_list();
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    public void initial_list() {
        get_imagelists = new ArrayList<>();
        imagelistadapter = new IMAGELISTADAPTER(get_imagelists, context);
        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        imageListViewFragmentBinding.imagelistRecycler.setLayoutManager(new GridLayoutManager(context, 2));
        imageListViewFragmentBinding.imagelistRecycler.setItemAnimator(new DefaultItemAnimator());
        imageListViewFragmentBinding.imagelistRecycler.setAdapter(imagelistadapter);
    }

    private void observeprogressbar() {
        imageListViewViewModel.getProgressbar().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(final Boolean progressObserve) {
                if (progressObserve) {
                    utility.showProgress(false, "LOADING");
                } else {
                    utility.hideProgress();
                }
            }
        });
        imageListViewViewModel.get_own_repo_list().observe(getActivity(), new Observer<List<GET_IMAGELIST>>() {
            @Override
            public void onChanged(List<GET_IMAGELIST> githubrepo) {
                try {
                    if (githubrepo != null && githubrepo.size() > 0) {
                        utility.logger("get Image list" + githubrepo.size());
                        get_imagelists.clear();
                        get_imagelists.addAll(githubrepo);
                        imagelistadapter.notifyDataSetChanged();
                    } else {
                        utility.showToast(context.getResources().getString(R.string.no_data_string));
                    }
                } catch (Exception e) {
                    Log.d("Error Line Number", Log.getStackTraceString(e));
                }
            }
        });
    }

}