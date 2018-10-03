package io.github.httpmattpvaughn.spaghetti.request;

import android.text.Editable;

/**
 * Created by Matt Vaughn: http://mattpvaughn.github.io/
 */
public class RequestPresenter implements RequestContract.Presenter {
    private static final String RECEIVING_EMAIL_ADDRESS = "uppackdev@gmail.com";
    private static final String EMAIL_SUBJECT = "Copypasta Request";
    private RequestContract.View view;

    @Override
    public void attachView(RequestContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public boolean isFormValid() {
        String content = view.getCopypasta();
        String emoji = view.getEmoji();
        // Check that submit_content and submit_emoji are valid
        return isCopypastaValid(content) && isEmojiValid(emoji);
    }

    @Override
    public boolean isCopypastaValid(String copypasta) {
        if (copypasta == null || copypasta.isEmpty()) {
            view.setContentErrorMessage("Copypasta cannot be empty");
            return false;
        }

        view.removeContentErrorMessage();
        return true;
    }

    @Override
    public boolean isEmojiValid(String emoji) {
        if (emoji == null || emoji.isEmpty()) {
            view.setEmojiErrorMessage("Emoji cannot be empty");
            return false;
        }

        if (emoji.codePointCount(0, emoji.length()) != 1) {
            view.setEmojiErrorMessage("Only one emoji is permitted");
            return false;
        }

        view.removeEmojiErrorMessage();

        return true;
    }

    @Override
    public void submitForm() {
        if (isFormValid()) {
            sendHTML(generateContentString());
        }
    }

    @Override
    public String generateContentString() {
        return "Title: " + view.getTitleString() +
                "\r\n" + "Content: " + view.getCopypasta() +
                "\r\n" + "Emoji: " + view.getEmoji() +
                "\r\n" + "Editable: " + "0" +
                "\r\n" + "Tags: " + view.getTags().replaceAll(", ", "|") +
                "\r\n" + "NSFW: " + (view.getNSFW() ? 1 : 0) +
                "\r\n" + "Category: " + view.getCategory();
    }

    @Override
    public void sendHTML(String content) {
        view.sendEmail(RECEIVING_EMAIL_ADDRESS, EMAIL_SUBJECT, content);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Who cares
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        isFormValid();
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // Boooooring
    }
}
