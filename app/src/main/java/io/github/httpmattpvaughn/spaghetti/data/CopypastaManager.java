package io.github.httpmattpvaughn.spaghetti.data;

import android.content.Context;

import androidx.annotation.NonNull;

/**
 * Created by Matt Vaughn: http://mattpvaughn.github.io/
 */
public class CopypastaManager implements CopypastaRepository {

    private CopypastaAPI copyPastaAPI;

    public CopypastaManager(Context context) {
        copyPastaAPI = new CopypastaDatabaseAPI(context);
    }

    @Override
    public void getAllPosts(@NonNull LoadCopypastasCallback callback, boolean loadMore, int nsfw) {
        copyPastaAPI.getPosts(callback::onPostsLoaded, nsfw, 0);
    }

    @Override
    public void getTemplatePosts(@NonNull LoadCopypastasCallback callback, int nsfw) {
        copyPastaAPI.getTemplatePosts(callback::onPostsLoaded, nsfw);
    }

    @Override
    public void getAsciiPosts(@NonNull LoadCopypastasCallback callback, int nsfw) {
        copyPastaAPI.getAsciiPosts(callback::onPostsLoaded, nsfw);
    }

    @Override
    public void getSearchResults(String query, @NonNull LoadCopypastasCallback callback, int nsfw) {
        copyPastaAPI.getQueryResults(query, callback::onPostsLoaded, nsfw);
    }

    @Override
    public void loadSingleCopypasta(@NonNull LoadSingleCopypastaCallback callback, int id) {
        copyPastaAPI.getCopypastaById(id, callback::onPostsLoaded);
    }


}
