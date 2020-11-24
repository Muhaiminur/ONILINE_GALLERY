package com.bs.androidtest.VIEWMODEL;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bs.androidtest.MODEL.GET_IMAGELIST;
import com.bs.androidtest.REPOSITORY.ImageListRepo;

import java.util.List;

public class ImageListViewViewModel extends AndroidViewModel {
    private ImageListRepo imageListRepo;
    private LiveData<List<GET_IMAGELIST>> listLiveData;
    private MutableLiveData<Boolean> progressbarObservable;

    public ImageListViewViewModel(@NonNull Application application) {
        super(application);
        imageListRepo = new ImageListRepo();
        listLiveData = imageListRepo.getApi_responseLiveData();
        progressbarObservable = imageListRepo.getProgress();
    }

    public void get_image_list() {
        imageListRepo.get_image_list();
    }

    public LiveData<List<GET_IMAGELIST>> get_own_repo_list() {
        return listLiveData;
    }


    public MutableLiveData<Boolean> getProgressbar() {
        return progressbarObservable;
    }
}