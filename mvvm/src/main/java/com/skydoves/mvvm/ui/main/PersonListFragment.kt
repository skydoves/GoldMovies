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

package com.skydoves.mvvm.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.skydoves.baserecyclerviewadapter.RecyclerViewPaginator
import com.skydoves.common_ui.adapters.PeopleAdapter
import com.skydoves.common_ui.viewholders.PeopleViewHolder
import com.skydoves.entity.entities.Person
import com.skydoves.mvvm.R
import com.skydoves.mvvm.base.ViewModelFragment
import com.skydoves.mvvm.databinding.FragmentPeopleBinding
import com.skydoves.mvvm.ui.details.person.PersonDetailActivity
import kotlinx.android.synthetic.main.fragment_people.recyclerView

class PersonListFragment :
  ViewModelFragment<FragmentPeopleBinding>(R.layout.fragment_people), PeopleViewHolder.Delegate {

  private val viewModel: MainActivityViewModel by injectActivityVIewModels()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    super.onCreateView(inflater, container, savedInstanceState)
    return binding {
      viewModel = this@PersonListFragment.viewModel
      lifecycleOwner = this@PersonListFragment
      adapter = PeopleAdapter(this@PersonListFragment)
    }.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initializeUI()
  }

  private fun initializeUI() {
    RecyclerViewPaginator(
      recyclerView = recyclerView,
      isLoading = { viewModel.isLoading },
      loadMore = { loadMore(it) },
      onLast = { false }
    ).apply {
      threshold = 4
      currentPage = 1
    }
  }

  private fun loadMore(page: Int) = this.viewModel.postPeoplePage(page)

  override fun onItemClick(person: Person, view: View) =
    PersonDetailActivity.startActivity(activity, person.id, view)
}
