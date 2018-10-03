package io.github.httpmattpvaughn.spaghetti.request;

import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;

/**
 * Created by Matt Vaughn: http://mattpvaughn.github.io/
 */
public interface RequestContract {
    interface View {
        // Show the user a message indicating an error in the content field
        void setContentErrorMessage(String message);

        // Show the user a message indicating an error in the emoji field
        void setEmojiErrorMessage(String message);

        // Removes the error status indicator from the emoji field
        void removeEmojiErrorMessage();

        // Removes the error status indicator from the content field
        void removeContentErrorMessage();

        String getCopypasta();

        // Get the user's "emoji" input string, single emoji character expected
        String getEmoji();

        // Get the user's "title" input string
        String getTitleString();

        // Get the user's "tags" input string, comma-separated list expected
        String getTags();

        // Get state of the NSFW checkbox
        boolean getNSFW();

        // Get the user's category input, corresponding to category field in
        // data/CopyPasta.java
        int getCategory();

        // Allows a user to send an email to a given address with a provided
        // subject and content.
        void sendEmail(String receivingAddress, String subject, String content);
    }

    interface Presenter extends TextWatcher {
        // Attaches a view to the presenter
        void attachView(RequestContract.View view);

        // Detaches the current view from the present
        void detachView();

        // Checks whether the form is valid
        boolean isFormValid();

        // Checks whether the input in the copypasta field is valid. Valid
        // content for the copypasta field is anything other an empty string
        boolean isCopypastaValid(String content);

        // Checks whether the emoji field in the view is valid. Valid content
        // for the emoji field is a string containing a single emoji or unicode
        // character.
        //
        // Note: Emojis rendered as a single emoji that are in fact two combined
        // with a zero-width joiner are not permitted at this time because I do
        // not know how to distinguish those from just two emojis.
        boolean isEmojiValid(String copypasta);

        // Checks for validity and sends the form where it needs to go
        void submitForm();

        // Convert the form data to a String
        String generateContentString();

        // Send the HTML as a string to server
        void sendHTML(String content);

        @Override
        void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2);

        // The presenter will run this when any fields required validation
        // in the form are changed
        @Override
        void onTextChanged(CharSequence charSequence, int i, int i1, int i2);

        @Override
        void afterTextChanged(Editable editable);
    }
}
