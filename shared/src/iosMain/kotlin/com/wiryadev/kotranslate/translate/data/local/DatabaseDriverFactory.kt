package com.wiryadev.kotranslate.translate.data.local

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.wiryadev.kotranslate.database.TranslateDatabase

actual class DatabaseDriverFactory {
    actual fun create(): SqlDriver = NativeSqliteDriver(
        schema = TranslateDatabase.Schema,
        name = "translate.db"
    )
}