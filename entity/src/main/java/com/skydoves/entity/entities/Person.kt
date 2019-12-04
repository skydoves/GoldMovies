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
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.skydoves.entity.response.PersonDetail
import kotlinx.android.parcel.Parcelize

/**
 * [People](https://developers.themoviedb.org/3/people/get-popular-people)
 *
 * An entity class for constructing network & database entity.
 *
 * Get the list of popular people on TMDb. This list updates daily.
 */
@Parcelize
@Entity(tableName = "People")
data class Person(
  var page: Int,
  @Embedded var personDetail: PersonDetail? = null,
  val profile_path: String?,
  val adult: Boolean,
  @PrimaryKey val id: Int,
  val name: String,
  val popularity: Float
) : Parcelable
