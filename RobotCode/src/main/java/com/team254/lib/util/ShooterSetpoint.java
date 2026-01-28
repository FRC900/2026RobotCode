package com.team254.lib.util;

import java.util.Optional;
import java.util.function.Supplier;

import com.team900.frc2026.Constants;
import com.team900.frc2026.RobotState;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;

public class ShooterSetpoint {

    public static Optional<Double> overrideRPS = Optional.empty();

    private double shooterRPS;
    private double shooterTopRPS = 5.0;

    private boolean isValid;

    public ShooterSetpoint(
            double shooterRPS,
            boolean isValid) {
        this.shooterRPS = shooterRPS;
        this.isValid = isValid;
    }

    public ShooterSetpoint(
            double shooterRPS,
            double hoodFF) {
        this.shooterRPS = shooterRPS;
        this.isValid = true;
    }

    public static void clearOverrideRPS() {
        overrideRPS = Optional.empty();
    }

    public static void setOverrideRPS(double rps) {
        overrideRPS = Optional.of(rps);
    }

    public boolean getIsValid() {
        return this.isValid;
    }

    // if (overrideRPS.isPresent()) {
    // launchSpeedRPS = overrideRPS.get();
    // }

    public double getShooterRPS() {
        return shooterRPS;
    }

    public double getTopShooterRPS() {
        return shooterTopRPS;
    }

   



    
   private static ShooterSetpoint fromSpeakerTarget(RobotState robotState) {

        double placeholderRPS = 5.0;

        double launchSpeedRPS = placeholderRPS;
 

        double launchSpeedMetersPerSec = Constants.ShooterConstants.kRingLaunchVelMetersPerSecPerRotPerSec *
                launchSpeedRPS;


        return makeSetpoint(robotState,
                launchSpeedMetersPerSec);
    }

    
    private static ShooterSetpoint makeSetpoint(RobotState robotState, double launchSpeedMetersPerSec) {
 

        boolean validSetpont = true;
        double shooterRPS = launchSpeedMetersPerSec / Constants.ShooterConstants.kRingLaunchVelMetersPerSecPerRotPerSec;
        if (shooterRPS > Constants.ShooterConstants.kShooterTopRPSCap) {
            shooterRPS = Constants.ShooterConstants.kShooterTopRPSCap;
            validSetpont = false;
        }

        return new ShooterSetpoint(shooterRPS,validSetpont);
    }




    public static Supplier<ShooterSetpoint> speakerSetpointSupplier(
            RobotState robotState) {
        return Util.memoizeByIteration(robotState.getIterationSupplier(),
                () -> fromSpeakerTarget(robotState));
    }
}
