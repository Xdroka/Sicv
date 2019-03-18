package com.tcc.sicv.utils

import com.google.gson.Gson

fun Any.toJson() = Gson().toJson(this)