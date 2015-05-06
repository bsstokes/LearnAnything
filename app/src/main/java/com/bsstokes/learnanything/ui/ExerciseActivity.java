package com.bsstokes.learnanything.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsstokes.learnanything.R;
import com.bsstokes.learnanything.api.Categories;
import com.bsstokes.learnanything.api.KhanAcademyApi;
import com.bsstokes.learnanything.data.transformers.ApiExerciseToExercise;
import com.bsstokes.learnanything.db.Database;
import com.bsstokes.learnanything.models.Exercise;
import com.bsstokes.learnanything.sync.rx.EndlessObserver;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class ExerciseActivity extends BaseActionBarActivity {

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

    @Inject
    KhanAcademyApi khanAcademyApi;

    @Inject
    Database database;

    private Exercise mExercise;
    private String mExerciseId;
    private String mExerciseTitle;
    private String mTopTopicSlug;

    @Override
    protected int getContentView() {
        return R.layout.activity_exercise;
    }

    @Nullable
    @Override
    protected View getHeaderSectionView() {
        return mHeaderSection;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getMainApplication().component().inject(this);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            mExerciseId = extras.getString(EXTRA_EXERCISE_ID, mExerciseId);
            mExerciseTitle = extras.getString(EXTRA_EXERCISE_TITLE, mExerciseTitle);
            mTopTopicSlug = extras.getString(EXTRA_TOP_TOPIC_SLUG, mTopTopicSlug);
        }

        Observable<com.bsstokes.learnanything.api.models.Exercise> deferredObservable = Observable.defer(new Func0<Observable<com.bsstokes.learnanything.api.models.Exercise>>() {
            @Override
            public Observable<com.bsstokes.learnanything.api.models.Exercise> call() {
                return Observable.just(khanAcademyApi.getExercise(mExerciseId));
            }
        });

        deferredObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new ApiExerciseToExercise())
                .subscribe(new EndlessObserver<Exercise>() {
                    @Override
                    public void onNext(Exercise exercise) {
                        database.createOrUpdate(exercise);
                        mExercise = exercise;
                        updateUI();
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

        String title = mExercise.getTitle();
        setTitle(title);
        mTitleTextView.setText(title);

        String imageUrl = mExercise.getImageUrl();
        Picasso.with(this).load(imageUrl).into(mExerciseImageView);

        mDescriptionTextView.setText(mExercise.getDescription());
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
        if (null != mExercise && !TextUtils.isEmpty(mExercise.getUrl())) {
            Uri uri = Uri.parse(mExercise.getUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
}
