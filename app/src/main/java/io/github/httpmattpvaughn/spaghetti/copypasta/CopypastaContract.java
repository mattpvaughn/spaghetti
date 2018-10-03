package io.github.httpmattpvaughn.spaghetti.copypasta;

import android.view.View;

import io.github.httpmattpvaughn.spaghetti.data.Copypasta;

/**
 * Created by Matt Vaughn: http://mattpvaughn.github.io/
 *
 *
 * TODO- I would like to merge copyCopypastaTextEditable and
 * copyCopypastaTextNotEditable into the same method, but am unable to
 * find a proper solution using a single textview that will allow
 * MadLibTemplater and full text editing to work within a single view. Will
 * likely end up extending edittext but will require some work. Could also
 * find a compromise that changes the UI and avoids the problem altogether
 */
public interface CopypastaContract {
    interface View {

        // Allows the user to easily replace certain words in template-style
        // copypastas- see MadLibTemplater
        void setCopypastaContentTemplateable(String string);

        // Disables template mode
        void setCopypastaContentNotTemplateable(String string);

        // Sets the title in the toolbar to a certain string
        void setToolbarTitle(String string);

        // Adds a listener of type FabListener to produce an action when the
        // fab is clicked
        void setFabListener(FabListener onFabClick);

        // Changes the view to make the copypasta not editable by the user
        void setEditing();

        // Changes the view to make the copypasta editable by the user (user can
        // change the copypasta with their keyboard, etc.)
        void setNotEditing();

        // Shows the user a message
        void showUserMessage(String s);

        // Copies the content from the textEditable view
        void copyCopypastaTextEditable();

        // Copies the text content from the non-editable view
        void copyCopypastaTextNotEditable();
    }

    interface Presenter {
        // Loads a copypasta matching an id using the CopypastaManager
        void loadCopypasta(int id);

        // Updates the view to represent the data in the copypasta by:
        //  - updating the toolbar title to be the copypasta title
        //  - updating the view content to be the copypasta content
        //  - updating the copy button listener
        //  - updates internal state
        void setCopypasta(Copypasta copypasta);

        // Toggles whether the user is able to edit the copypasta arbitrarily
        void toggleEditingCopypasta();

        // Copies the current copypasta to the clipboard
        void copyCopypastaToClipboard();

        // Attach a view to the presenter
        void attachView(CopypastaContract.View view);

        // Detach the view
        void detachView();
    }

    interface FabListener {
        void onFabClick();
    }

}
