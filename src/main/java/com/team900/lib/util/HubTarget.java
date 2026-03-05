package com.team900.lib.util;
import com.team900.frc2026.Constants;
import com.team900.frc2026.RobotState;

import edu.wpi.first.math.geometry.Translation3d;

public class HubTarget {
    
    
    public static Translation3d generate(RobotState robotstate){

        var HubPose = robotstate.isRedAlliance() ? Constants.kRedHubCenterPose : Constants.kBlueHubCenterPose;

        return HubPose;
    }


}
