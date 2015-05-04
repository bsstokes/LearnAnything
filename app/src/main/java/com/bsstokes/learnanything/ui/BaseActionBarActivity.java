package com.bsstokes.learnanything.ui;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import com.bsstokes.learnanything.api.Categories;

import butterknife.ButterKnife;

public abstract class BaseActionBarActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @LayoutRes
    protected abstract int getContentView();

    @Nullable
    protected abstract View getHeaderSectionView();

    protected void configureColors(String topicSlug) {
        int colorResId = Categories.getColorForCategory(topicSlug);
        int color = getResources().getColor(colorResId);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));

        View headerSectionView = getHeaderSectionView();
        if (null != headerSectionView) {
            headerSectionView.setBackgroundColor(color);
        }
    }

    protected void toast(CharSequence text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
