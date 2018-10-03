package io.github.httpmattpvaughn.spaghetti.copypasta;

import android.content.Context;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;

import static android.view.inputmethod.EditorInfo.IME_ACTION_NEXT;

/**
 * Given a string formatted like so: "Hello my name is {{PLACEHOLDER}}, how are you?"
 * and a textview, format the textview such that the placeholder text can be clicked
 * on to input a new word to replace it.
 * <p>
 * I.E. Start with "Hello my name is {{PLACEHOLDER}}, how are you?", then you click
 * on placeholder, type in NEW_STRING, and it will change to "Hello my name is
 * {{NEW_STRING}}, how are you?"
 */

// TODO- move the input box and scroll the textview so that the active word is
// always visible
public final class MadLibTemplater {
    // The original template
    private final String templateString;

    // Break the strings like "Hello my name is ", "PLACEHOLDER", ", how are you?"
    private List<String> segments;
    private EditText editText;
    private TextView madlib;
    private TextWatcher watcher;

    public static MadLibTemplater from(TextView madlib) {
        return new MadLibTemplater(madlib.getText().toString(), madlib);
    }

    private MadLibTemplater(String string, TextView madlib) {
        this.templateString = string;
        this.madlib = madlib;
        this.segments = makeSegments(string);
    }

    private List<String> makeSegments(String string) {
        String[] splitted = string.split("([{}]{2})");
        return Arrays.asList(splitted);
    }


    public void apply() {
        madlib.setText(makeSpannable(templateString));
        madlib.setMovementMethod(new LinkMovementMethod());
    }

    public SpannableStringBuilder makeSpannable(String string) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append(string.replaceAll("[{}]", ""));

        boolean isBlank = false;

        int lengthConsumed = 0;
        for (int i = 0; i < segments.size(); i++) {
            int currentLength = segments.get(i).length();
            if (isBlank) {
                int finalI = i;
                int finalLengthConsumed = lengthConsumed;
                spannableStringBuilder.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View view) {
                        onSpanClick(view, finalI);
                    }
                }, lengthConsumed, currentLength + lengthConsumed, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            isBlank = !isBlank;
            lengthConsumed += currentLength;
        }

        return spannableStringBuilder;
    }

    public List<String> getSegments() {
        return segments;
    }

    public void onSpanClick(View view, int segment) {
        // When a spannable is clicked, do something
        if (editText == null) {
            initializeEditText(view);
        }
        // Remove every time so edittext only has listener for most recent
        if (watcher != null) {
            editText.removeTextChangedListener(watcher);
        }

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == IME_ACTION_NEXT) {
                    SpannableString ssb = (SpannableString) madlib.getText();
                    ClickableSpan[] spans = ssb.getSpans(0, ssb.length(), ClickableSpan.class);
                    Arrays.sort(spans, new Comparator<ClickableSpan>() {
                        @Override
                        public int compare(ClickableSpan o1, ClickableSpan o2) {
                            return ssb.getSpanStart(o1) - ssb.getSpanStart(o2);
                        }
                    });
                    // get next span in the ssb (it will contain segments.get(segment + 2) and be at index > spanEndIndex)
                    int index = segment / 2 + 1;
                    if (index < spans.length) {
                        spans[index].onClick(textView);
                    }
                }
                return true;
            }
        });
        watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // change the segment, then update the base text
                segments.set(segment, charSequence.toString());
                redraw();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        editText.setText(null);
        editText.setHint(segments.get(segment));
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

        // When you type into the edittext
        editText.addTextChangedListener(watcher);
    }

    private void redraw() {
        //
        String body = segmentToString(segments);
        SpannableStringBuilder spannableStringBuilder = makeSpannable(body);
        madlib.setText(spannableStringBuilder);
    }

    private String segmentToString(List<String> segments) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < segments.size(); i++) {
            sb.append(segments.get(i));
        }
        return sb.toString();
    }

    private void initializeEditText(View view) {
        editText = new EditText(view.getContext());
        editText.setLines(1);
        editText.setSingleLine();
        editText.setMaxLines(1);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setImeOptions(IME_ACTION_NEXT);
        ViewGroup parent = (ViewGroup) view.getParent();
        parent.addView(editText, 0);
    }
}
