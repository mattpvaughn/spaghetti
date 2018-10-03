package io.github.httpmattpvaughn.spaghetti.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.room.TypeConverter;

/**
 * Created by Matt Vaughn: http://mattpvaughn.github.io/
 */
class TagConverter {

    @TypeConverter
    public static List<String> toList(String tags) {
        if(tags == null){
            return new ArrayList<>();
        }
        String[] strings = tags.split("n");
        return Arrays.asList(strings);
    }

    @TypeConverter
    public static String toString(List<String> stringList) {
        return stringList.toString();
    }
}
