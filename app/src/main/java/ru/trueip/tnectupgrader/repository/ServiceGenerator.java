package ru.trueip.tnectupgrader.repository;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {


    private static OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

    private static OkHttpClient.Builder httpClient = okHttpClient.newBuilder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass,String baseurl){
        Retrofit retrofit = builder
                .baseUrl(baseurl)
                .client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }

}