package com.example.mobilepowermap.Utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;


public class RetrofitClient {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://powermap-production-a419.up.railway.app";

    public static Retrofit getRetrofitInstance(String token) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        // Adiciona o token ao cabeçalho se fornecido
        if (token != null && !token.isEmpty()) {
            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Authorization", "Bearer " + token)
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            });
        }

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }

        return retrofit;
    }
}
