package com.example.myittaroostockinventorymanger.local;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateLongTypeConverter {

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public Date formTimestamp(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }
}
