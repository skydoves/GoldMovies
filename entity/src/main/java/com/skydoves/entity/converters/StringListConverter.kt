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

package com.skydoves.entity.converters

import androidx.room.TypeConverter

/** StringListConverter is a converter class for type converting a entity. */
open class StringListConverter : BaseMoshiConverter() {

  @TypeConverter
  fun fromString(value: String): List<String>? {
    return getAdapter<List<String>?>().fromJson(value)
  }

  @TypeConverter
  fun fromList(list: List<String>?): String {
    return getAdapter<List<String>?>().toJson(list)
  }
}
