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

package com.skydoves.mvvm.repository

import androidx.lifecycle.MutableLiveData
import com.skydoves.entity.Keyword
import com.skydoves.entity.Review
import com.skydoves.entity.Video
import com.skydoves.entity.database.TvDao
import com.skydoves.entity.entities.Tv
import com.skydoves.network.ApiResponse
import com.skydoves.network.client.TvClient
import com.skydoves.network.message
import javax.inject.Inject
import javax.inject.Singleton
import timber.log.Timber

@Singleton
class TvRepository @Inject constructor(
  private val tvClient: TvClient,
  private val tvDao: TvDao
) : Repository {

  override var isLoading: Boolean = false

  init {
    Timber.d("Injection TvRepository")
  }

  fun loadKeywordList(id: Int, error: (String) -> Unit): MutableLiveData<List<Keyword>> {
    val liveData = MutableLiveData<List<Keyword>>()
    val tv = tvDao.getTv(id)
    var keywords = tv.keywords
    if (keywords.isNullOrEmpty()) {
      this.isLoading = true
      tvClient.fetchKeywords(id) { response ->
        this.isLoading = false
        when (response) {
          is ApiResponse.Success -> {
            response.data?.let { data ->
              keywords = data.keywords
              tv.keywords = keywords
              liveData.postValue(keywords)
              tvDao.updateTv(tv)
            }
          }
          is ApiResponse.Failure.Error -> error(response.message())
          is ApiResponse.Failure.Exception -> error(response.message())
        }
      }
    }
    liveData.postValue(keywords)
    return liveData
  }

  fun loadVideoList(id: Int, error: (String) -> Unit): MutableLiveData<List<Video>> {
    val liveData = MutableLiveData<List<Video>>()
    val tv = tvDao.getTv(id)
    var videos = tv.videos
    if (videos.isNullOrEmpty()) {
      this.isLoading = true
      tvClient.fetchVideos(id) { response ->
        this.isLoading = false
        when (response) {
          is ApiResponse.Success -> {
            response.data?.let { data ->
              videos = data.results
              tv.videos = videos
              liveData.postValue(videos)
              tvDao.updateTv(tv)
            }
          }
          is ApiResponse.Failure.Error -> error(response.message())
          is ApiResponse.Failure.Exception -> error(response.message())
        }
      }
    }
    liveData.postValue(videos)
    return liveData
  }

  fun loadReviewsList(id: Int, error: (String) -> Unit): MutableLiveData<List<Review>> {
    val liveData = MutableLiveData<List<Review>>()
    val tv = tvDao.getTv(id)
    var reviews = tv.reviews
    if (reviews.isNullOrEmpty()) {
      this.isLoading = true
      tvClient.fetchReviews(id) { response ->
        this.isLoading = false
        when (response) {
          is ApiResponse.Success -> {
            response.data?.let { data ->
              reviews = data.results
              tv.reviews = reviews
              liveData.postValue(reviews)
              tvDao.updateTv(tv)
            }
          }
          is ApiResponse.Failure.Error -> error(response.message())
          is ApiResponse.Failure.Exception -> error(response.message())
        }
      }
    }
    liveData.postValue(reviews)
    return liveData
  }

  fun getTv(id: Int) = tvDao.getTv(id)

  fun onClickFavourite(tv: Tv): Boolean {
    tv.favourite = !tv.favourite
    tvDao.updateTv(tv)
    return tv.favourite
  }
}
