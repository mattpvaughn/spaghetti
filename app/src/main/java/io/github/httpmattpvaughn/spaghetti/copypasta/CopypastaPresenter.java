package io.github.httpmattpvaughn.spaghetti.copypasta;

import androidx.annotation.Nullable;
import io.github.httpmattpvaughn.spaghetti.data.Copypasta;
import io.github.httpmattpvaughn.spaghetti.data.CopypastaRepository;

/**
 * Created by Matt Vaughn: http://mattpvaughn.github.io/
 */
public class CopypastaPresenter implements CopypastaContract.Presenter, CopypastaContract.FabListener {
    private static final int TOOLBAR_TITLE_MAX_LENGTH = 100;
    private CopypastaContract.View view;
    private CopypastaRepository repository;
    private Copypasta copypasta;
    private boolean editing;

    public CopypastaPresenter(CopypastaRepository copypastaRepository) {
        this.repository = copypastaRepository;
        this.editing = false;
    }

    @Override
    public void loadCopypasta(int id) {
        repository.loadSingleCopypasta(copypasta -> {
            setCopypasta(copypasta);
        }, id);
    }

    @Override
    public void setCopypasta(@Nullable Copypasta copypasta) {
        if(copypasta == null) {
            throw new IllegalArgumentException("Copypasta cannot be null");
        }

        // Update view title
        if(copypasta.title != null) {
            view.setToolbarTitle(copypasta.title);
        } else {
            view.setToolbarTitle(
                    copypasta.content.substring(
                            0,
                            Math.min(TOOLBAR_TITLE_MAX_LENGTH, copypasta.content.length())));
        }

        // Update the copypasta content
        if(copypasta.isEditable()) {
            view.setCopypastaContentTemplateable(copypasta.content);
        } else {
            view.setCopypastaContentNotTemplateable(copypasta.content);
        }

        // updating copy button listener
        view.setFabListener(this);
        this.copypasta = copypasta;
    }

    @Override
    public void toggleEditingCopypasta() {
        if (copypasta == null) {
            throw new IllegalStateException("Tried to call toggleEditingCopypasta before copypasta had loaded!");
        }

        editing = !editing;
        if(copypasta.isEditable()) {
            view.showUserMessage(editing ? "Note: editing currently disables custom input" : "Editing disabled.");
        } else {
            view.showUserMessage(editing ? "Editing enabled!" : "Editing disabled.");
        }
        if(editing) {
            view.setEditing();
        } else {
            view.setNotEditing();
        }
    }

    @Override
    public void copyCopypastaToClipboard() {
        if(editing) {
            view.copyCopypastaTextEditable();
        } else {
            view.copyCopypastaTextNotEditable();
        }
    }

    @Override
    public void attachView(CopypastaContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void onFabClick() {
        copyCopypastaToClipboard();
    }
}
