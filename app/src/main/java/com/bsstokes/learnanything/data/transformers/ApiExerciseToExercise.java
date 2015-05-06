package com.bsstokes.learnanything.data.transformers;

import com.bsstokes.learnanything.data.Exercise;

import rx.functions.Func1;

public class ApiExerciseToExercise implements Func1<com.bsstokes.learnanything.api.models.Exercise, Exercise> {

    @Override
    public Exercise call(com.bsstokes.learnanything.api.models.Exercise apiExercise) {
        return Exercise.builder()
                .id(apiExercise.id)
                .url(apiExercise.ka_url)
                .description(apiExercise.translated_description)
                .title(apiExercise.translated_pretty_display_name)
                .imageUrl(apiExercise.image_url)
                .image256Url(apiExercise.image_url_256)
                .build();
    }
}
