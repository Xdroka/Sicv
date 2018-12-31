package com.tcc.sicv.domain.iteractor;

import com.tcc.sicv.data.remote.model.UserResponse;
import com.tcc.sicv.data.remote.services.UserWebService;
import com.tcc.sicv.domain.model.Result;
import com.tcc.sicv.presentation.model.User;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import rx.android.schedulers.AndroidSchedulers;

public class LoginIteractorImpl implements LoginIteractor {
    private UserWebService service;

    public LoginIteractorImpl(UserWebService service) {
        this.service = service;
    }

    @Override
    public Result<User> sigin(User user) {
//        service.signIn(user)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .toObservable()
//                .subscribeWith(new DisposableObserver<UserResponse>() {
//                    @Override
//                    public void onNext(UserResponse value) {
//                        if (true) {
//                            return new Result.Error(
//                                    new Exception(
//                                            "HTTP: ${response.code()} - ${response.message()}"
//                                    )
//                            );
//                            return
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//
//                    override fun onComplete() {}
//
//            override fun onNext( Response<ListEnterprises> response) {
//
//                }
//                searchFound(
//                        response.body()?.enterprises?.convertListOfEnterprises()
//                        ?: listOf(Enterprise())
//                        )
//
//            }
//
//            override fun onError(exception: Throwable) {
//                errorSearch(exception)
//            }
//        })
        return null;
    }
}
