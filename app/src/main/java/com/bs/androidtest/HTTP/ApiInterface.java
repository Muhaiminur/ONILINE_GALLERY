package com.bs.androidtest.HTTP;

import com.bs.androidtest.MODEL.GET_IMAGELIST;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {
    //1 get image list
    @GET("v2/list")
    Call<List<GET_IMAGELIST>> GET_IMAGE_LIST();

}
