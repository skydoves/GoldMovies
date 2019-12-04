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

import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.ms.square.android.expandabletextview.ExpandableTextView
import com.skydoves.common_ui.R
import com.skydoves.common_ui.extensions.simpleToolbarWithHome

@BindingAdapter("bindExpandableTextView")
fun bindExpandableTextView(expandableTextView: ExpandableTextView, text: String?) {
  expandableTextView.text = text
}

@BindingAdapter("bindFavourite")
fun bindFavourite(imageView: ImageView, favourite: Boolean) {
  if (favourite) {
    imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, R.drawable.ic_heart))
  } else {
    imageView.setImageDrawable(
      ContextCompat.getDrawable(imageView.context, R.drawable.ic_heart_border))
  }
}

@BindingAdapter("observeFavourite")
fun observeFavourite(imageView: ImageView, favourite: Boolean) {
  bindFavourite(imageView, favourite)
}

@BindingAdapter("simpleToolbarWithHome", "simpleToolbarTitle")
fun simpleToolbarWithHome(toolbar: Toolbar, activity: AppCompatActivity, title: String) {
  activity.simpleToolbarWithHome(toolbar, title)
}
