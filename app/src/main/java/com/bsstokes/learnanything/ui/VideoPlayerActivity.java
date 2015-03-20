package com.bsstokes.learnanything.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bsstokes.learnanything.R;
import com.bsstokes.learnanything.api.Categories;
import com.bsstokes.learnanything.api.KhanAcademyApi;
import com.bsstokes.learnanything.api.models.Video;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class VideoPlayerActivity extends ActionBarActivity {

    public static final String EXTRA_VIDEO_ID = "videoId";
    public static final String EXTRA_VIDEO_TITLE = "videoTitle";
    public static final String EXTRA_TOP_TOPIC_SLUG = "topTopicSlug";

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
    private VideoAdapter mVideoAdapter;
    private String mTopTopicSlug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            mVideoId = extras.getString(EXTRA_VIDEO_ID, mVideoId);
            mVideoTitle = extras.getString(EXTRA_VIDEO_TITLE, mVideoTitle);
            mTopTopicSlug = extras.getString(EXTRA_TOP_TOPIC_SLUG, mTopTopicSlug);
        }

        mVideoAdapter = new VideoAdapter(mVideoTitle);

        configureColors();

        KhanAcademyApi api = new KhanAcademyApi();
        api.getVideo(mVideoId, new Callback<Video>() {
            @Override
            public void success(Video video, Response response) {
                mVideoAdapter.setVideo(video);
                updateUI();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(VideoPlayerActivity.this, "Oops. Download failed!", Toast.LENGTH_SHORT).show();
            }
        });

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
        setTitle(mVideoAdapter.getTitle());
        mTitleTextView.setText(mVideoAdapter.getTitle());

        if (mVideoAdapter.hasHtmlDescription()) {
            mDescriptionTextView.setVisibility(View.VISIBLE);
            mDescriptionTextView.setText(Html.fromHtml(mVideoAdapter.getHtmlDescription()));
        } else {
            mDescriptionTextView.setVisibility(View.GONE);
        }

        String imageUrl = mVideoAdapter.getImageUrl();
        Picasso.with(this).load(imageUrl).into(mVideoImageView);

        String message = mVideoAdapter.getTitle() + "/" + mVideoAdapter.getImageUrl();
        Toast.makeText(VideoPlayerActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.video_image_view)
    void onClickVideoImageView() {
        onOpenVideo();
    }

    @OnClick(R.id.watch_video_button)
    void onClickWatchVideoButton() {
        onOpenVideo();
    }

    private void onOpenVideo() {
        String videoUrl = mVideoAdapter.getVideoUrl();
        if (null != videoUrl && !TextUtils.isEmpty(videoUrl)) {
            openVideoUrl(videoUrl);
        }
    }

    private void openVideoUrl(@NonNull String videoUrl) {
        Uri uri = Uri.parse(videoUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private static class VideoAdapter {
        private Video video;
        private String defaultTitle;

        public VideoAdapter(String defaultTitle) {
            this.defaultTitle = defaultTitle;
        }

        public void setVideo(Video video) {
            this.video = video;
        }

        public String getTitle() {
            if (null == video) {
                return defaultTitle;
            } else {
                return video.translated_title;
            }
        }

        public String getHtmlDescription() {
            if (null == video) {
                return null;
            } else {
                return video.translated_description_html;
            }
        }

        public boolean hasHtmlDescription() {
            return !TextUtils.isEmpty(getHtmlDescription());
        }

        public String getImageUrl() {
            if (null == video) {
                return null;
            } else if (null != video.download_urls && video.download_urls.containsKey("png")) {
                return video.download_urls.get("png");
            } else {
                return video.image_url;
            }
        }

        public String getVideoUrl() {
            if (null == video) {
                return null;
            } else {
                return video.url;
            }
        }
    }
}
