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

package com.skydoves.entity.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/** Migration2_3 an object class for migrating version 2 to 3. */
@Suppress("ClassName")
object Migration2_3 : Migration(2, 3) {

  override fun migrate(database: SupportSQLiteDatabase) {
    database.execSQL("DROP TABLE Gallery")
    database.execSQL("ALTER TABLE Movie ADD COLUMN favourite INTEGER NOT NULL DEFAULT '0'")
    database.execSQL("ALTER TABLE Tv ADD COLUMN favourite INTEGER NOT NULL DEFAULT '0'")
  }
}
