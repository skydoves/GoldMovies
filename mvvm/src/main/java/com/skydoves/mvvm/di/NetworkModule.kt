/*
 * Designed and developed by 2019 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.mvvm.di

import com.skydoves.network.EndPoint
import com.skydoves.network.RequestInterceptor
import com.skydoves.network.client.MovieClient
import com.skydoves.network.client.PeopleClient
import com.skydoves.network.client.TheDiscoverClient
import com.skydoves.network.client.TvClient
import com.skydoves.network.service.MovieService
import com.skydoves.network.service.PeopleService
import com.skydoves.network.service.TheDiscoverService
import com.skydoves.network.service.TvService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

  @Provides
  @Singleton
  fun provideHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
      .addInterceptor(RequestInterceptor())
      .build()
  }

  @Provides
  @Singleton
  fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
      .client(okHttpClient)
      .baseUrl(EndPoint.TheMovieDB)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  @Provides
  @Singleton
  fun provideDiscoverService(retrofit: Retrofit): TheDiscoverService {
    return retrofit.create(TheDiscoverService::class.java)
  }

  @Provides
  @Singleton
  fun provideDiscoverClient(theDiscoverService: TheDiscoverService): TheDiscoverClient {
    return TheDiscoverClient(theDiscoverService)
  }

  @Provides
  @Singleton
  fun providePeopleService(retrofit: Retrofit): PeopleService {
    return retrofit.create(PeopleService::class.java)
  }

  @Provides
  @Singleton
  fun providePeopleClient(peopleService: PeopleService): PeopleClient {
    return PeopleClient(peopleService)
  }

  @Provides
  @Singleton
  fun provideMovieService(retrofit: Retrofit): MovieService {
    return retrofit.create(MovieService::class.java)
  }

  @Provides
  @Singleton
  fun provideMovieClient(movieService: MovieService): MovieClient {
    return MovieClient(movieService)
  }

  @Provides
  @Singleton
  fun provideTvService(retrofit: Retrofit): TvService {
    return retrofit.create(TvService::class.java)
  }

  @Provides
  @Singleton
  fun provideTvClient(tvService: TvService): TvClient {
    return TvClient(tvService)
  }
}
