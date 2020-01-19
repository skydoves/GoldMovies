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

package com.skydoves.common_ui.bindings

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.skydoves.common_ui.GlideApp
import com.skydoves.common_ui.PosterPath.getBackdropPath
import com.skydoves.common_ui.R
import com.skydoves.common_ui.extensions.requestGlideListener
import com.skydoves.common_ui.extensions.visible
import com.skydoves.entity.Keyword
import com.skydoves.entity.entities.Movie
import com.skydoves.entity.entities.Person
import com.skydoves.entity.entities.Tv
import com.skydoves.entity.response.PersonDetail
import com.skydoves.whatif.whatIfNotNull
import com.skydoves.whatif.whatIfNotNullOrEmpty

@BindingAdapter("visibilityByModel")
fun visibilityByModel(view: View, anyList: List<Any>?) {
  anyList.whatIfNotNullOrEmpty {
    view.visible()
  }
}

@BindingAdapter("mapKeywordList")
fun bindMapKeywordList(chipGroup: ChipGroup, keywords: List<Keyword>?) {
  keywords.whatIfNotNullOrEmpty {
    chipGroup.visible()
    for (keyword in it) {
      val chip = Chip(chipGroup.context)
      chip.text = keyword.name
      chip.isCheckable = false
      chip.setTextAppearanceResource(R.style.ChipTextStyle)
      chip.setChipBackgroundColorResource(R.color.colorPrimary)
      chipGroup.addView(chip)
    }
  }
}

@BindingAdapter("mapNameTagList")
fun bindTags(chipGroup: ChipGroup, personDetail: PersonDetail?) {
  personDetail?.also_known_as?.whatIfNotNull {
    chipGroup.visible()
    for (nameTag in it) {
      val chip = Chip(chipGroup.context)
      chip.text = nameTag
      chip.isCheckable = false
      chip.setTextAppearanceResource(R.style.ChipTextStyle)
      chip.setChipBackgroundColorResource(R.color.colorPrimary)
      chipGroup.addView(chip)
    }
  }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("bindReleaseDate")
fun bindReleaseDate(view: TextView, movie: Movie) {
  view.text = "Release Date : ${movie.release_date}"
}

@SuppressLint("SetTextI18n")
@BindingAdapter("bindAirDate")
fun bindAirDate(view: TextView, tv: Tv) {
  view.text = "First Air Date : ${tv.first_air_date}"
}

@BindingAdapter("biography")
fun bindBiography(view: TextView, personDetail: PersonDetail?) {
  view.text = personDetail?.biography
}

@BindingAdapter("bindBackDrop")
fun bindBackDrop(view: ImageView, movie: Movie) {
  bindBackDrop(view, movie.backdrop_path, movie.poster_path)
}

@BindingAdapter("bindBackDrop")
fun bindBackDrop(view: ImageView, tv: Tv) {
  bindBackDrop(view, tv.backdrop_path, tv.poster_path)
}

@BindingAdapter("bindBackDrop")
fun bindBackDrop(view: ImageView, person: Person) {
  person.profile_path.whatIfNotNull {
    GlideApp.with(view.context)
      .load(getBackdropPath(it))
      .apply(RequestOptions().circleCrop())
      .into(view)
  }
}

private fun bindBackDrop(view: ImageView, path: String?, posterPath: String?) {
  path.whatIfNotNull(
    whatIf = {
      GlideApp.with(view.context)
        .load(getBackdropPath(it))
        .error(ContextCompat.getDrawable(view.context, R.drawable.not_found))
        .listener(view.requestGlideListener())
        .into(view)
    },
    whatIfNot = {
      GlideApp.with(view.context)
        .load(getBackdropPath(posterPath))
        .error(ContextCompat.getDrawable(view.context, R.drawable.not_found))
        .listener(view.requestGlideListener())
        .into(view)
    }
  )
}
