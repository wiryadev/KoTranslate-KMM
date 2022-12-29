package com.wiryadev.kotranslate.translate.data.local

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import com.wiryadev.kotranslate.database.TranslateDatabase

actual class DatabaseDriverFactory(
    private val context: Context,
) {
    actual fun create(): SqlDriver = AndroidSqliteDriver(
        schema = TranslateDatabase.Schema,
        context = context,
        name = "translate.db"
    )
}