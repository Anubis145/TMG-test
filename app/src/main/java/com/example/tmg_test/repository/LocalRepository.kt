package com.example.tmg_test.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.tmg_test.model.GameModel
import com.example.tmg_test.utils.SORT_TYPE_WINS
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.reflect.KProperty

open class LocalRepository @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    private val gson = Gson()

    inner class StringPreferenceDelegate(private val defaultValue: String = "") {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): String =
            sharedPreferences.getString(property.name, defaultValue)!!

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) =
            sharedPreferences
                .edit()
                .putString(property.name, value)
                .apply()
    }

    inner class BooleanPreferenceDelegate(private val defaultValue: Boolean = false) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): Boolean =
            sharedPreferences.getBoolean(property.name, defaultValue)

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) =
            sharedPreferences
                .edit()
                .putBoolean(property.name, value)
                .apply()
    }

    inner class JsonPreferenceDelegate<T>(private val clazz: Class<T>) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            val json = sharedPreferences.getString(property.name, "null")!!
            if (json.isEmpty())
                return null
            return gson.fromJson(json, clazz)
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) =
            sharedPreferences
                .edit()
                .putString(property.name, gson.toJson(value))
                .apply()
    }

    inner class IntPreferenceDelegate(private val defaultValue: Int = 0) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): Int =
            sharedPreferences.getInt(property.name, defaultValue)

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) =
            sharedPreferences
                .edit()
                .putInt(property.name, value)
                .apply()
    }

    inner class LongPreferenceDelegate(private val defaultValue: Long = 0) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): Long =
            sharedPreferences.getLong(property.name, defaultValue)

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) =
            sharedPreferences
                .edit()
                .putLong(property.name, value)
                .apply()
    }

    inner class JsonListPreferenceDelegate<T>(clazz: Class<T>) {
        private val typeToken = TypeToken.getParameterized(ArrayList::class.java, clazz).type
        operator fun getValue(thisRef: Any?, property: KProperty<*>): List<T> {
            val json = sharedPreferences.getString(property.name, "")!!
            if (json.isEmpty()) return arrayListOf()
            return gson.fromJson(json, typeToken)
        }
        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: List<T>?) =
            sharedPreferences
                .edit()
                .putString(property.name, gson.toJson(value))
                .apply()
    }
    fun clear() {
        sharedPreferences
            .edit()
            .clear()
            .apply()
    }
    companion object {
        const val PREFS = "PREFS"
    }

    var leaderSortType by StringPreferenceDelegate(SORT_TYPE_WINS)
}
