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

package com.skydoves.entity.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.skydoves.entity.Keyword
import com.skydoves.entity.Review
import com.skydoves.entity.Video
import kotlinx.android.parcel.Parcelize

/**
 * [Tv](https://developers.themoviedb.org/3/discover/tv-discover)
 *
 * An entity class for constructing network & database entity.
 *
 * Discover TV shows by different types of data like average rating, number of votes,
 * genres, the network they aired on and air dates
 * Discover also supports a nice list of sort options. See below for all of the available options.
 */
@Parcelize
@Entity
data class Tv(
  var page: Int,
  var keywords: List<Keyword>? = ArrayList(),
  var videos: List<Video>? = ArrayList(),
  var reviews: List<Review>? = ArrayList(),
  val poster_path: String?,
  val popularity: Float,
  @PrimaryKey val id: Int,
  val backdrop_path: String?,
  val vote_average: Float,
  val overview: String,
  val first_air_date: String?,
  val origin_country: List<String>,
  val genre_ids: List<Int>,
  val original_language: String,
  val vote_count: Int,
  val name: String,
  val original_name: String,
  var favourite: Boolean = false
) : Parcelable
