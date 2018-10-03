package io.github.httpmattpvaughn.spaghetti.data;

import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.annotation.NonNull;
import androidx.room.Room;
import io.github.httpmattpvaughn.spaghetti.Constants;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Matt Vaughn: http://mattpvaughn.github.io/
 */
public class CopypastaDatabaseAPI implements CopypastaAPI {

    final AppDatabase db;

    public CopypastaDatabaseAPI(Context context) {
        // Copy DB from assets to app dir
        copyAssets(context, "copypasta_posts.db");
        db = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "copypasta_posts.db")
                .addMigrations()
                .build();

    }

    @Override
    public void getPosts(CopypastaServiceCallback callback, int nsfw, int offset) {
        db.copyPastaDao().getAll(nsfw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onLoaded);
    }

    @Override
    public void getTemplatePosts(@NonNull CopypastaServiceCallback callback, int nsfw) {
        // TODO- keep references to and dispose of these subscribers at onDestroy (or onStop?)
        db.copyPastaDao().getTemplatePosts(nsfw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onLoaded);
    }

    @Override
    public void getAsciiPosts(@NonNull CopypastaServiceCallback callback, int nsfw) {
        db.copyPastaDao().getAsciiPosts(nsfw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onLoaded);
    }

    @Override
    public void getQueryResults(String query, @NonNull CopypastaServiceCallback callback, int nsfw) {
        db.copyPastaDao().search(query, nsfw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onLoaded);
    }

    @Override
    public void getCopypastaById(int id, @NonNull SingleCopypastaServiceCallback callback) {
        db.copyPastaDao().getCopypastaById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onLoaded);
    }

    private void copyAssets(Context context, String fileName) {
        Log.i("Database", "New database is being copied to device!");
        byte[] buffer = new byte[1024];
        OutputStream myOutput = null;
        int length;
        // Open your local db as the input stream
        InputStream myInput = null;
        try {
            myInput = context.getAssets().open("databases/" + fileName);
            // transfer bytes from the inputfile to the
            // outputfile
            myOutput = new FileOutputStream(context.getDatabasePath(fileName).getAbsolutePath());
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.close();
            myOutput.flush();
            myInput.close();
            Log.i(Constants.loggingTag, "New database has been copied to device!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
