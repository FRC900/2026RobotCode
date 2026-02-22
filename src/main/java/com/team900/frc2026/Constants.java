package com.team900.frc2026;

import java.util.Arrays;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.wpilibj.RobotBase;

public class Constants {

    public static final Mode simMode = Mode.SIM;
    public static final Mode currentMode = RobotBase.isReal() ? Mode.REAL : simMode;
    public static final SimControllerType kSimControllerType = SimControllerType.XBOX;

      public static final boolean tuningMode = false;


    public static final double kRealDt = 0.02;

    public enum SimControllerType {
        XBOX,
        DUAL_SENSE
    }

    public enum Mode {
        /** Running on a real robot. */
        REAL,

        /** Running a physics simulator. */
        SIM,

        /** Replaying from a log file. */
        REPLAY
    }


  public static boolean disableHAL = false;
    
  public static void disableHAL() {
    disableHAL = true;
  }
    
    // TODO: temporary code to be changed to reflect rebuilt map
     public static final AprilTagFieldLayout kAprilTagLayout =
            AprilTagFieldLayout.loadField(AprilTagFields.k2025ReefscapeWelded);
    public static final int[] kAllowedTagIDs = {17, 18, 19, 20, 21, 22, 6, 7, 8, 9, 10, 11};
    public static final AprilTagFieldLayout kAprilTagLayoutReefsOnly =
            new AprilTagFieldLayout(
                    kAprilTagLayout.getTags().stream()
                            .filter(
                                    tag ->
                                            Arrays.stream(kAllowedTagIDs)
                                                    .anyMatch(element -> element == tag.ID))
                            .toList(),
                    kAprilTagLayout.getFieldLength(),
                    kAprilTagLayout.getFieldWidth());

    public static final double kFieldWidthMeters = kAprilTagLayout.getFieldWidth();
    public static final double kFieldLengthMeters = kAprilTagLayout.getFieldLength();

    
  public record Gains(
      double kP, double kI, double kD, double ffkS, double ffkV, double ffkA, double ffkG) {}
}
