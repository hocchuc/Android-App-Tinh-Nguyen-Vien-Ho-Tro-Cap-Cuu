package com.emc.emergency.task;

import com.emc.emergency.model.Accident;
import com.emc.emergency.model.User;

import java.util.ArrayList;

/**
 * Created by Admin on 13/7/2017.
 */

public interface ReturnDataAllAccident {
    void handleReturnDataAllAccident (ArrayList<Accident> arrAccident);
}