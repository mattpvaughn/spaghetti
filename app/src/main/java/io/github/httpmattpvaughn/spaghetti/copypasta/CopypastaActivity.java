package io.github.httpmattpvaughn.spaghetti.copypasta;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.github.httpmattpvaughn.spaghetti.R;
import io.github.httpmattpvaughn.spaghetti.data.Copypasta;
import io.github.httpmattpvaughn.spaghetti.data.CopypastaManager;
import io.github.httpmattpvaughn.spaghetti.request.RequestActivity;
import io.github.httpmattpvaughn.spaghetti.settings.SettingsActivity;

/**
 * Created by Matt Vaughn: http://mattpvaughn.github.io/
 */
public class CopypastaActivity extends AppCompatActivity implements CopypastaContract.View {

    private Toolbar toolbar;
    private CopypastaContract.Presenter presenter;
    private FloatingActionButton fab;
    private TextView copyPastaContent;
    private EditText copyPastaContentEditable;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_copypasta, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copypasta);

        toolbar = findViewById(R.id.toolbar);
        copyPastaContent = findViewById(R.id.focused_copypasta_content);
        copyPastaContentEditable = findViewById(R.id.focused_copypasta_content_editable);
        fab = findViewById(R.id.fab);

        presenter = new CopypastaPresenter(new CopypastaManager(this));
        presenter.attachView(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        int id = intent.getExtras().getInt(getString(R.string.copypasta_id), -1);
        presenter.loadCopypasta(id);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.action_copypasta_form:
                startActivity(new Intent(this, RequestActivity.class));
                break;
            case R.id.action_edit_copypasta:
                presenter.toggleEditingCopypasta();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // TODO- maybe extract to a Utils class
    public void copyToClipboard(String text) {
        // Gets a handle to the clipboard service.
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("\uD83E\uDD14", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setCopypastaContentNotTemplateable(String content) {
        copyPastaContent.setText(content);
    }

    @Override
    public void setCopypastaContentTemplateable(String content) {
        copyPastaContent.setText(content);
        MadLibTemplater.from(copyPastaContent).apply();
    }

    @Override
    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void setFabListener(CopypastaContract.FabListener listener) {
        fab.setOnClickListener(v -> listener.onFabClick());
    }

    @Override
    public void setEditing() {
        copyPastaContentEditable.setText(copyPastaContent.getText().toString());
        copyPastaContentEditable.setVisibility(View.VISIBLE);
        copyPastaContent.setVisibility(View.GONE);
    }

    @Override
    public void setNotEditing() {
        copyPastaContent.setText(copyPastaContentEditable.getText().toString());
        copyPastaContent.setVisibility(View.VISIBLE);
        copyPastaContentEditable.setVisibility(View.GONE);
    }

    @Override
    public void showUserMessage(String userMessage) {
        Toast.makeText(this, userMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void copyCopypastaTextEditable() {
        copyToClipboard(copyPastaContentEditable.getText().toString());
    }

    @Override
    public void copyCopypastaTextNotEditable() {
        copyToClipboard(copyPastaContent.getText().toString());
    }
}
