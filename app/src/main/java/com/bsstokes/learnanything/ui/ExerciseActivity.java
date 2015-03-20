package com.bsstokes.learnanything.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsstokes.learnanything.R;
import com.bsstokes.learnanything.api.Categories;
import com.bsstokes.learnanything.api.KhanAcademyApi;
import com.bsstokes.learnanything.api.models.Exercise;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ExerciseActivity extends ActionBarActivity {

    public static final String EXTRA_EXERCISE_ID = "exerciseId";
    public static final String EXTRA_EXERCISE_TITLE = "exerciseTitle";
    public static final String EXTRA_TOP_TOPIC_SLUG = "topTopicSlug";

    public static void startActivity(Context context, String exerciseId, String exerciseTitle, String topTopicSlug) {
        Intent intent = new Intent(context, ExerciseActivity.class);
        intent.putExtra(EXTRA_EXERCISE_ID, exerciseId);
        intent.putExtra(EXTRA_EXERCISE_TITLE, exerciseTitle);
        intent.putExtra(EXTRA_TOP_TOPIC_SLUG, topTopicSlug);
        context.startActivity(intent);
    }

    @InjectView(R.id.exercise_image_view)
    ImageView mExerciseImageView;

    @InjectView(R.id.header_section)
    View mHeaderSection;

    @InjectView(R.id.title_text_view)
    TextView mTitleTextView;

    @InjectView(R.id.description_text_view)
    TextView mDescriptionTextView;

    private Exercise mExercise;
    private String mExerciseId;
    private String mExerciseTitle;
    private String mTopTopicSlug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            mExerciseId = extras.getString(EXTRA_EXERCISE_ID, mExerciseId);
            mExerciseTitle = extras.getString(EXTRA_EXERCISE_TITLE, mExerciseTitle);
            mTopTopicSlug = extras.getString(EXTRA_TOP_TOPIC_SLUG, mTopTopicSlug);
        }

        KhanAcademyApi api = new KhanAcademyApi();
        api.getExercise(mExerciseId, new Callback<Exercise>() {
            @Override
            public void success(Exercise exercise, Response response) {
                mExercise = exercise;
                updateUI();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

        configureColors();
        updateUI();
    }

    private void configureColors() {
        int colorResId = Categories.getColorForCategory(mTopTopicSlug);
        int color = getResources().getColor(colorResId);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        mHeaderSection.setBackgroundColor(color);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {
        if (null == mExercise) {
            setTitle(mExerciseTitle);
            mTitleTextView.setText(mExerciseTitle);

            return;
        }

        String title = mExercise.translated_pretty_display_name;
        setTitle(title);
        mTitleTextView.setText(title);

        String imageUrl = mExercise.image_url;
        Picasso.with(this).load(imageUrl).into(mExerciseImageView);

        mDescriptionTextView.setText(mExercise.translated_description);
    }

    @OnClick(R.id.exercise_image_view)
    void onClickExerciseImageView() {
        openExerciseInBrowser();
    }

    @OnClick(R.id.open_exercise_button)
    void onClickOpenExerciseButton() {
        openExerciseInBrowser();
    }

    private void openExerciseInBrowser() {
        if (null != mExercise && !TextUtils.isEmpty(mExercise.ka_url)) {
            Uri uri = Uri.parse(mExercise.ka_url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
}
