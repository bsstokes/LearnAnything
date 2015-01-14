package com.bsstokes.learnanything;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
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
    private Video mVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ButterKnife.inject(this);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            mVideoId = extras.getString(EXTRA_VIDEO_ID, mVideoId);
        }

        KhanAcademyApi.Client client = new KhanAcademyApi.Client();
        client.getVideo(mVideoId, new Callback<Video>() {
            @Override
            public void success(Video video, Response response) {
                mVideo = video;
                updateUI();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void updateUI() {
        if (null == mVideo) {
            return;
        }

        setTitle(mVideo.translated_title);
        mTitleTextView.setText(mVideo.translated_title);

        mDescriptionTextView.setText(Html.fromHtml(mVideo.translated_description_html));

        String imageUrl = getImageUrl(mVideo);
        Picasso.with(this).load(imageUrl).into(mVideoImageView);

        String message = mVideo.translated_title + "/" + mVideo.url;
        Toast.makeText(VideoPlayerActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private String getImageUrl(Video video) {
        if (null != video.download_urls && video.download_urls.containsKey("png")) {
            Log.d("BS", "Using download_urls[png]");
            return video.download_urls.get("png");
        } else {
            Log.d("BS", "Using image_url");
            return video.image_url;
        }
    }

    @OnClick(R.id.video_image_view)
    void onClickVideoImageView() {
        if (null != mVideo && !TextUtils.isEmpty(mVideo.url)) {
            Uri uri = Uri.parse(mVideo.url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
}
