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

package com.skydoves.network.client

import com.skydoves.entity.response.KeywordListResponse
import com.skydoves.entity.response.ReviewListResponse
import com.skydoves.entity.response.VideoListResponse
import com.skydoves.network.ApiResponse
import com.skydoves.network.service.TvService
import com.skydoves.network.transform

/** TvClient is a UseCase of the [TvService] interface. */
class TvClient(private val service: TvService) {

  fun fetchKeywords(
    id: Int,
    onResult: (response: ApiResponse<KeywordListResponse>) -> Unit
  ) {
    this.service.fetchKeywords(id).transform(onResult)
  }

  fun fetchVideos(
    id: Int,
    onResult: (response: ApiResponse<VideoListResponse>) -> Unit
  ) {
    this.service.fetchVideos(id).transform(onResult)
  }

  fun fetchReviews(
    id: Int,
    onResult: (response: ApiResponse<ReviewListResponse>) -> Unit
  ) {
    this.service.fetchReviews(id).transform(onResult)
  }
}
