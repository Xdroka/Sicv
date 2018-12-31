package com.tcc.sicv.data.remote.services;

import com.tcc.sicv.data.remote.model.UserResponse;
import com.tcc.sicv.presentation.model.User;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserWebService {
    @POST("users")
    Flowable<UserResponse> signIn(@Body User user);

}
