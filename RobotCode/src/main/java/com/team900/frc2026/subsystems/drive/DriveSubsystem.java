package com.team900.frc2026.subsystems.drive;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.ctre.phoenix6.swerve.SwerveRequest.ApplyRobotSpeeds;
import com.team254.lib.time.RobotTime;
import com.team900.frc2026.Constants;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.Supplier;
import org.littletonrobotics.junction.Logger;

/** Swerve drive subsystem */
public class DriveSubsystem extends SubsystemBase {
    DriveIO io;
    DriveIOInputsAutoLogged inputs = new DriveIOInputsAutoLogged();
    DriveViz telemetry = new DriveViz(Constants.DriveConstants.kDriveMaxSpeed);

    private final ApplyRobotSpeeds stopRequest =
            new ApplyRobotSpeeds().withDriveRequestType(DriveRequestType.OpenLoopVoltage);

    public DriveSubsystem(DriveIO io) {
        this.io = io;
    }

    @Override
    public void periodic() {
        double timestamp = RobotTime.getTimestampSeconds();
        io.readInputs(inputs);
        telemetry.telemeterize(inputs);
        Logger.processInputs("DriveInputs", inputs);
        io.logModules(inputs);

        if (DriverStation.isDisabled()) {
            configureStandardDevsForDisabled();
        } else {
            configureStandardDevsForEnabled();
        }

        Logger.recordOutput(
                "Drive/latencyPeriodicSec", RobotTime.getTimestampSeconds() - timestamp);
        Logger.recordOutput(
                "Drive/currentCommand",
                (getCurrentCommand() == null) ? "Default" : getCurrentCommand().getName());
    }

    public void setControl(SwerveRequest request) {
        io.setControl(request);
    }

    public Command applyRequest(Supplier<SwerveRequest> requestSupplier) {
        return io.applyRequest(requestSupplier, this).withName("Swerve drive request");
    }

    public void resetOdometry(Pose2d pose) {
        io.resetOdometry(pose);
    }

    public Pose2d getPose() {
        return inputs.Pose;
    }

    public void stop() {
        setControl(stopRequest);
    }

    @Override
    public void simulationPeriodic() {
        io.simulationPeriodic();
    }

    // --- Standard Deviation Configuration ---

    public void setStateStdDevs(double xStd, double yStd, double rotStd) {
        io.setStateStdDevs(xStd, yStd, rotStd);
    }

    private void configureStandardDevsForDisabled() {
        setStateStdDevs(
                Constants.DriveConstants.kDisabledDriveXStdDev,
                Constants.DriveConstants.kDisabledDriveYStdDev,
                Constants.DriveConstants.kDisabledDriveRotStdDev);
    }

    private void configureStandardDevsForEnabled() {
        setStateStdDevs(
                Constants.DriveConstants.kEnabledDriveXStdDev,
                Constants.DriveConstants.kEnabledDriveYStdDev,
                Constants.DriveConstants.kEnabledDriveRotStdDev);
    }
}
