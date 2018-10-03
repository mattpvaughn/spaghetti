package io.github.httpmattpvaughn.spaghetti.copypasta.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import io.github.httpmattpvaughn.spaghetti.copypasta.HomeTest;
import io.github.httpmattpvaughn.spaghetti.data.Copypasta;
import io.github.httpmattpvaughn.spaghetti.data.CopypastaRepository;

/**
 * Created by Matt Vaughn: http://mattpvaughn.github.io/
 */
public class MockCopypastaManager implements CopypastaRepository {

    private static final String EXAMPLE_COPYPASTA_CONTENT = "EXAMPLE_COPYPASTA_CONTENT";
    private static final String EXAMPLE_COPYPASTA_TITLE = "EXAMPLE_COPYPASTA_TITLE";
    private static final int EXAMPLE_COPYPASTA_ID = 0;
    private static final int EXAMPLE_COPYPASTA_EDITABLE = 0;
    private static final int FAKE_COPYPASTA_LIST_SIZE = 100;
    private static final String EXAMPLE_COPYPASTA_EMOJI = "\uD83C\uDD71";
    private static final String[] EXAMPLE_COPYPASTA_TAG_ARRAY = new String[] { "navy seal", "frogman", "frogmen", "insult"};

    @Override
    public void getAllPosts(@NonNull LoadCopypastasCallback callback, boolean loadMore, int nsfw) {
        callback.onPostsLoaded(makeFakeCopypastas());
    }

    @Override
    public void getTemplatePosts(@NonNull LoadCopypastasCallback callback, int nsfw) {
        callback.onPostsLoaded(makeFakeCopypastas());
    }

    @Override
    public void getAsciiPosts(@NonNull LoadCopypastasCallback callback, int nsfw) {
        callback.onPostsLoaded(makeFakeCopypastas());
    }

    @Override
    public void getSearchResults(String query, @NonNull LoadCopypastasCallback callback, int nsfw) {
        if(query.equals(HomeTest.EXAMPLE_SEARCH_QUERY_NO_RESULTS_SQLIZED)) {
            // Specify this so we can how the presenter handles an empty result
            callback.onPostsLoaded(new ArrayList<>());
        } else {
            callback.onPostsLoaded(makeFakeCopypastas());
        }
    }

    @Override
    public void loadSingleCopypasta(@NonNull LoadSingleCopypastaCallback callback, int id) {
        callback.onPostsLoaded(makeFakeCopypasta());
    }

    public Copypasta makeFakeCopypasta() {
        // Possibly good candidate for a Builder...
        Copypasta copypasta = new Copypasta();
        copypasta.id = EXAMPLE_COPYPASTA_ID;
        copypasta.content = EXAMPLE_COPYPASTA_CONTENT;
        copypasta.title = EXAMPLE_COPYPASTA_TITLE;
        copypasta.editable = EXAMPLE_COPYPASTA_EDITABLE;
        copypasta.nsfw = 0;
        copypasta.category = 0;
        copypasta.emoji = EXAMPLE_COPYPASTA_EMOJI;
        copypasta.tags = Arrays.asList(EXAMPLE_COPYPASTA_TAG_ARRAY);
        return copypasta;
    }

    public List<Copypasta> makeFakeCopypastas() {
        List<Copypasta> list = new ArrayList<>();
        for(int i = 0; i < FAKE_COPYPASTA_LIST_SIZE; i++) {
            list.add(makeFakeCopypasta());
        }
        return list;
    }
}
