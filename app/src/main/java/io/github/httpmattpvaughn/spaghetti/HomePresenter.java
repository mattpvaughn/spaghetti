package io.github.httpmattpvaughn.spaghetti;

import android.os.Bundle;
import android.os.Parcelable;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import io.github.httpmattpvaughn.spaghetti.data.Copypasta;
import io.github.httpmattpvaughn.spaghetti.data.CopypastaRepository;
import io.github.httpmattpvaughn.spaghetti.settings.AppSettings;

import static io.github.httpmattpvaughn.spaghetti.HomeContract.ActiveTab.ASCII;
import static io.github.httpmattpvaughn.spaghetti.HomeContract.ActiveTab.HOME;
import static io.github.httpmattpvaughn.spaghetti.HomeContract.ActiveTab.SHUFFLE;
import static io.github.httpmattpvaughn.spaghetti.HomeContract.ActiveTab.TEMPLATE;

/**
 * Created by Matt Vaughn: http://mattpvaughn.github.io/
 */
public class HomePresenter implements HomeContract.Presenter {

    // TODO- dagger
    private HomeContract.View view;
    private CopypastaRepository repository;
    private AppSettings appSettings;
    private long savedSeed = 0L;
    private boolean recreating = false;
    private int activeTabId = HOME;
    private Parcelable layoutManagerSavedState;
    private boolean nsfw;

    private final String RANDOM_SEED_KEY = "RANDOM_SEED";
    private String ACTIVE_TAB_ID_KEY = "ACTIVE_BOTTOM_TAB_ID";
    private String SAVED_LAYOUT_MANGER_KEY = "SAVED_LAYOUT_MANAGER";

    public HomePresenter(CopypastaRepository repository, AppSettings appSettings) {
        this.appSettings = appSettings;
        this.nsfw = appSettings.isNSFW();
        this.repository = repository;
    }

    @Override
    public void loadRecentStories() {
        repository.getAllPosts(this::setStories,
                false,
                nsfw ? 1 : 0);
    }

    @Override
    public void loadSearchResults(String query) {
        // Wrap query in sql wildcard characters, no need to escape b/c it's local db
        if (query.length() > 2) {
            query = "%" + query + "%";
            repository.getSearchResults(query, posts -> {
                if (posts.size() == 0) {
                    view.showEmptySearchResults();
                } else {
                    view.showSearchResults(posts);
                }
            }, nsfw ? 1 : 0);
        }
    }

    @Override
    public void loadTemplates() {
        repository.getTemplatePosts(this::setStories, nsfw ? 1 : 0);
    }

    @Override
    public void loadAsciiPosts() {
        repository.getAsciiPosts(this::setStories, nsfw ? 1 : 0);
    }

    @Override
    public void loadRandomPosts() {
        // If the app is being rebuilt, a random seed may be saved which we
        // would want to use instead recreating a new seed
        if (recreating && savedSeed != 0L) {
            recreating = false;
        } else {
            savedSeed = System.currentTimeMillis();
        }
        repository.getAllPosts(posts -> {
                    Collections.shuffle(posts, new Random(savedSeed));
                    setStories(posts);
                },
                false,
                nsfw ? 1 : 0);
    }

    private void setStories(List<Copypasta> posts) {
        if (layoutManagerSavedState != null) {
            view.restoreScrollLocation(layoutManagerSavedState);
            layoutManagerSavedState = null;
        }
        view.setStories(posts);
    }

    @Override
    public void loadStories(int activeTabId) {
        // Update the active posts
        switch (activeTabId) {
            case HOME:
                loadRecentStories();
                view.setToolbarTitle(R.string.navigation_home);
                break;
            case SHUFFLE:
                loadRandomPosts();
                view.setToolbarTitle(R.string.navigation_shuffle);
                break;
            case ASCII:
                loadAsciiPosts();
                view.setToolbarTitle(R.string.navigation_art);
                break;
            case TEMPLATE:
                loadTemplates();
                view.setToolbarTitle(R.string.navigation_template);
                break;
            default:
                throw new IllegalStateException("Error: Navigation tab id of " + activeTabId + " is not valid.");
        }
    }

    @Override
    public void setActiveTab(int activeTabId) {
        this.activeTabId = activeTabId;
        view.setBottomNavigationItemChecked(activeTabId);
    }

    @Override
    public void onBottomTabNavigationChange(int id) {
        setActiveTab(id);

        view.hideSearchResults();
        view.scrollToTop();
        loadStories(activeTabId);
    }

    @Override
    public void submitQuery(String query) {
        if (query.length() <= 2) {
            view.showUserMessage("Search is too short!");
        }
    }

    @Override
    public void handleRestoredBundle(Bundle savedInstanceState) {
        if (recreating && savedInstanceState != null) {
            savedSeed = savedInstanceState.getLong(RANDOM_SEED_KEY); // default value = 0L
            setActiveTab(savedInstanceState.getInt(ACTIVE_TAB_ID_KEY));
            layoutManagerSavedState = savedInstanceState.getParcelable(SAVED_LAYOUT_MANGER_KEY);
        }

        // Recreating the activity entirely
        if (recreating) {
            loadStories(activeTabId);
        }
    }

    @Override
    public void writeToBundle(Bundle outState) {
        outState.putLong(RANDOM_SEED_KEY, savedSeed);
        outState.putInt(ACTIVE_TAB_ID_KEY, activeTabId);
        outState.putParcelable(SAVED_LAYOUT_MANGER_KEY, view.getCopypastaLayoutManager());
    }

    @Override
    public void attachView(HomeContract.View view, boolean recreating) {
        this.view = view;
        boolean isNSFWTemp = appSettings.isNSFW();
        // If the activity is restarting and appSettings have changed, then we
        // have to reload the view (b/c user toggled a setting and view needs
        // to reflect that)
        if(!recreating && this.nsfw != isNSFWTemp) {
            this.nsfw = isNSFWTemp;
            loadStories(activeTabId);
        }
        this.recreating = recreating;
    }

    @Override
    public void detachView() {
        this.view = null;
    }
}
