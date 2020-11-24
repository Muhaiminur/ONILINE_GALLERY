package com.bs.androidtest.VIEW.FRAGMENT;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bs.androidtest.LIBRARY.Utility;
import com.bs.androidtest.R;
import com.bs.androidtest.VIEWMODEL.ImageDetailsViewViewModel;
import com.bs.androidtest.VIEWMODEL.ImageListViewViewModel;
import com.bs.androidtest.databinding.ImageDetailsViewFragmentBinding;
import com.bumptech.glide.Glide;

public class ImageDetailsView extends Fragment {

    ImageDetailsViewFragmentBinding detailsViewFragmentBinding;
    Context context;
    Utility utility;
    String image_url;
    private ImageDetailsViewViewModel imageDetailsViewViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (detailsViewFragmentBinding == null) {
            detailsViewFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.image_details_view_fragment, container, false);
            try {
                if (context == null) {
                    context = getActivity();
                    utility = new Utility(context);
                }if (getArguments() != null) {
                    image_url = getArguments().getString("image_id");
                    utility.logger("image found"+image_url);
                    if (image_url != null && !TextUtils.isEmpty(image_url)) {
                        utility.logger("image"+image_url);
                        Glide.with(context).load(image_url).apply(utility.Glide_Cache_On()).into(detailsViewFragmentBinding.imageDetails);
                    }else {
                        utility.logger("image"+"not found");
                    }
                }else {
                    utility.logger("image no");
                }
            } catch (Exception e) {
                Log.d("Error Line Number", Log.getStackTraceString(e));
            }
        }
        return detailsViewFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            context = getActivity();
            utility = new Utility(context);
            imageDetailsViewViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(ImageDetailsViewViewModel.class);
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }

    }

}