package io.github.httpmattpvaughn.spaghetti.request;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.github.httpmattpvaughn.spaghetti.R;

/**
 * Created by Matt Vaughn: http://mattpvaughn.github.io/
 */
public class RequestActivity extends AppCompatActivity implements RequestContract.View {
    private TextInputEditText content;
    private TextInputEditText emoji;
    private TextInputEditText title;
    private CheckBox nsfw;
    private TextInputEditText tags;
    private RadioGroup category;
    private FloatingActionButton fab;
    private RequestContract.Presenter presenter;

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        content = findViewById(R.id.submit_content);
        emoji = findViewById(R.id.submit_emoji);
        title = findViewById(R.id.submit_title);
        nsfw = findViewById(R.id.submit_nsfw);
        tags = findViewById(R.id.submit_tags);
        category = findViewById(R.id.submit_category);
        fab = findViewById(R.id.fab);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.presenter = new RequestPresenter();
        this.presenter.attachView(this);

        fab.setOnClickListener(view -> presenter.submitForm());

        this.content.addTextChangedListener(presenter);
        this.emoji.addTextChangedListener(presenter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void setContentErrorMessage(String message) {
        content.setError(message);
    }

    @Override
    public void setEmojiErrorMessage(String message) {
        emoji.setError(message);
    }

    @Override
    public void removeEmojiErrorMessage() {
        emoji.setError(null);
    }

    @Override
    public void removeContentErrorMessage() {
        content.setError(null);
    }

    @Override
    public String getCopypasta() {
        return content.getText().toString();
    }

    @Override
    public String getEmoji() {
        return emoji.getText().toString();
    }

    @Override
    public String getTitleString() {
        return title.getText().toString();
    }

    @Override
    public String getTags() {
        return tags.getText().toString();
    }

    @Override
    public boolean getNSFW() {
        return nsfw.isChecked();
    }

    @Override
    public int getCategory() {
        int id = category.getCheckedRadioButtonId();
        switch (id) {
            case R.id.submit_category_copypasta:
                return 0;
            case R.id.submit_category_ascii:
                return 1;
            case R.id.submit_category_emoji:
                return 2;
        }
        return -1;
    }

    @Override
    public void sendEmail(String receivingAddress, String subject, String content) {
        Uri uri = Uri.fromParts(
                "mailto", receivingAddress, null);
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, content);
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}
