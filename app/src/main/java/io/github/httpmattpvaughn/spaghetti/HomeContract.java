package io.github.httpmattpvaughn.spaghetti;

import android.os.Bundle;
import android.os.Parcelable;

import java.util.List;

import io.github.httpmattpvaughn.spaghetti.data.Copypasta;

/**
 * Created by Matt Vaughn: http://mattpvaughn.github.io/
 */
public interface HomeContract {
    interface View {
        // Displays a list of stories to the view
        void setStories(List<Copypasta> copypastas);

        // Displays a message "no search results found"
        void showEmptySearchResults();

        // Displays a list of search results
        void showSearchResults(List<Copypasta> copypastas);

        // Hides the search results
        void hideSearchResults();

        // Displays a message to show the user
        void showUserMessage(String string);

        // Toggles on/off an indicator to show stories being loaded in
        void setLoading(boolean loading);

        // Show a single copypasta
        void showCopypastaInNewScreen(int copypastaId);

        // Copies the current copypasta to the clipboard
        void copyToClipboard(String copypasta);

        void setBottomNavigationItemChecked(int activeBottomTabId);

        Parcelable getCopypastaLayoutManager();

        void restoreScrollLocation(Parcelable layoutManager);

        void scrollToTop();

        void setToolbarTitle(int title);
    }

    interface Presenter {
        // Attaches a view to the presenter, passes along state about whether the
        // view is being created or just restarted.
        // Is also when the view checks to see if AppSettings have changed, updates
        // the view to reflect if they have.
        void attachView(HomeContract.View view, boolean creating);

        // Detaches the presenter from the view, removing any references to the
        // view
        void detachView();

        // Gets a list of the most recent copypastas from the repository, calls
        // the view to display them
        void loadRecentStories();

        // Gets a list of copypastas as a result to a query, calls the view to
        // display them
        void loadSearchResults(String query);

        // Even though search results are loaded automatically, submit query
        // checks if the user is trying to submit a query (pressing return)
        // so that it can warn if a query is invalid
        // I.E. if the user is trying to search for "be" (which is too short
        // and will not return any results via loadSearchResults), this will
        // warn against doing that if they press enter
        void submitQuery(String query);

        // Gets a list of all emoji art posts
        void loadTemplates();

        // Gets a list of all ascii art posts
        void loadAsciiPosts();

        // Loads a list of all posts in a random order
        void loadRandomPosts();

        // Loads a list of
        void loadStories(int activeTabId);

        // Handles a Bundle containing saved state data to be restored
        void handleRestoredBundle(Bundle savedInstanceState);

        // Writes the current state to a bundle
        void writeToBundle(Bundle outState);

        // Handles changes to primary navigation. Updates the state, hides
        // search results, scrolls main scrolling view to the top, and
        // loads the relevant posts
        void onBottomTabNavigationChange(int id);

        // Updates the state in the presenter and then sets the navigation
        // to show as active in the view
        void setActiveTab(int tabId);
    }

    interface CopypastaClickListener {
        void onRowClick(Copypasta copypasta);
        boolean onRowLongClick(Copypasta copypasta);
    }

    final class ActiveTab {
        public final static int HOME = R.id.navigation_home;
        public final static int TEMPLATE = R.id.navigation_template;
        public final static int ASCII = R.id.navigation_art;
        public final static int SHUFFLE = R.id.navigation_shuffle;
    }
}
