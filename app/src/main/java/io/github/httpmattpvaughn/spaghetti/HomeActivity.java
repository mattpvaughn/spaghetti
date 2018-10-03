package io.github.httpmattpvaughn.spaghetti;

import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.httpmattpvaughn.spaghetti.copypasta.CopypastaActivity;
import io.github.httpmattpvaughn.spaghetti.data.Copypasta;
import io.github.httpmattpvaughn.spaghetti.data.CopypastaManager;
import io.github.httpmattpvaughn.spaghetti.request.RequestActivity;
import io.github.httpmattpvaughn.spaghetti.settings.AppSettingsImpl;
import io.github.httpmattpvaughn.spaghetti.settings.SettingsActivity;

public class HomeActivity extends AppCompatActivity implements HomeContract.View, HomeContract.CopypastaClickListener {

    private RecyclerView copypastaRecyclerView;
    private CopypastaRecyclerViewAdapter copypastaRecyclerViewAdapter;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigation;
    private RecyclerView searchResultsRecyclerView;
    private CopypastaRecyclerViewAdapter searchResultsRecyclerViewAdapter;
    private View noSearchResultsView;

    private HomeContract.Presenter presenter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        // Retrieve the SearchView and plug it into SearchManager
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                hideSearchResults();
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.submitQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.loadSearchResults(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.action_copypasta_form:
                Intent requestIntent = new Intent(this, RequestActivity.class);
                startActivity(requestIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        copypastaRecyclerView = findViewById(R.id.main_copypasta_recyclerview);
        searchResultsRecyclerView = findViewById(R.id.search_results_recyclerview);
        noSearchResultsView = findViewById(R.id.no_search_results);

        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        copypastaRecyclerViewAdapter = new CopypastaRecyclerViewAdapter(this);
        copypastaRecyclerViewAdapter.setHasStableIds(true);
        copypastaRecyclerView.setAdapter(copypastaRecyclerViewAdapter);
        copypastaRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchResultsRecyclerViewAdapter = new CopypastaRecyclerViewAdapter(this);
        searchResultsRecyclerViewAdapter.setHasStableIds(true);
        searchResultsRecyclerView.setAdapter(searchResultsRecyclerViewAdapter);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        presenter = new HomePresenter(new CopypastaManager(this), new AppSettingsImpl(this));
        this.presenter.attachView(this, true);
        presenter.handleRestoredBundle(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        presenter.attachView(this, false);
        super.onRestart();
    }

    @Override
    public void setStories(List<Copypasta> copypastas) {
        copypastaRecyclerViewAdapter.setData(copypastas);
        copypastaRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void showSearchResults(List<Copypasta> copypastas) {
        searchResultsRecyclerViewAdapter.setData(copypastas);
        searchResultsRecyclerViewAdapter.notifyDataSetChanged();
        noSearchResultsView.setVisibility(View.GONE);
        searchResultsRecyclerView.setVisibility(View.VISIBLE);
        copypastaRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showEmptySearchResults() {
        noSearchResultsView.setVisibility(View.VISIBLE);
        searchResultsRecyclerView.setVisibility(View.GONE);
        copypastaRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideSearchResults() {
        noSearchResultsView.setVisibility(View.GONE);
        searchResultsRecyclerView.setVisibility(View.GONE);
        copypastaRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showUserMessage(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setLoading(boolean loading) {

    }

    @Override
    public void showCopypastaInNewScreen(int copypastaId) {
        Intent intent = new Intent(this, CopypastaActivity.class);
        intent.putExtra(getString(R.string.copypasta_id), copypastaId);
        startActivity(intent);
    }

    public void copyToClipboard(String text) {
        // Gets a handle to the clipboard service.
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("\uD83E\uDD14", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setBottomNavigationItemChecked(int activeBottomTabId) {
        bottomNavigation.getMenu().findItem(activeBottomTabId).setChecked(true);
    }

    @Override
    public Parcelable getCopypastaLayoutManager() {
        return copypastaRecyclerView.getLayoutManager().onSaveInstanceState();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        presenter.writeToBundle(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void restoreScrollLocation(Parcelable layoutManagerSavedState) {
        copypastaRecyclerView.getLayoutManager().onRestoreInstanceState(layoutManagerSavedState);
    }

    @Override
    public void scrollToTop() {
        copypastaRecyclerView.scrollToPosition(0);
    }

    @Override
    public void setToolbarTitle(@StringRes int title) {
        toolbar.setTitle(title);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        presenter.onBottomTabNavigationChange(item.getItemId());
        return false;
    };

    @Override
    public void onRowClick(Copypasta copypasta) {
        showCopypastaInNewScreen(copypasta.id);
    }

    @Override
    public boolean onRowLongClick(Copypasta copypasta) {
        copyToClipboard(copypasta.content);
        return true;
    }
}
