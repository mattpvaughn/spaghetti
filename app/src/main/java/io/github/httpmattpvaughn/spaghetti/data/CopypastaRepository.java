package io.github.httpmattpvaughn.spaghetti.data;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by Matt Vaughn: http://mattpvaughn.github.io/
 */
public interface CopypastaRepository {

    interface LoadCopypastasCallback {
        void onPostsLoaded(List<Copypasta> posts);
    }

    interface LoadSingleCopypastaCallback {
        void onPostsLoaded(Copypasta copypasta);
    }

    void getAllPosts(@NonNull LoadCopypastasCallback callback, boolean loadMore, int nsfw);

    void getTemplatePosts(@NonNull LoadCopypastasCallback callback, int nsfw);

    void getAsciiPosts(@NonNull LoadCopypastasCallback callback, int nsfw);

    void getSearchResults(String query, @NonNull LoadCopypastasCallback callback, int nsfw);

    void loadSingleCopypasta(@NonNull LoadSingleCopypastaCallback callback, int id);
}
