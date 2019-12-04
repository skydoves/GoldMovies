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

package com.skydoves.mvvm.di

import android.app.Application
import androidx.annotation.NonNull
import androidx.room.Room
import com.skydoves.entity.database.AppDatabase
import com.skydoves.entity.database.MovieDao
import com.skydoves.entity.database.PeopleDao
import com.skydoves.entity.database.TvDao
import com.skydoves.entity.database.migrations.Migration1_2
import com.skydoves.entity.database.migrations.Migration2_3
import com.skydoves.mvvm.R
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PersistenceModule {

  @Provides
  @Singleton
  fun provideDatabase(@NonNull application: Application): AppDatabase {
    return Room
      .databaseBuilder(application, AppDatabase::class.java,
        application.getString(R.string.database))
      .allowMainThreadQueries()
      .fallbackToDestructiveMigration()
      .addMigrations(Migration1_2, Migration2_3)
      .build()
  }

  @Provides
  @Singleton
  fun provideMovieDao(@NonNull database: AppDatabase): MovieDao {
    return database.movieDao()
  }

  @Provides
  @Singleton
  fun provideTvDao(@NonNull database: AppDatabase): TvDao {
    return database.tvDao()
  }

  @Provides
  @Singleton
  fun providePeopleDao(@NonNull database: AppDatabase): PeopleDao {
    return database.peopleDao()
  }
}
