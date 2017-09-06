package com.mc.youthhostels.entity.filters;

abstract public class FilterNew implements Filterable {
    private String name;
    private boolean activated;
    private boolean checked;

    public FilterNew(String name) {
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

    @Override
    public void apply() {
        activated = checked;
    }

    @Override
    public void sync() {
        checked = activated;
    }

    @Override
    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
