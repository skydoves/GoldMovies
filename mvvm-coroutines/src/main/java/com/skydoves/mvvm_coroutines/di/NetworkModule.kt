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

package com.skydoves.mvvm_coroutines.di

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
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {
  single {
    OkHttpClient.Builder()
      .addInterceptor(RequestInterceptor())
      .build()
  }

  single {
    Retrofit.Builder()
      .client(get<OkHttpClient>())
      .baseUrl(EndPoint.TheMovieDB)
      .addConverterFactory(MoshiConverterFactory.create())
      .build()
  }

  single { get<Retrofit>().create(TheDiscoverService::class.java) }

  single { TheDiscoverClient(get()) }

  single { get<Retrofit>().create(PeopleService::class.java) }

  single { PeopleClient(get()) }

  single { get<Retrofit>().create(MovieService::class.java) }

  single { MovieClient(get()) }

  single { get<Retrofit>().create(TvService::class.java) }

  single { TvClient(get()) }
}
