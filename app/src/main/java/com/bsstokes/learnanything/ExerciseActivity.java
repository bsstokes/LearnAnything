package com.bsstokes.learnanything;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

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

    public static final String EXTRA_EXERCISE_NAME = "exerciseName";

    public static void startActivity(Context context, String exerciseName) {
        Intent intent = new Intent(context, ExerciseActivity.class);
        intent.putExtra(EXTRA_EXERCISE_NAME, exerciseName);
        context.startActivity(intent);
    }

    @InjectView(R.id.exercise_image_view)
    ImageView mExerciseImageView;

    @InjectView(R.id.title_text_view)
    TextView mTitleTextView;

    @InjectView(R.id.description_text_view)
    TextView mDescriptionTextView;

    private Exercise mExercise;
    private String mExerciseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            mExerciseName = extras.getString(EXTRA_EXERCISE_NAME, mExerciseName);
        }

        KhanAcademyApi api = new KhanAcademyApi();
        api.getExercise(mExerciseName, new Callback<Exercise>() {
            @Override
            public void success(Exercise exercise, Response response) {
                mExercise = exercise;
                updateUI();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
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
