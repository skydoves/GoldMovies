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

package com.skydoves.mvvm_coroutines.ui.details.tv

import androidx.annotation.MainThread
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.skydoves.entity.Keyword
import com.skydoves.entity.Review
import com.skydoves.entity.Video
import com.skydoves.entity.entities.Tv
import com.skydoves.mvvm_coroutines.base.LiveCoroutinesViewModel
import com.skydoves.mvvm_coroutines.repository.TvRepository
import com.skydoves.network.extensions.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

class TvDetailViewModel constructor(
  private val tvRepository: TvRepository
) : LiveCoroutinesViewModel() {

  private val tvIdLiveData: MutableStateFlow<Int> = MutableStateFlow(0)

  val keywordListLiveData: LiveData<List<Keyword>?>
  val videoListLiveData: LiveData<List<Video>?>
  val reviewListLiveData: LiveData<List<Review>?>
  val toastLiveData: MutableLiveData<String> = MutableLiveData()

  private lateinit var tv: Tv
  val favourite = ObservableBoolean()

  val isLoading: ObservableBoolean = ObservableBoolean(false)

  init {
    Timber.d("Injection TvDetailViewModel")

    this.keywordListLiveData = tvIdLiveData.asLiveData().switchMap { id ->
      launchOnViewModelScope {
        isLoading.set(true)
        tvRepository.loadKeywordList(id) {
          isLoading.set(false)
        }.asLiveData()
      }
    }

    this.videoListLiveData = tvIdLiveData.asLiveData().switchMap { id ->
      launchOnViewModelScope {
        isLoading.set(true)
        tvRepository.loadVideoList(id) {
          isLoading.set(false)
        }.asLiveData()
      }
    }

    this.reviewListLiveData = tvIdLiveData.asLiveData().switchMap { id ->
      launchOnViewModelScope {
        isLoading.set(true)
        tvRepository.loadReviewsList(id) {
          isLoading.set(false)
        }.asLiveData()
      }
    }
  }

  @MainThread
  fun postTvId(id: Int) {
    this.tvIdLiveData.setValue(id)
    this.tv = tvRepository.getTv(id)
    this.favourite.set(this.tv.favourite)
  }

  fun getTv() = this.tv

  fun onClickedFavourite(tv: Tv) =
    favourite.set(tvRepository.onClickFavourite(tv))
}
