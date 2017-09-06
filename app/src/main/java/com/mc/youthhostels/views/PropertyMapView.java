package com.mc.youthhostels.views;

import java.util.List;

import com.mc.youthhostels.LoadingView;
import routedrawer.model.Routes;

public interface PropertyMapView extends LoadingView {
    void updateInfoBoard(List<Routes> routes);
}
