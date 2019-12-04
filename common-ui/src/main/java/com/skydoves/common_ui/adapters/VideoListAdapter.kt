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

package com.skydoves.common_ui.adapters

import android.view.View
import com.skydoves.baserecyclerviewadapter.BaseAdapter
import com.skydoves.baserecyclerviewadapter.SectionRow
import com.skydoves.common_ui.R
import com.skydoves.common_ui.viewholders.VideoListViewHolder
import com.skydoves.entity.Video

/** TvFavouriteListAdapter is an adapter class for binding [Video] items. */
class VideoListAdapter : BaseAdapter() {

  init {
    addSection(ArrayList<Video>())
  }

  fun addVideoList(videos: List<Video>) {
    val section = sections()[0]
    section.addAll(videos)
    notifyItemRangeInserted(section.size - videos.size + 1, videos.size)
  }

  override fun layout(sectionRow: SectionRow) = R.layout.item_video

  override fun viewHolder(layout: Int, view: View) = VideoListViewHolder(view)
}
