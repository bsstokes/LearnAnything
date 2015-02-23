package com.bsstokes.learnanything.api.models;

import java.util.Map;

public class Video {

    //    translated_youtube_id: "SQzjzStU1RQ",
//    relative_url: "/video/why-dividing-by-zero-is-undefined",
//    license_logo_url: "/images/license-logos/by-nc-sa.png",
//    has_questions: false,
//    keywords: "zero, undefined",
//    ka_url: "http://www.khanacademy.org/video/why-dividing-by-zero-is-undefined",
//    duration: 248,
    public String translated_title;
    public String translated_description_html;
    public String id;
//    description_html: "Thinking about why dividing by zero is left undefined",
//    title: "Why dividing by zero is undefined",
//    progress_key: "v871510490",
//    edit_slug: "edit/v/871510490",
//    author_names: [
//            "Sal Khan"
//            ],
//    license_full_name: "Creative Commons Attribution/Non-Commercial/Share-Alike",
//    license_url: "http://creativecommons.org/licenses/by-nc-sa/3.0",
//    deleted_mod_time: "2013-07-13T00:03:07Z",
//    description: "Thinking about why dividing by zero is left undefined",
//    extra_properties: null,
//    node_slug: "v/why-dividing-by-zero-is-undefined",
//    deleted: false,
//    license_name: "CC BY-NC-SA (KA default)",
//    backup_timestamp: "2013-09-23T18:42:23Z",
//    date_added: "2012-06-25T15:07:06Z",
    public Map<String, String> download_urls;
    //    download_urls: {
//        mp4: "http://fastly.kastatic.org/KA-youtube-converted/SQzjzStU1RQ.mp4/SQzjzStU1RQ.mp4",
//                png: "http://fastly.kastatic.org/KA-youtube-converted/SQzjzStU1RQ.mp4/SQzjzStU1RQ.png",
//                m3u8: "http://fastly.kastatic.org/KA-youtube-converted/SQzjzStU1RQ.m3u8/SQzjzStU1RQ.m3u8"
//    },
//    translated_youtube_lang: "en",
//    kind: "Video",
//    date_modified: "2015-01-10T10:39:08Z",
    public String url;
    //    clarifications_enabled: true,
//    ka_user_license: "cc-by-nc-sa",
//    global_id: "v871510490",
//    sha: "3c2e60f94a6dda0460d7de2d8859335ce7d1ed36",
//    translated_description: "Thinking about why dividing by zero is left undefined",
    public String image_url;
    //    youtube_id: "SQzjzStU1RQ",
//    content_kind: "Video",
    public String readable_id;

    @Override
    public String toString() {
        return super.toString() + " (Video)";
    }
}
