package com.mc.youthhostels.events;

import entity.Property.Search.Suggestion;

public class SuggestionSelectedEvent {
    private final Suggestion suggestion;

    public SuggestionSelectedEvent(Suggestion suggestion) {
        this.suggestion = suggestion;
    }

    public Suggestion getSuggestion() {
        return suggestion;
    }
}
