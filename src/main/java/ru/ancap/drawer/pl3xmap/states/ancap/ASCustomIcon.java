package ru.ancap.drawer.pl3xmap.states.ancap;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ASCustomIcon {
    
    BLUE_FLAG("blue_flag"),
    GREEN_FLAG("green_flag"),
    KING("king"),
    SHIELD("shield"),
    TOWER("tower"),
    WARNING("warning"),
    FIRE("fire"),
    ;
    
    private final String key;
    
}