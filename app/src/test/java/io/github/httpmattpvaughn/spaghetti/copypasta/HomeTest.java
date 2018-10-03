package io.github.httpmattpvaughn.spaghetti.copypasta;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import androidx.annotation.IdRes;
import io.github.httpmattpvaughn.spaghetti.HomeActivity;
import io.github.httpmattpvaughn.spaghetti.HomeContract;
import io.github.httpmattpvaughn.spaghetti.HomePresenter;
import io.github.httpmattpvaughn.spaghetti.R;
import io.github.httpmattpvaughn.spaghetti.copypasta.data.MockCopypastaManager;
import io.github.httpmattpvaughn.spaghetti.copypasta.settings.MockAppSettingsImpl;
import io.github.httpmattpvaughn.spaghetti.data.CopypastaRepository;
import io.github.httpmattpvaughn.spaghetti.settings.AppSettings;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Test HomePresenter against expected results from HomeContract.Presenter
 * <p>
 * Created by Matt Vaughn: http://mattpvaughn.github.io/
 */

@RunWith(MockitoJUnitRunner.class)
public class HomeTest {

    // A valid search query that will return results from MockCopypastaManager
    private static final String EXAMPLE_SEARCH_QUERY_BASIC = "not empty";

    // A valid search query that will not return results from MockCopypastaManager
    public static final String EXAMPLE_SEARCH_QUERY_NO_RESULTS = "empty";

    // A search query which has been wrapped in '%' wildcard characters that
    // will not return any results from MockCopypastaManager
    public static final String EXAMPLE_SEARCH_QUERY_NO_RESULTS_SQLIZED = "%empty%";

    // An invalid search query (too short)
    private static final String EXAMPLE_SEARCH_QUERY_TOO_SHORT = "A";

    // The ID of a tab in a bottom navigation bar
    private static final @IdRes int EXAMPLE_BOTTOM_TAB_ID = R.id.navigation_home;

    HomeContract.Presenter presenter;
    private AppSettings appSettings;
    private CopypastaRepository repository;

    HomeContract.View view;

    // Mock resources that Mockito is not needed for
    @Before
    public void setup() {
        // Create a mocked HomeContract.View
        this.view = Mockito.mock(HomeActivity.class);

        // Mock our repository
        this.repository = new MockCopypastaManager();

        // Mock our app settings
        this.appSettings = new MockAppSettingsImpl(true);

        // Initialize our presenter (and initialize with Mockito.spy so we can
        // call verify() on it)
        this.presenter = Mockito.spy(new HomePresenter(repository, appSettings));
        this.presenter.attachView(view, true);
    }

    // Test that calling loadRecentStories will result in the view loading a list
    // of copypastas
    @Test
    public void loadRecentStoriesBasic() {
        presenter.loadRecentStories();
        verify(view).setStories(any());
    }

    // Test that calling loadRandomStories will result in the view loading a list
    // of copypastas
    @Test
    public void loadRandomStoriesBasic() {
        presenter.loadRandomPosts();
        verify(view).setStories(any());
    }

    // Test that calling loadTemplatePosts will result in the view loading a list
    // of copypastas
    @Test
    public void loadTemplates() {
        presenter.loadTemplates();
        verify(view).setStories(any());
    }

    // Test that calling loadAsciiPosts will result in the view loading a list
    // of copypastas
    @Test
    public void loadAsciiPosts() {
        presenter.loadAsciiPosts();
        verify(view).setStories(any());
    }

    // Test that a valid search result will cause the view to show a search
    // results screen
    @Test
    public void loadSearchResultsBasic() {
        presenter.loadSearchResults(EXAMPLE_SEARCH_QUERY_BASIC);
        verify(view).showSearchResults(any());
    }

    // Test that passing the presenter a search result which does not return
    // any results will cause the view to show a showEmptySearchResults screen
    @Test
    public void loadSearchResultEmpty() {
        presenter.loadSearchResults(EXAMPLE_SEARCH_QUERY_NO_RESULTS);
        verify(view).showEmptySearchResults();
    }

    // Test that the presenter will request the view to show a message if the
    // user submits a query that is too short.
    @Test
    public void submitQueryTooShort() {
        presenter.submitQuery(EXAMPLE_SEARCH_QUERY_TOO_SHORT);
        verify(view).showUserMessage(anyString());
    }

    // Test that the presenter will NOT request the view to show a message if
    // the user submits a valid query
    @Test
    public void submitQueryCorrectLength() {
        presenter.submitQuery(EXAMPLE_SEARCH_QUERY_BASIC);
        verify(view, never()).showUserMessage(any());
    }

    // Test that the presenter will properly handle a simple bottomnavigation change
    @Test
    public void testBottomNavigationTabChange() {
        presenter.onBottomTabNavigationChange(EXAMPLE_BOTTOM_TAB_ID);
        verify(view).hideSearchResults();
        verify(view).scrollToTop();
        verify(presenter).setActiveTab(anyInt());
        verify(presenter).loadStories(anyInt());
    }

    // Test that attachView will update the view with the proper copypastas if
    // app settings have changed since the presenter has been detached from
    // the view
    @Test
    public void testAttachViewNeedsReload() {
        // Initialize an AppSettings object with NSFW = false
        AppSettings fakeSettings = new MockAppSettingsImpl(false);

        // Make a presenter with that appsettings, attach to a mocked view then detach
        HomePresenter tempPresenter = Mockito.spy(new HomePresenter(new MockCopypastaManager(), fakeSettings));
        tempPresenter.attachView(view, false);
        tempPresenter.detachView();

        // Toggling appSettings should trigger a view reload (tempPresenter.loadStories())
        // if attachView(view, false) is called
        fakeSettings.setNSFW(true);
        tempPresenter.attachView(view, false);

        verify(tempPresenter).loadStories(anyInt());
    }

    // Test that attachView will not try to reload the view when the view is
    // not being recreated and no settings have changed. In this case the view
    // will already be populated with the proper data
    @Test
    public void testAttachViewNoReloadOnWhenNoSettingsChange() {
        // Initialize an AppSettings object with NSFW = false
        AppSettings fakeSettings = new MockAppSettingsImpl(false);

        // Make a presenter with that appsettings, attach to a mocked view then detach
        HomePresenter tempPresenter = Mockito.spy(new HomePresenter(new MockCopypastaManager(), fakeSettings));
        tempPresenter.attachView(view, false);
        tempPresenter.detachView();

        // A view reload should not be trigged because appsettings have not changed
        fakeSettings.setNSFW(false);
        tempPresenter.attachView(view, false);

        verify(tempPresenter, never()).loadStories(anyInt());
    }

    // Test that attachView() will not try to reload posts in a view when it
    // is recreating the view because handleRestoredBundle() should handle this
    // case so we can properly use the bundled state
    @Test
    public void testAttachNoReloadOnRecreating() {
        // Initialize an AppSettings object with NSFW = false
        AppSettings fakeSettings = new MockAppSettingsImpl(false);

        // Make a presenter with that appsettings, attach to a mocked view then detach
        HomePresenter tempPresenter = Mockito.spy(new HomePresenter(new MockCopypastaManager(), fakeSettings));
        tempPresenter.attachView(view, false);
        tempPresenter.detachView();

        // A view reload should not be trigged because appsettings have not changed
        fakeSettings.setNSFW(true);
        tempPresenter.attachView(view, true);

        verify(tempPresenter, never()).loadStories(anyInt());
    }
}