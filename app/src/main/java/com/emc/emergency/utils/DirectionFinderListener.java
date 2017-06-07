package com.emc.emergency.utils;



import com.emc.emergency.model.Route;

import java.util.List;


public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
