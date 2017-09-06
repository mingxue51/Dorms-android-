package com.mc.youthhostels.entity;


import com.mc.youthhostels.dialog.Checkable;

public class TypeCheckable implements Checkable {
    private boolean checked;
    private String name;

    public TypeCheckable(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void actionOnSetChecked() {
        checked = !checked;
    }
}
