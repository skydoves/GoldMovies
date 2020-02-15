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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skydoves.mvvm.di.annotations.ViewModelKey
import com.skydoves.mvvm.ui.details.movie.MovieDetailViewModel
import com.skydoves.mvvm.ui.details.person.PersonDetailViewModel
import com.skydoves.mvvm.ui.details.tv.TvDetailViewModel
import com.skydoves.mvvm.ui.main.MainActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
internal abstract class ViewModelModule {

  @Binds
  @IntoMap
  @ViewModelKey(MainActivityViewModel::class)
  internal abstract fun bindMainActivityViewModels(mainActivityViewModel: MainActivityViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(MovieDetailViewModel::class)
  internal abstract fun bindMovieDetailActivityViewModels(movieDetailViewModel: MovieDetailViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(TvDetailViewModel::class)
  internal abstract fun bindTvDetailActivityViewModels(tvDetailViewModel: TvDetailViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(PersonDetailViewModel::class)
  internal abstract fun bindPersonDetailActivityViewModels(personDetailViewModel: PersonDetailViewModel): ViewModel

  @Binds
  internal abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory
}
