package com.team900.frc2026.subsystems.drive;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.swerve.SwerveDrivetrain;
import com.ctre.phoenix6.swerve.SwerveDrivetrainConstants;
import com.ctre.phoenix6.swerve.SwerveModuleConstants;
import com.ctre.phoenix6.swerve.SwerveRequest;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.LinearAcceleration;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.Subsystem;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.littletonrobotics.junction.Logger;

/**
 * Hardware implementation of DriveIO Using CTRE Phoenix 6 SwerveDrivetrain with Kraken X60 and
 * CANcoders. Runs odometry at 250 Hz.
 */
public class DriveIOHardware extends SwerveDrivetrain<TalonFX, TalonFX, CANcoder>
        implements DriveIO {

    AtomicReference<SwerveDriveState> telemetryCache_ = new AtomicReference<>();

    private final StatusSignal<AngularVelocity> angularPitchVelocity;
    private final StatusSignal<AngularVelocity> angularRollVelocity;
    private final StatusSignal<AngularVelocity> angularYawVelocity;
    private final StatusSignal<Angle> roll;
    private final StatusSignal<Angle> pitch;
    private final StatusSignal<LinearAcceleration> accelerationX;
    private final StatusSignal<LinearAcceleration> accelerationY;

    public DriveIOHardware(
            SwerveDrivetrainConstants driveTrainConstants,
            SwerveModuleConstants<?, ?, ?>... modules) {
        super(TalonFX::new, TalonFX::new, CANcoder::new, driveTrainConstants, 250.0, modules);

        // Register gyro signals from Pigeon2
        angularPitchVelocity = getPigeon2().getAngularVelocityYWorld();
        angularRollVelocity = getPigeon2().getAngularVelocityXWorld();
        angularYawVelocity = getPigeon2().getAngularVelocityZWorld();
        roll = getPigeon2().getRoll();
        pitch = getPigeon2().getPitch();
        accelerationX = getPigeon2().getAccelerationX();
        accelerationY = getPigeon2().getAccelerationY();

        BaseStatusSignal.setUpdateFrequencyForAll(250, angularYawVelocity);
        BaseStatusSignal.setUpdateFrequencyForAll(
                100,
                angularPitchVelocity,
                angularRollVelocity,
                roll,
                pitch,
                accelerationX,
                accelerationY);

        // Set odometry thread to highest priority
        this.getOdometryThread().setThreadPriority(99);

        // Cache telemetry for reading in periodic()
        registerTelemetry(telemetryConsumer_);
    }

    @Override
    public void resetOdometry(Pose2d pose) {
        super.resetPose(pose);
    }

    @Override
    public Command applyRequest(
            Supplier<SwerveRequest> requestSupplier, Subsystem subsystemRequired) {
        return Commands.run(() -> this.setControl(requestSupplier.get()), subsystemRequired);
    }

    @Override
    public void setStateStdDevs(double xStd, double yStd, double rotStd) {
        super.setStateStdDevs(VecBuilder.fill(xStd, yStd, rotStd));
    }

    Consumer<SwerveDriveState> telemetryConsumer_ =
            swerveDriveState -> {
                telemetryCache_.set(swerveDriveState.clone());
            };

    @Override
    public void setControl(SwerveRequest request) {
        super.setControl(request);
    }

    @Override
    public void readInputs(DriveIOInputs inputs) {
        if (telemetryCache_.get() == null) return;
        inputs.fromSwerveDriveState(telemetryCache_.get());
        var gyroRotation = inputs.Pose.getRotation();
        inputs.gyroAngle = gyroRotation.getDegrees();

        BaseStatusSignal.refreshAll(
                angularRollVelocity,
                angularPitchVelocity,
                angularYawVelocity,
                pitch,
                roll,
                accelerationX,
                accelerationY);

        double yawRadsPerS = Units.degreesToRadians(angularYawVelocity.getValueAsDouble());

        // Log gyro data
        Logger.recordOutput("Drive/gyroYawRateRadPerSec", yawRadsPerS);
        Logger.recordOutput("Drive/gyroPitchDeg", pitch.getValueAsDouble());
        Logger.recordOutput("Drive/gyroRollDeg", roll.getValueAsDouble());
    }

    @Override
    public void logModules(SwerveDriveState driveState) {
        final String[] moduleNames = {"Drive/FL", "Drive/FR", "Drive/BL", "Drive/BR"};
        if (driveState.ModuleStates == null) return;
        for (int i = 0; i < getModules().length; i++) {
            Logger.recordOutput(
                    moduleNames[i] + " Absolute Encoder Angle",
                    getModule(i).getEncoder().getAbsolutePosition().getValueAsDouble() * 360);
            Logger.recordOutput(
                    moduleNames[i] + " Steering Angle", driveState.ModuleStates[i].angle);
            Logger.recordOutput(
                    moduleNames[i] + " Target Steering Angle", driveState.ModuleTargets[i].angle);
            Logger.recordOutput(
                    moduleNames[i] + " Drive Velocity",
                    driveState.ModuleStates[i].speedMetersPerSecond);
            Logger.recordOutput(
                    moduleNames[i] + " Target Drive Velocity",
                    driveState.ModuleTargets[i].speedMetersPerSecond);
        }
    }
}
