package io.github.httpmattpvaughn.spaghetti.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Created by Matt Vaughn: http://mattpvaughn.github.io/
 */
@Database(entities = {Copypasta.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CopypastaDao copyPastaDao();
}

