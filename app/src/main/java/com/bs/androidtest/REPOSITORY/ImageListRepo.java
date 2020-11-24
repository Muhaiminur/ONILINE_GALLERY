package com.bs.androidtest.REPOSITORY;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.bs.androidtest.HTTP.ApiClient;
import com.bs.androidtest.HTTP.ApiInterface;
import com.bs.androidtest.LIBRARY.Utility;
import com.bs.androidtest.MODEL.GET_IMAGELIST;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageListRepo {
    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
    private MutableLiveData<List<GET_IMAGELIST>> responseMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> progressbarObservable = new MutableLiveData<>();
    Utility utility = new Utility();

    public void get_image_list() {
        try {
            progressbarObservable.postValue(true);
            Call<List<GET_IMAGELIST>> call = apiInterface.GET_IMAGE_LIST();
            call.enqueue(new Callback<List<GET_IMAGELIST>>() {
                @Override
                public void onResponse(Call<List<GET_IMAGELIST>> call, Response<List<GET_IMAGELIST>> response) {
                    try {
                        utility.logger("image list" + response.toString());
                        progressbarObservable.postValue(false);
                        if (response.isSuccessful() && response != null && response.code() == 200) {
                            List<GET_IMAGELIST> api_response = response.body();
                            Log.d("image list", api_response.toString());
                            responseMutableLiveData.postValue(api_response);
                        } else {
                            responseMutableLiveData.postValue(response.body());
                            Log.d("image list", "FAILed");
                        }
                    } catch (Exception e) {
                        progressbarObservable.postValue(false);
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }

                @Override
                public void onFailure(Call<List<GET_IMAGELIST>> call, Throwable t) {
                    progressbarObservable.postValue(false);
                    Log.d("Error", t.toString());
                    //bankMutableLiveData.postValue(null);
                }
            });
        } catch (Exception e) {
            progressbarObservable.postValue(false);
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }


    }

    public LiveData<List<GET_IMAGELIST>> getApi_responseLiveData() {
        return responseMutableLiveData;
    }

    public MutableLiveData<Boolean> getProgress() {
        return progressbarObservable;
    }
}
