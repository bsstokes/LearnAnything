package com.bsstokes.learnanything;

import android.content.Context;
import android.content.Intent;
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

    public static Intent buildIntent(Context context, String videoId) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra(EXTRA_VIDEO_ID, videoId);
        return intent;
    }

    @InjectView(R.id.video_image_view)
    ImageView mVideoImageView;

    @InjectView(R.id.title_text_view)
    TextView mTitleTextView;

    @InjectView(R.id.description_text_view)
    TextView mDescriptionTextView;

    private String mVideoId;
    private VideoAdapter mVideoAdapter = new VideoAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            mVideoId = extras.getString(EXTRA_VIDEO_ID, mVideoId);
        }

        KhanAcademyApi.Client client = new KhanAcademyApi.Client();
        client.getVideo(mVideoId, new Callback<Video>() {
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

        public void setVideo(Video video) {
            this.video = video;
        }

        public String getTitle() {
            if (null == video) {
                return null;
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
