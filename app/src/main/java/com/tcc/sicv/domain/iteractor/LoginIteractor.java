package com.tcc.sicv.domain.iteractor;

import com.tcc.sicv.domain.model.Result;
import com.tcc.sicv.presentation.model.User;

public interface LoginIteractor {
    public Result<User> sigin(User user);
}
