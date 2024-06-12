package com.ianmyrfield.githubcodesample

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ianmyrfield.githubcodesample.data.GithubApi
import com.ianmyrfield.githubcodesample.data.UserDataSource
import com.ianmyrfield.githubcodesample.data.UserRepository
import com.ianmyrfield.githubcodesample.ui.search.SearchViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

const val TIME_OUT: Long = 30

val viewModels = module {
    viewModel { SearchViewModel(repo = get()) }
}

val networking = module {
    factory { provideHttpLoggingInterceptor() }
    factory { provideGson() }
    factory { provideOkHttpClient(httpLoggingInterceptor = get()) }
    factory { provideGithubApi(retrofit = get()) }

    single { provideRetrofit(okHttpClient = get(), gson = get()) }
    single<UserDataSource> { UserRepository(api = get()) }
}

fun provideGithubApi(retrofit: Retrofit): GithubApi = retrofit.create(GithubApi::class.java)

fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

fun provideGson(): Gson {
    return GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
        .setLenient()
        .create()
}

fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {

    return OkHttpClient.Builder()
        .apply {
            connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            readTimeout(TIME_OUT, TimeUnit.SECONDS)
            writeTimeout(TIME_OUT, TimeUnit.SECONDS)

            if (BuildConfig.DEBUG) {
                addInterceptor(httpLoggingInterceptor)
            }
        }
        .build()
}

fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor { message -> Timber.tag("GithubAPI").i(message) }
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    return interceptor
}