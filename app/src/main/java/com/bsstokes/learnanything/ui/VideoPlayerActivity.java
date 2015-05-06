package com.bsstokes.learnanything.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsstokes.learnanything.R;
import com.bsstokes.learnanything.api.KhanAcademyApi;
import com.bsstokes.learnanything.data.transformers.CursorToVideo;
import com.bsstokes.learnanything.db.Database;
import com.bsstokes.learnanything.models.Video;
import com.bsstokes.learnanything.sync.rx.EndlessObserver;
import com.bsstokes.learnanything.ui.video.VideoLoader;
import com.bsstokes.learnanything.ui.video.VideoPresenter;
import com.bsstokes.learnanything.ui.video.VideoView;
import com.squareup.picasso.Picasso;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;
import rx.functions.Action1;

public class VideoPlayerActivity extends BaseActionBarActivity implements VideoView {

    private static final String EXTRA_VIDEO_ID = "videoId";
    private static final String EXTRA_VIDEO_TITLE = "videoTitle";
    private static final String EXTRA_TOP_TOPIC_SLUG = "topTopicSlug";

    private static final String TAG = "VideoPlayerActivity";

    public static void startActivity(Context context, String videoId, String videoTitle, String topTopicSlug) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra(EXTRA_VIDEO_ID, videoId);
        intent.putExtra(EXTRA_VIDEO_TITLE, videoTitle);
        intent.putExtra(EXTRA_TOP_TOPIC_SLUG, topTopicSlug);
        context.startActivity(intent);
    }

    @InjectView(R.id.video_image_view)
    ImageView mVideoImageView;

    @InjectView(R.id.header_section)
    View mHeaderSection;

    @InjectView(R.id.title_text_view)
    TextView mTitleTextView;

    @InjectView(R.id.description_text_view)
    TextView mDescriptionTextView;

    private String mVideoId;
    private String mVideoTitle;
    private String mTopTopicSlug;

    private VideoPresenter videoPresenter;

    @Inject
    Database database;

    @Inject
    KhanAcademyApi khanAcademyApi;

    @Override
    protected int getContentView() {
        return R.layout.activity_video_player;
    }

    @Override
    @Nullable
    protected View getHeaderSectionView() {
        return mHeaderSection;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getMainApplication().component().inject(this);

        Log.d(TAG, "database=" + database);
        Log.d(TAG, "khanAcademyApi=" + khanAcademyApi);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            mVideoId = extras.getString(EXTRA_VIDEO_ID, mVideoId);
            mVideoTitle = extras.getString(EXTRA_VIDEO_TITLE, mVideoTitle);
            mTopTopicSlug = extras.getString(EXTRA_TOP_TOPIC_SLUG, mTopTopicSlug);
        }

        configureColors(mTopTopicSlug);

        videoPresenter = new VideoPresenter(this);

        VideoLoader videoLoader = new VideoLoader();
        videoLoader.loadVideo(khanAcademyApi, mVideoId)
                .subscribe(new EndlessObserver<Video>() {
                    @Override
                    public void onNext(Video video) {
                        videoPresenter.setVideo(video);
                        long createResult = database.createOrUpdate(video);
                        Log.d(TAG, "createResult=" + createResult);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        String message = "Oops. Download failed!";
                        toast(message);
                        Log.e(TAG, message, throwable);
                    }
                });

        videoPresenter.update();

        database.getVideoByReadableId(mVideoId)
                .subscribe(new Action1<SqlBrite.Query>() {
                    @Override
                    public void call(SqlBrite.Query query) {
                        Log.d(TAG, "query = " + query);
                        try (Cursor cursor = query.run()) {
                            List<Video> values = new ArrayList<>(cursor.getCount());

                            while (cursor.moveToNext()) {
                                Video video = new CursorToVideo().call(cursor);
                                values.add(video);
                            }

                            for (Video video : values) {
                                Log.d(TAG, "Single Video: " + video.getId());
                            }
                        }
                    }
                });

        database.getAllVideos()
                .subscribe(new Action1<SqlBrite.Query>() {
                    @Override
                    public void call(SqlBrite.Query query) {
                        try (Cursor cursor = query.run()) {
                            List<Video> values = new ArrayList<>(cursor.getCount());

                            while (cursor.moveToNext()) {
                                Video video = new CursorToVideo().call(cursor);
                                values.add(video);
                            }

                            for (Video video : values) {
                                Log.d(TAG, "Video: " + video.getId());
                            }
                        }
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

    @OnClick(R.id.video_image_view)
    void onClickVideoImageView() {
        videoPresenter.openVideo();
    }

    @OnClick(R.id.watch_video_button)
    void onClickWatchVideoButton() {
        videoPresenter.openVideo();
    }

    @Override
    public void openVideoUrl(@NonNull String videoUrl) {
        Uri uri = Uri.parse(videoUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onLoading() {
        setTitle(mVideoTitle);
        mTitleTextView.setText(mVideoTitle);
        mDescriptionTextView.setText(null);

        toast("onLoading");
    }

    @Override
    public void setVideoTitle(String title) {
        setTitle(title);
        mTitleTextView.setText(title);
    }

    @Override
    public void setVideoHtmlDescription(String htmlDescription) {
        if (TextUtils.isEmpty(htmlDescription)) {
            mDescriptionTextView.setVisibility(View.GONE);
        } else {
            mDescriptionTextView.setVisibility(View.VISIBLE);
            mDescriptionTextView.setText(Html.fromHtml(htmlDescription));
        }
    }

    @Override
    public void setVideoImageUrl(String imageUrl) {
        Picasso.with(this).load(imageUrl).into(mVideoImageView);
    }
}
