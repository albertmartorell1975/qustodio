package com.albertmartorell.qustodio.utils

import android.content.Context
import com.albertmartorell.qustodio.Events
import org.json.JSONObject
import java.io.IOException
import java.util.*

fun getJsonFromAsset(context: Context, fileName: String): String {

    val jsonString: String

    try {

        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }

    } catch (ioException: IOException) {

        ioException.printStackTrace()
        return ""

    }

    return jsonString

}

fun sortedByTimeStamp(_content: String?): MutableList<Events> {

    val jsonObject = JSONObject(_content)
    val jsonArray = jsonObject.optJSONArray("events")

    val listOfEvents = mutableListOf<Events>()

    for (count in 0 until jsonArray.length()) {

        val event = jsonArray.getJSONObject(count)

        listOfEvents.add(
            Events(
                timestamp = event.optLong("timestamp"),
                user_id = event.optLong("user_id"),
                type = event.optString("type")
            )
        )

    }

    listOfEvents.sortBy { it.timestamp }

    return listOfEvents

}

fun setEvent(
    _listOfEvents: MutableList<Events>,
    _timeStamp: Long,
    _user_id: Long,
    _type: String
): List<Events> {

    val result = _listOfEvents.filter { it.user_id == _user_id }
    if (result.isNullOrEmpty())
        _listOfEvents.add(Events(timestamp = _timeStamp, user_id = _user_id, type = _type))

    return _listOfEvents

}

fun getCurrentTimeInMilliseconds(): Long {

    val calendar = Calendar.getInstance()
    calendar.time = Date()

    return calendar.timeInMillis / 1000

}
