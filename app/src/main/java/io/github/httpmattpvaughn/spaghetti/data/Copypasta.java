package io.github.httpmattpvaughn.spaghetti.data;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

/**
 * Created by Matt Vaughn: http://mattpvaughn.github.io/
 */
@Entity(tableName = "copypasta")
public class Copypasta {

    // The id of the copypasta- required
    @PrimaryKey
    public int id;

    // The title of the copypasta- optional
    @ColumnInfo(name = "title")
    @Nullable
    public String title;

    // The text making up the body of the copypasta- required field
    @ColumnInfo(name = "content")
    public String content;

    // A single emoji summarizing the content/mood of the post, required field
    @ColumnInfo(name = "emoji")
    public String emoji;

    // Whether a copypasta can be filled in like a mad lib, required field
    //      0: false
    //      1: true
    @ColumnInfo(name = "editable")
    public int editable;

    // Tags summarizing the content of a post- optional field
    @TypeConverters(TagConverter.class)
    @ColumnInfo(name = "tags")
    public List<String> tags;

    // Whether a post contains explicit content- required field
    //      0: false
    //      1: true
    @ColumnInfo(name = "nsfw")
    public Integer nsfw;

    // Categories: (required field)
    //      0: normal copypasta/emojipasta
    //      1: emoji art
    //      2: ascii art
    @ColumnInfo(name = "category")
    public Integer category;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Copypasta copypasta = (Copypasta) o;
        return id == copypasta.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public boolean isEditable() {
        return editable == 1;
    }

    @Override
    public String toString() {
        return "Copypasta{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", emoji='" + emoji + '\'' +
                ", editable=" + editable +
                ", tags=" + tags +
                ", nsfw=" + nsfw +
                ", category=" + category +
                '}';
    }
}
