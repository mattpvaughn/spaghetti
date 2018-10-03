package io.github.httpmattpvaughn.spaghetti.copypasta.request;

import android.widget.TextView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import io.github.httpmattpvaughn.spaghetti.copypasta.CopypastaActivity;
import io.github.httpmattpvaughn.spaghetti.copypasta.CopypastaPresenter;
import io.github.httpmattpvaughn.spaghetti.copypasta.MadLibTemplater;
import io.github.httpmattpvaughn.spaghetti.copypasta.copypasta.CopypastaTest;
import io.github.httpmattpvaughn.spaghetti.copypasta.data.MockCopypastaManager;
import io.github.httpmattpvaughn.spaghetti.data.CopypastaRepository;
import io.github.httpmattpvaughn.spaghetti.request.RequestActivity;
import io.github.httpmattpvaughn.spaghetti.request.RequestContract;
import io.github.httpmattpvaughn.spaghetti.request.RequestPresenter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

/**
 * Created by Matt Vaughn: http://mattpvaughn.github.io/
 */

@RunWith(MockitoJUnitRunner.class)
public class RequestTest {

    private static final String EXAMPLE_EMPTY_STRING = "";
    private static final String EXAMPLE_VALID_COPYPASTA_STRING = "copypasta";
    private static final String EXAMPLE_VALID_EMOJI_STRING = "\uD83D\uDC38";
    private static final String EXAMPLE_VALID_UNICODE_CHAR_STRING = "☭";
    private static final String EXAMPLE_EMOJI_INPUT_TOO_LONG = "THIS STRING IS TOO LONG TO BE AN EMOJI";
    private static final String EXAMPLE_JOINED_EMOJIS = "\uD83C\uDDFA\uD83C\uDDF8";
    private static final String EXAMPLE_NULL_STRING = null;
    private RequestActivity view;
    private RequestPresenter presenter;

    @Before
    public void setup() {
        // Mock a fake view with mockito
        this.view = Mockito.mock(RequestActivity.class);

        // Initialize presenter
        this.presenter = Mockito.spy(new RequestPresenter());
        this.presenter.attachView(view);
    }

    @Test
    public void testCopypastaValidatorValidInput() {
        Assert.assertTrue(presenter.isCopypastaValid(EXAMPLE_VALID_COPYPASTA_STRING));
        verify(view).removeContentErrorMessage();
    }

    @Test
    public void testCopypastaValidatorEmptyInput() {
        Assert.assertFalse(presenter.isCopypastaValid(EXAMPLE_EMPTY_STRING));
        verify(view).setContentErrorMessage(anyString());
    }

    @Test
    public void testCopypastaValidatorNullInput() {
        Assert.assertFalse(presenter.isCopypastaValid(EXAMPLE_NULL_STRING));
        verify(view).setContentErrorMessage(anyString());
    }

    @Test
    public void testEmojiValidatorValidInput() {
        Assert.assertTrue(presenter.isEmojiValid(EXAMPLE_VALID_EMOJI_STRING));
        verify(view).removeEmojiErrorMessage();
    }

    @Test
    public void testEmojiValidatorInputTooLong() {
        Assert.assertFalse(presenter.isEmojiValid(EXAMPLE_EMOJI_INPUT_TOO_LONG));
        verify(view).setEmojiErrorMessage(anyString());
    }

    @Test
    public void testEmojiValidatorInputNull() {
        Assert.assertFalse(presenter.isEmojiValid(EXAMPLE_NULL_STRING));
        verify(view).setEmojiErrorMessage(anyString());
    }

    @Test
    public void testEmojiValidatorInputEmpty() {
        Assert.assertFalse(presenter.isEmojiValid(EXAMPLE_EMPTY_STRING));
        verify(view).setEmojiErrorMessage(anyString());
    }

    @Test
    public void testEmojiValidatorZeroWidthJoinedEmojis() {
        Assert.assertFalse(presenter.isEmojiValid(EXAMPLE_JOINED_EMOJIS));
        verify(view).setEmojiErrorMessage(anyString());
    }

    @Test
    public void testEmojiValidatorUnicodeCharacter() {
        Assert.assertTrue(presenter.isEmojiValid(EXAMPLE_VALID_UNICODE_CHAR_STRING));
        verify(view).removeEmojiErrorMessage();
    }

    @Test
    public void testIsFormValidBasic() {
        // Mock return values from the view so we can test validation
        Mockito.when(view.getEmoji()).thenReturn(EXAMPLE_VALID_EMOJI_STRING);
        Mockito.when(view.getCopypasta()).thenReturn(EXAMPLE_VALID_COPYPASTA_STRING);

        presenter.isFormValid();
        verify(view).getCopypasta();
        verify(view).getEmoji();
        verify(presenter).isCopypastaValid(Matchers.eq(EXAMPLE_VALID_COPYPASTA_STRING));
        verify(presenter).isEmojiValid(Matchers.eq(EXAMPLE_VALID_EMOJI_STRING));
    }

    @Test
    public void testSendHTML() {
        presenter.sendHTML(EXAMPLE_VALID_COPYPASTA_STRING);
        verify(view).sendEmail(anyString(), anyString(), Matchers.eq(EXAMPLE_VALID_COPYPASTA_STRING));
    }
}