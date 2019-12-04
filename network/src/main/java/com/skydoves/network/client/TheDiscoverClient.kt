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

import com.skydoves.entity.response.DiscoverMovieResponse
import com.skydoves.entity.response.DiscoverTvResponse
import com.skydoves.network.ApiResponse
import com.skydoves.network.service.TheDiscoverService
import com.skydoves.network.transform

/** TheDiscoverClient is a UseCase of the [TheDiscoverService] interface. */
class TheDiscoverClient(private val service: TheDiscoverService) {

  fun fetchDiscoverMovie(
    page: Int,
    onResult: (response: ApiResponse<DiscoverMovieResponse>) -> Unit
  ) {
    this.service.fetchDiscoverMovie(page).transform(onResult)
  }

  fun fetchDiscoverTv(
    page: Int,
    onResult: (response: ApiResponse<DiscoverTvResponse>) -> Unit
  ) {
    this.service.fetchDiscoverTv(page).transform(onResult)
  }
}
