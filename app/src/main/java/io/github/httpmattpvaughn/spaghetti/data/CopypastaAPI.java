package io.github.httpmattpvaughn.spaghetti.data;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by Matt Vaughn: http://mattpvaughn.github.io/
 */
public interface CopypastaAPI {
    interface CopypastaServiceCallback {
        void onLoaded(List<Copypasta> posts);
    }

    interface SingleCopypastaServiceCallback {
        void onLoaded(Copypasta copypasta);
    }

    void getPosts(@NonNull CopypastaServiceCallback callback, int nsfw, int offset);

    void getTemplatePosts(@NonNull CopypastaServiceCallback callback, int nsfw);

    void getAsciiPosts(@NonNull CopypastaServiceCallback callback, int nsfw);

    void getQueryResults(String query, @NonNull CopypastaServiceCallback callback, int nsfw);

    void getCopypastaById(int id, @NonNull SingleCopypastaServiceCallback callback);

}
