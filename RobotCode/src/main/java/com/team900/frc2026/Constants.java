package com.team900.frc2026;

import com.ctre.phoenix6.CANBus;
import com.team254.lib.subsystems.ServoMotorSubsystemConfig;
import com.team254.lib.subsystems.ServoMotorSubsystemWithFollowersConfig;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;

public class Constants {

    public static final class SensorConstants {
        // placeholder constant. Make sure to tune.

        public static final int kShooterBottomBannerSensorPort = 3;

   
        public static final double kShooterDebounceTime = 0.01;
        public static final double kAmpDebounceTime = 0.01;
        public static final double kFeederDebounceTime = 0.01;
        public static final double kIntakeDebounceTime = 0.01;
    }

    // control stuff
    public static final boolean kForceDriveGamepad = true;
    public static final int kGamepadAdditionalControllerPort = 1;
    public static final int kOperatorControllerPort = 2;
    public static final int kMainThrottleJoystickPort = 0;
    public static final int kMainTurnJoystickPort = 1;
    public static final double kDriveJoystickThreshold = 0.03;
    public static final double kJoystickThreshold = 0.1;
    public static final int kDriveGamepadPort = 0;

    public static final ServoMotorSubsystemConfig kShooterBottomConfig =
            new ServoMotorSubsystemConfig(1);

    public static final ServoMotorSubsystemWithFollowersConfig kShooterTopConfig = 
                new ServoMotorSubsystemWithFollowersConfig(2);



        
  
    public static final double kFieldLengthMeters = 8.07;

    public static final SimControllerType kSimControllerType = SimControllerType.XBOX;

    public enum SimControllerType {
        XBOX,
        DUAL_SENSE
    }


        public class ShooterConstants {
                //Tune Constants
                public static final double kBallReleaseHeight = Units.inchesToMeters(22.183);


                public static final double kStage2ShooterWheelDiameter = Units.inchesToMeters(3.0); // in
                public static final double kStage1ShooterWheelDiameter = Units.inchesToMeters(2.0); // in

                public static final double kBallLaunchVelMetersPerSecPerRotPerSec = 0.141;

                public static final double kShooterTopRPSCap = 3.0; // rot/s
        



                public static final double kTopRollerSpeedupFactor = 1.0;
                public static final double kBottomRollerSpeedupFactor = 1.0;

        }

    //TUNE ALL OF THESE
    public static final double kTurretToRobotCenterX = Units.inchesToMeters(2.3115);
    public static final double kTurretToRobotCenterY = 0;
    public static final Transform2d kTurretToRobotCenter = new Transform2d(
            new Translation2d(Constants.kTurretToRobotCenterX, Constants.kTurretToRobotCenterY),
            new Rotation2d());

 


    //Have to add in april tag stuff but I'm not doing that
    public static final Translation3d kRedSpeakerPose = new Translation3d(
            1,
            1, 2.045);
    public static final Translation3d kBlueSpeakerPose = new Translation3d(
            1,
            1, 2.045);



}
