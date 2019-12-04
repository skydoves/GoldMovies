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

package com.skydoves.common_ui.viewholders

import android.view.View
import com.skydoves.baserecyclerviewadapter.BaseViewHolder
import com.skydoves.common_ui.databinding.ItemTvFavouriteBinding
import com.skydoves.entity.entities.Tv
import kotlinx.android.synthetic.main.item_tv_favourite.view.*

/** TvFavouriteListViewHolder is a viewHolder class for binding a [Tv] item. */
class TvFavouriteListViewHolder(
  view: View,
  private val delegate: Delegate
) : BaseViewHolder(view) {

  interface Delegate {
    fun onItemClick(tv: Tv)
  }

  private lateinit var tv: Tv
  private val binding by bindings<ItemTvFavouriteBinding>(view)

  override fun bindData(data: Any) {
    if (data is Tv) {
      tv = data
      binding.apply {
        tv = data
        palette = itemView.item_poster_palette
        executePendingBindings()
      }
    }
  }

  override fun onClick(v: View?) = delegate.onItemClick(tv)

  override fun onLongClick(p0: View?) = false
}
