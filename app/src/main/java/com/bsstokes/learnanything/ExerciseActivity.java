package com.bsstokes.learnanything;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsstokes.learnanything.api.KhanAcademyApi;
import com.bsstokes.learnanything.api.models.Exercise;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ExerciseActivity extends ActionBarActivity {

    public static final String EXTRA_EXERCISE_NAME = "exerciseName";

    public static Intent buildIntent(Context context, String exerciseName) {
        Intent intent = new Intent(context, ExerciseActivity.class);
        intent.putExtra(EXTRA_EXERCISE_NAME, exerciseName);
        return intent;
    }

    @InjectView(R.id.exercise_image_view)
    ImageView mExerciseImageView;

    @InjectView(R.id.title_text_view)
    TextView mTitleTextView;

    private Exercise mExercise;
    private String mExerciseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        ButterKnife.inject(this);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            mExerciseName = extras.getString(EXTRA_EXERCISE_NAME, mExerciseName);
        }

        KhanAcademyApi.Client apiClient = new KhanAcademyApi.Client();
        apiClient.getExercise(mExerciseName, new Callback<Exercise>() {
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

    private void updateUI() {
        if (null == mExercise) {
            return;
        }

        String title = mExercise.translated_pretty_display_name;
        setTitle(title);
        mTitleTextView.setText(title);

        String imageUrl = mExercise.image_url;
        Picasso.with(this).load(imageUrl).into(mExerciseImageView);
    }
}
