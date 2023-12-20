package com.flynaut.healthtag.util

import android.text.format.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    val utcFormat = SimpleDateFormat(DateFormat.UTC_FORMAT, Locale.getDefault())

    fun dateFormatFromMillis(format: String, timeInMillis: Long?): String {
        val fmt = SimpleDateFormat(format, Locale.ENGLISH)
        return if (timeInMillis == null || timeInMillis == 0L)
            ""
        else
            fmt.format(timeInMillis)
    }


    fun dateFormatChange(formatFrom: String, formatTo: String, value: String): String {
        val originalFormat = SimpleDateFormat(formatFrom, Locale.ENGLISH)
        val targetFormat = SimpleDateFormat(formatTo, Locale.ENGLISH)
        val date = originalFormat.parse(value)
        return targetFormat.format(date)
    }

    fun localToUTC(dateFormat: String, datesToConvert: String): String {

        var dateToReturn = datesToConvert

        val sdf = SimpleDateFormat(dateFormat)
        sdf.timeZone = TimeZone.getDefault()
        var gmt: Date? = null

        val sdfOutPutToSend = SimpleDateFormat(DateFormat.DATE_FORMAT)
        sdfOutPutToSend.timeZone = TimeZone.getTimeZone("UTC")

        try {

            gmt = sdf.parse(datesToConvert)
            dateToReturn = sdfOutPutToSend.format(gmt)

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return dateToReturn
    }


    fun getTimeAgo(createdAt: String?): String {
        val agoString: String

        if (createdAt == null) {
            return ""
        }

        utcFormat.timeZone = TimeZone.getTimeZone("Etc/UTC")
        val time = utcFormat.parse(createdAt).time
        val now = System.currentTimeMillis()

        val ago = DateUtils.getRelativeTimeSpanString(
            time, now, DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE
        )

        return ago.toString()
    }

    fun getTimeAgoForMillis(millis: Long): String {

        val now = System.currentTimeMillis()

        return DateUtils.getRelativeTimeSpanString(
            millis, now, DateUtils.SECOND_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_RELATIVE
        ).toString()
    }

    fun dateFormatFromMillisBackend(format: String, timeInMillis: Long?): String {
        val fmt = SimpleDateFormat(format, Locale.ENGLISH)
        return if (timeInMillis == null || timeInMillis == 0L)
            ""
        else
            fmt.format(timeInMillis)
    }

    fun getLocalTimeAgo(timeString: Long?, removeAgo: String): String {
        var agoString = ""

        timeString?.let {
            val now = System.currentTimeMillis()

            val ago = DateUtils.getRelativeTimeSpanString(
                timeString,
                now,
                DateUtils.SECOND_IN_MILLIS,
                DateUtils.FORMAT_SHOW_TIME
            )

            agoString = ago.toString()
        }

        return agoString
    }

    fun dateTimeFormatFromUTC(format: String, createdDate: String?): String {
        return if (createdDate == null || createdDate.isEmpty())
            ""
        else {
            utcFormat.timeZone = TimeZone.getTimeZone("Etc/UTC")

            val fmt = SimpleDateFormat(format, Locale.getDefault())
            fmt.format(utcFormat.parse(createdDate))
        }
    }
}

/*On Date selected listener*/
interface OnDateSelected {
    fun onDateSelected(date: String)
}

fun isYesterday(calendar: Calendar): Boolean {
    val tempCal = Calendar.getInstance()
    tempCal.add(Calendar.DAY_OF_MONTH, -1)
    return calendar.get(Calendar.DAY_OF_MONTH) == tempCal.get(Calendar.DAY_OF_MONTH)
}

object DateFormat {
    const val DATE_FORMAT = "yyyy-MM-dd"
    const val DATE_TIME_FORMAT = "dd MMM yyyy · hh:mm a"
    const val TIME_FORMAT = "hh:mm a"
    const val TIME_FORMAT_24 = "HH:mm"
    const val MON_YEAR_FORMAT = "dd MMM, yyyy"
    const val MON_DAY_YEAR = "MMM dd, yyyy"
    const val MON_DATE = "MMM dd"
    const val MON_DATE_YEAR = "MMM dd, yy"
    const val DATE_FORMAT_SLASH = "MM/dd/yy"
    const val DATE_FORMAT_SLASH_YEAR = "dd/MM/yyyy"
    const val DATE_FORMAT_RENEW = "yyyy-MM-dd hh:mm"
    const val UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val UTC_FORMAT_NORMAL = "yyyy-MM-dd hh:mm:ss"
    const val MONTH_FORMAT = "MMM"
    const val HH_24 = "HH"
    const val MM = "mm"
}
