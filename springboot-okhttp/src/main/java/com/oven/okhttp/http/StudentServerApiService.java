package com.oven.okhttp.http;

import com.alibaba.fastjson.JSONObject;
import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.Map;

@RetrofitClient(baseUrl = "${remote.baseUrl.host}", sourceOkHttpClient = "OvenOkHttpClient")
public interface StudentServerApiService {

    @POST("{path}")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Response<String> getStudent(@Path(value = "path", encoded = true) String path,
                                @HeaderMap Map<String, String> headers,
                                @QueryMap Map<String, String> params,
                                @Body JSONObject body);

}
