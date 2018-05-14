package com.example.nihad.booksapp;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.StyleSpan;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.List;

public class PageSplitter {
    private final int pageWidth;
    private final int pageHeight;
    private final float lineSpacingMultiplier;
    private final int lineSpacingExtra;
    private final List<CharSequence> pages = new ArrayList<CharSequence>();
    private SpannableStringBuilder currentLine = new SpannableStringBuilder();
    private SpannableStringBuilder currentPage = new SpannableStringBuilder();
    private int currentLineHeight;
    private int pageContentHeight;
    private int currentLineWidth;
    private int textLineHeight;

    public PageSplitter(int pageWidth, int pageHeight, float lineSpacingMultiplier, int lineSpacingExtra, Context context) {
        this.pageWidth = (int) (pageWidth - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, context.getResources().getDisplayMetrics()));
        this.pageHeight = (int) (pageHeight - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics()));
//        this.pageWidth = pageWidth;
//        this.pageHeight = pageHeight;

        this.lineSpacingMultiplier = lineSpacingMultiplier;
        this.lineSpacingExtra = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, lineSpacingExtra, context.getResources().getDisplayMetrics()) + 1;
    }

    public void append(String text, TextPaint textPaint) {
        textLineHeight = (int) Math.ceil(textPaint.getFontMetrics(null) * lineSpacingMultiplier + lineSpacingExtra);
        String[] paragraphs = text.split("\n", -1);
        int i;
        for (i = 0; i < paragraphs.length - 1; i++) {
            appendText(paragraphs[i], textPaint);
            appendNewLine();
        }
        appendText(paragraphs[i], textPaint);
    }

    public void pageBreak(){
        boolean finished = false;
        while(!finished){
            if (pageContentHeight + currentLineHeight > pageHeight) {
                pages.add(currentPage);
                currentPage = new SpannableStringBuilder();
                pageContentHeight = 0;
                finished = true;
            }
            else
                appendNewLine();
        }

    }
    private void appendText(String text, TextPaint textPaint) {
        String[] words = text.split(" ", -1);
        int i;
        for (i = 0; i < words.length - 1; i++) {
            appendWord(words[i] + " ", textPaint);
        }
        appendWord(words[i], textPaint);
    }

    private void appendNewLine() {
        currentLine.append("\n");
        checkForPageEnd();
        appendLineToPage(textLineHeight);
    }

    private void checkForPageEnd() {
        if (pageContentHeight + currentLineHeight > pageHeight) {
            pages.add(currentPage);
            currentPage = new SpannableStringBuilder();
            pageContentHeight = 0;
        }
    }

    private void appendWord(String appendedText, TextPaint textPaint) {
        int textWidth = (int) Math.ceil(textPaint.measureText(appendedText));

        if (currentLineWidth + textWidth >= pageWidth) {
            checkForPageEnd();
            appendLineToPage(textLineHeight);
        }

//        if(appendedText == "" || appendedText == " "){
//            checkForPageEnd();
//            appendLineToPage(textLineHeight);
//        }

        appendTextToLine(appendedText, textPaint, textWidth);
    }

    private void appendLineToPage(int textLineHeight) {
        currentPage.append(currentLine);
        pageContentHeight += currentLineHeight;

        currentLine = new SpannableStringBuilder();
        currentLineHeight = textLineHeight;
        currentLineWidth = 0;
    }

    private void appendTextToLine(String appendedText, TextPaint textPaint, int textWidth) {
        currentLineHeight = Math.max(currentLineHeight, textLineHeight);
        currentLine.append(renderToSpannable(appendedText, textPaint));
        currentLineWidth += textWidth;
    }

    public List<CharSequence> getPages() {
        List<CharSequence> copyPages = new ArrayList<CharSequence>(pages);
        SpannableStringBuilder lastPage = new SpannableStringBuilder(currentPage);

        if (pageContentHeight + currentLineHeight > pageHeight) {
            copyPages.add(lastPage);
            lastPage = new SpannableStringBuilder();
        }
        lastPage.append(currentLine);
        copyPages.add(lastPage);
        return copyPages;
    }

    private SpannableString renderToSpannable(String text, TextPaint textPaint) {
        SpannableString spannable = new SpannableString(text);

        if (textPaint.isFakeBoldText()) {
            spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, spannable.length(), 0);
        }
        return spannable;
    }

    public Integer getCount() {
        return pages.size();
    }
}
