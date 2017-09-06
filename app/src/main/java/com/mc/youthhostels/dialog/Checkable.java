package com.mc.youthhostels.dialog;

public interface Checkable {
    String getName();
    String getDescription();
    boolean isChecked();
    void actionOnSetChecked();
}
