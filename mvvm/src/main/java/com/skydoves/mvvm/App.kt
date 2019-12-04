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

package com.skydoves.mvvm

import com.jakewharton.threetenabp.AndroidThreeTen
import com.skydoves.mvvm.di.DaggerAppComponent
import dagger.android.DaggerApplication
import timber.log.Timber

@Suppress("unused")
class App : DaggerApplication() {

  private val appComponent = DaggerAppComponent.builder()
    .application(this)
    .build()

  override fun onCreate() {
    super.onCreate()
    AndroidThreeTen.init(this)

    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }

  override fun applicationInjector() = appComponent
}
