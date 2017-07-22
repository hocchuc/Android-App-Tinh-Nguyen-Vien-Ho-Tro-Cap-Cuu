package com.emc.emergency.Helper.Utils;



import com.emc.emergency.Helper.Model.Route;

import java.util.List;


public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
