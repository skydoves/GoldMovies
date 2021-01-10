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
import com.skydoves.entity.Keyword
import com.skydoves.entity.Review
import com.skydoves.entity.Video
import kotlinx.android.parcel.Parcelize

/**
 * [Movie](https://developers.themoviedb.org/3/discover/movie-discover)
 *
 * An entity class for constructing network & database entity.
 *
 * Discover movies by different types of data like average rating,
 * number of votes, genres and certifications.
 * You can get a valid list of certifications from the  method.
 */
@Parcelize
@Entity(primaryKeys = [("id")])
data class Movie(
  var page: Int,
  var keywords: List<Keyword>? = ArrayList(),
  var videos: List<Video>? = ArrayList(),
  var reviews: List<Review>? = ArrayList(),
  val poster_path: String?,
  val adult: Boolean,
  val overview: String,
  val release_date: String?,
  val genre_ids: List<Int>,
  val id: Int,
  val original_title: String,
  val original_language: String,
  val title: String,
  val backdrop_path: String?,
  val popularity: Float,
  val vote_count: Int,
  val video: Boolean,
  val vote_average: Float,
  var favourite: Boolean = false
) : Parcelable
