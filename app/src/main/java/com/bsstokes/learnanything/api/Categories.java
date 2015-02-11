package com.bsstokes.learnanything.api;

import android.support.annotation.ColorRes;

import com.bsstokes.learnanything.R;

import java.util.HashMap;
import java.util.Map;

public class Categories {
    public static final String NEW_AND_NOTEWORTHY = "new-and-noteworthy";
    public static final String MATH = "math";
    public static final String SCIENCE = "science";
    public static final String ECONOMICS_AND_FINANCE = "economics-finance-domain";
    public static final String ARTS_AND_HUMANITIES = "humanities";
    public static final String TEST_PREP = "test-prep";
    public static final String PARTNER_CONTENT = "partner-content";
    public static final String COLLEGE_ADMISSIONS = "college-admissions";
    public static final String TALKS_AND_INTERVIEWS = "talks-and-interviews";
    public static final String COACH_RESOURCES = "coach-res";
    public static final String COMPUTING = "computing";

    @ColorRes
    public static final int DEFAULT_COLOR = R.color.ka_dark_gray;
    public static final Map<String, Integer> COLOR_MAP = new HashMap<>();

    static {
        COLOR_MAP.put(Categories.NEW_AND_NOTEWORTHY, R.color.ka_dark_gray);
        COLOR_MAP.put(Categories.MATH, R.color.ka_blue);
        COLOR_MAP.put(Categories.SCIENCE, R.color.ka_red);
        COLOR_MAP.put(Categories.ECONOMICS_AND_FINANCE, R.color.ka_gold);
        COLOR_MAP.put(Categories.ARTS_AND_HUMANITIES, R.color.ka_orange);
        COLOR_MAP.put(Categories.TEST_PREP, R.color.ka_dark_gray);
        COLOR_MAP.put(Categories.PARTNER_CONTENT, R.color.ka_teal);
        COLOR_MAP.put(Categories.COLLEGE_ADMISSIONS, R.color.ka_dark_gray);
        COLOR_MAP.put(Categories.TALKS_AND_INTERVIEWS, R.color.ka_dark_gray);
        COLOR_MAP.put(Categories.COACH_RESOURCES, R.color.ka_dark_gray);
        COLOR_MAP.put(Categories.COMPUTING, R.color.ka_dark_gray);
    }

    @ColorRes
    public static int getColorForCategory(String category) {
        Integer color = COLOR_MAP.get(category);
        return (null == color) ? DEFAULT_COLOR : color;
    }
}
