package com.bsstokes.learnanything.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.bsstokes.learnanything.R;
import com.bsstokes.learnanything.api.Categories;
import com.bsstokes.learnanything.api.KhanAcademyApi;
import com.bsstokes.learnanything.api.models.Article;
import com.bsstokes.learnanything.sync.rx.EndlessObserver;
import com.squareup.phrase.Phrase;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ArticleActivity extends ActionBarActivity {

    public static final String EXTRA_ARTICLE_INTERNAL_ID = "articleInternalId";
    public static final String EXTRA_ARTICLE_TITLE = "articleTitle";
    public static final String EXTRA_TOP_TOPIC_SLUG = "topTopicSlug";

    public static void startActivity(Context context, String articleInternalId, String articleTitle, String topTopicSlug) {
        Intent intent = new Intent(context, ArticleActivity.class);
        intent.putExtra(EXTRA_ARTICLE_INTERNAL_ID, articleInternalId);
        intent.putExtra(EXTRA_ARTICLE_TITLE, articleTitle);
        intent.putExtra(EXTRA_TOP_TOPIC_SLUG, topTopicSlug);
        context.startActivity(intent);
    }

    @InjectView(R.id.header_section)
    View mHeaderSection;

    @InjectView(R.id.title_text_view)
    TextView mTitleTextView;

    @InjectView(R.id.content_web_view)
    WebView mContentWebView;

    private Article mArticle;
    private String mArticleInternalId;
    private String mArticleTitle;
    private String mTopTopicSlug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            mArticleInternalId = extras.getString(EXTRA_ARTICLE_INTERNAL_ID, mArticleInternalId);
            mArticleTitle = extras.getString(EXTRA_ARTICLE_TITLE, mArticleTitle);
            mTopTopicSlug = extras.getString(EXTRA_TOP_TOPIC_SLUG, mTopTopicSlug);
        }

        KhanAcademyApi api = new KhanAcademyApi();
        api.getArticle(mArticleInternalId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new EndlessObserver<Article>() {
                    @Override
                    public void onNext(Article article) {
                        mArticle = article;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (android.R.id.home):
                onBackPressed();
                return true;

            case (R.id.action_open_in_browser):
                openArticleInBrowser();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {
        if (null == mArticle) {
            setTitle(mArticleTitle);
            mTitleTextView.setText(mArticleTitle);

            return;
        }

        String title = mArticle.translated_title;
        setTitle(title);
        mTitleTextView.setText(title);

        String htmlContent = mArticle.translated_html_content;
        String htmlPage = HtmlPageGenerator.generateHtmlPage(htmlContent);
        mContentWebView.loadData(htmlPage, "text/html", "UTF-8");
    }

    private static class HtmlPageGenerator {

        private static final String TEMPLATE = "<html><head><style type=\"text/css\">{css}</style></head><body>{body}</body></html>";
        private static final String CSS = "*{font-family:sans-serif;line-height:1.4em;}body{padding:1.4em;}.block-image img{margin-left:0;width:auto;max-width:100%;}h1{font-size:1.4em}";

        public static String generateHtmlPage(String body) {
            return Phrase.from(TEMPLATE)
                    .put("css", CSS)
                    .put("body", body)
                    .format()
                    .toString();
        }
    }

    private void openArticleInBrowser() {
        if (null != mArticle) {
            String relative_url = mArticle.relative_url;

            Uri rootUri = Uri.parse("http://www.khanacademy.org");
            Uri uri = rootUri.buildUpon()
                    .path(relative_url)
                    .build();
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
}
