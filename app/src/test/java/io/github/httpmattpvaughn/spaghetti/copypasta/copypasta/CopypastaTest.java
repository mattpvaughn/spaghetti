package io.github.httpmattpvaughn.spaghetti.copypasta.copypasta;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import io.github.httpmattpvaughn.spaghetti.HomeContract;
import io.github.httpmattpvaughn.spaghetti.HomePresenter;
import io.github.httpmattpvaughn.spaghetti.copypasta.CopypastaActivity;
import io.github.httpmattpvaughn.spaghetti.copypasta.CopypastaContract;
import io.github.httpmattpvaughn.spaghetti.copypasta.CopypastaPresenter;
import io.github.httpmattpvaughn.spaghetti.copypasta.HomeTest;
import io.github.httpmattpvaughn.spaghetti.copypasta.data.MockCopypastaManager;
import io.github.httpmattpvaughn.spaghetti.copypasta.settings.MockAppSettingsImpl;
import io.github.httpmattpvaughn.spaghetti.data.Copypasta;
import io.github.httpmattpvaughn.spaghetti.data.CopypastaRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

/**
 * Created by Matt Vaughn: http://mattpvaughn.github.io/
 */
@RunWith(MockitoJUnitRunner.class)
public class CopypastaTest {

    private CopypastaContract.Presenter presenter;
    private CopypastaRepository repository;
    private Copypasta copypasta;

    // A mocked view
    private CopypastaContract.View view;

    @Before
    public void setup() {
        // Mock a fake view with mockito
        this.view = Mockito.mock(CopypastaActivity.class);

        // Create a fake copypasta repository
        this.repository = new MockCopypastaManager();

        // Initialize presenter
        this.presenter = Mockito.spy(new CopypastaPresenter(repository));
        this.presenter.attachView(view);

        // Create a fake copypasta
        this.repository.loadSingleCopypasta(copypasta -> CopypastaTest.this.copypasta = copypasta, 0);
    }

    // Test that loading a single copypasta from the repository will result in
    // setCopypasta() being called
    @Test
    public void loadCopypastaBasic() {
        // Note: the ID passed here will not matter with our implementation of
        // MockCopypastaManager
        presenter.loadCopypasta(copypasta.id);
        verify(presenter).setCopypasta(copypasta);
    }

    // Test that setting a generic copypasta will result in the view changing
    // the toolbar title, the fab onclick listener
    @Test
    public void testSetCopypastaBasic() {
        presenter.setCopypasta(copypasta);
        verify(view).setToolbarTitle(copypasta.title);
        verify(view).setFabListener(any());
    }

    // Test that setting a null copypasta will result in the presenter throwing
    // an exception
    @Test
    public void testSetCopypastaNull() {
        try {
            presenter.setCopypasta(null);
            fail("Presenter should have through IllegalArgumentException");
        } catch (IllegalArgumentException arg) {
            // Success
        }
    }

    // Test that a copypasta with editable = 0 will result in the view loading
    // the content in as not in template-style
    @Test
    public void testSetCopypastaNotTemplateStyle() {
        copypasta.editable = 0;
        presenter.setCopypasta(copypasta);
        verify(view).setCopypastaContentNotTemplateable(copypasta.content);
    }

    // Test that passing setCopypasta(Copypasta) on a copypasta that is
    // editable will result in the copypasta being loaded as in template-style
    @Test
    public void testSetCopypastaTemplateStyle() {
        copypasta.editable = 1;
        presenter.setCopypasta(copypasta);
        verify(view).setCopypastaContentTemplateable(copypasta.content);
    }

    // Test that calling setCopypasta on a copypasta with title=null will result
    // in the toolbar title being set to a substring of the copypasta.content
    @Test
    public void testSetCopypastaNoTitle() {
        copypasta.title = null;
        presenter.setCopypasta(copypasta);
        verify(view).setToolbarTitle(copypasta.content.substring(0, Math.min(copypasta.content.length(), 100)));
    }


    // Test that calling presenter.toggleEditingCopypasta() after the presenter
    // is first initialized will result the view showing the copypasta in an
    // editing state and will display a message to the user indicating that
    @Test
    public void testToggleCopypastaEditableEditing() {
        presenter.setCopypasta(copypasta);
        presenter.toggleEditingCopypasta();
        verify(view).showUserMessage(anyString());
        verify(view).setEditing();
    }

    // Test that calling presenter.toggleEditingCopypasta() twice will result
    // in the view being in a non-editing state
    @Test
    public void testToggleCopypastaEditableNotEditing() {
        presenter.setCopypasta(copypasta);
        presenter.toggleEditingCopypasta();
        presenter.toggleEditingCopypasta();
        verify(view).setNotEditing();
    }

    // Test that for a default presenter copyCopypastaToClipboard will call
    // view.copyCopypastaTextNotEditable()
    @Test
    public void testCopyCopypastaToClipboard() {
        presenter.setCopypasta(copypasta);
        presenter.copyCopypastaToClipboard();
        verify(view).copyCopypastaTextNotEditable();
    }
}