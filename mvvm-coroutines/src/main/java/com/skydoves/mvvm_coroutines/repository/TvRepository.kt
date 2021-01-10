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

package com.skydoves.mvvm_coroutines.repository

import androidx.annotation.WorkerThread
import com.skydoves.entity.database.TvDao
import com.skydoves.entity.entities.Tv
import com.skydoves.network.service.TvService
import com.skydoves.sandwich.suspendOnSuccess
import com.skydoves.whatif.whatIfNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class TvRepository constructor(
  private val tvService: TvService,
  private val tvDao: TvDao
) : Repository {

  init {
    Timber.d("Injection TvRepository")
  }

  @WorkerThread
  fun loadKeywordList(id: Int, success: () -> Unit) = flow {
    val tv = tvDao.getTv(id)
    var keywords = tv.keywords
    if (keywords.isNullOrEmpty()) {
      val response = tvService.fetchKeywords(id)
      response.suspendOnSuccess {
        data.whatIfNotNull {
          keywords = data!!.keywords
          tv.keywords = keywords
          tvDao.updateTv(tv)
          emit(keywords)
          success()
        }
      }
    } else {
      emit(keywords)
      success()
    }
  }.flowOn(Dispatchers.IO)

  @WorkerThread
  fun loadVideoList(id: Int, success: () -> Unit) = flow {
    val tv = tvDao.getTv(id)
    var videos = tv.videos
    if (videos.isNullOrEmpty()) {
      val response = tvService.fetchVideos(id)
      response.suspendOnSuccess {
        data.whatIfNotNull {
          videos = data!!.results
          tv.videos = videos
          tvDao.updateTv(tv)
          emit(videos)
          success()
        }
      }
    } else {
      emit(videos)
      success()
    }
  }.flowOn(Dispatchers.IO)

  @WorkerThread
  fun loadReviewsList(id: Int, success: () -> Unit) = flow {
    val tv = tvDao.getTv(id)
    var reviews = tv.reviews
    if (reviews.isNullOrEmpty()) {
      val response = tvService.fetchReviews(id)
      response.suspendOnSuccess {
        data.whatIfNotNull {
          reviews = data!!.results
          tv.reviews = reviews
          tvDao.updateTv(tv)
          emit(reviews)
          success()
        }
      }
    } else {
      emit(reviews)
      success()
    }
  }.flowOn(Dispatchers.IO)

  fun getTv(id: Int) = tvDao.getTv(id)

  fun onClickFavourite(tv: Tv): Boolean {
    tv.favourite = !tv.favourite
    tvDao.updateTv(tv)
    return tv.favourite
  }
}
