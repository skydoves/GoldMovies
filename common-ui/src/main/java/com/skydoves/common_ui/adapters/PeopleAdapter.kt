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
import com.skydoves.common_ui.viewholders.PeopleViewHolder
import com.skydoves.entity.entities.Person

/** PeopleAdapter is an adapter class for binding [Person] items. */
class PeopleAdapter(
  private val delegate: PeopleViewHolder.Delegate
) : BaseAdapter() {

  init {
    addSection(ArrayList<Person>())
  }

  fun addPeople(people: List<Person>) {
    sections()[0].addAll(people)
    notifyDataSetChanged()
  }

  override fun layout(sectionRow: SectionRow) = R.layout.item_person

  override fun viewHolder(layout: Int, view: View) = PeopleViewHolder(view, delegate)
}
