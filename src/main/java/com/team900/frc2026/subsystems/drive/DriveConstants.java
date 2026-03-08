// Copyright (c) 2025 FRC 1533
// http://github.com/triplestrange
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package com.team900.frc2026.subsystems.drive;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.CANBus;
import com.pathplanner.lib.config.ModuleConfig;
import com.pathplanner.lib.config.RobotConfig;
import com.team900.frc2026.Constants.Gains;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import org.ironmaple.simulation.drivesims.COTS;
import org.ironmaple.simulation.drivesims.configs.DriveTrainSimulationConfig;
import org.ironmaple.simulation.drivesims.configs.SwerveModuleSimulationConfig;

public class DriveConstants {

    // CompTunerConstants doesn't include these constants, so they are declared locally
    static final double ODOMETRY_FREQUENCY =
            new CANBus(CompTunerConstants.DrivetrainConstants.CANBusName).isNetworkFD()
                    ? 250.0
                    : 100.0;

    public static final double DRIVE_BASE_RADIUS =
            Math.max(
                    Math.max(
                            Math.hypot(
                                    CompTunerConstants.FrontLeft.LocationX,
                                    CompTunerConstants.FrontLeft.LocationY),
                            Math.hypot(
                                    CompTunerConstants.FrontRight.LocationX,
                                    CompTunerConstants.FrontRight.LocationY)),
                    Math.max(
                            Math.hypot(
                                    CompTunerConstants.BackLeft.LocationX,
                                    CompTunerConstants.BackLeft.LocationY),
                            Math.hypot(
                                    CompTunerConstants.BackRight.LocationX,
                                    CompTunerConstants.BackRight.LocationY)));

    // PathPlanner config constants
    public static final double ROBOT_MASS_KG = Units.lbsToKilograms(140);
    public static final double ROBOT_MOI = 5.645;
    public static final double WHEEL_COF = 1;
    public static final double MAX_STEER_VEL_RAD_PER_SEC = 2 * Math.PI;
    public static final RobotConfig PP_CONFIG =
            new RobotConfig(
                    ROBOT_MASS_KG,
                    ROBOT_MOI,
                    new ModuleConfig(
                            CompTunerConstants.FrontLeft.WheelRadius,
                            CompTunerConstants.kSpeedAt12Volts.in(MetersPerSecond),
                            WHEEL_COF,
                            DCMotor.getKrakenX60Foc(1)
                                    .withReduction(
                                            CompTunerConstants.FrontLeft.DriveMotorGearRatio),
                            CompTunerConstants.FrontLeft.SlipCurrent,
                            1),
                    getModuleTranslations());

    public static final DriveTrainSimulationConfig mapleSimConfig =
            DriveTrainSimulationConfig.Default()
                    .withRobotMass(Kilograms.of(ROBOT_MASS_KG))
                    .withCustomModuleTranslations(getModuleTranslations())
                    .withGyro(COTS.ofPigeon2())
                    .withSwerveModule(
                            new SwerveModuleSimulationConfig(
                                    DCMotor.getKrakenX60Foc(1),
                                    DCMotor.getKrakenX60Foc(1),
                                    SimTunerConstants.FrontLeft.DriveMotorGearRatio,
                                    SimTunerConstants.FrontLeft.SteerMotorGearRatio,
                                    Volts.of(SimTunerConstants.FrontLeft.DriveFrictionVoltage),
                                    Volts.of(SimTunerConstants.FrontLeft.SteerFrictionVoltage),
                                    Meters.of(SimTunerConstants.FrontLeft.WheelRadius),
                                    KilogramSquareMeters.of(
                                            SimTunerConstants.FrontLeft.SteerInertia),
                                    WHEEL_COF));

    /** Returns an array of module translations. */
    public static Translation2d[] getModuleTranslations() {
        return new Translation2d[] {
            new Translation2d(
                    CompTunerConstants.FrontLeft.LocationX, CompTunerConstants.FrontLeft.LocationY),
            new Translation2d(
                    CompTunerConstants.FrontRight.LocationX,
                    CompTunerConstants.FrontRight.LocationY),
            new Translation2d(
                    CompTunerConstants.BackLeft.LocationX, CompTunerConstants.BackLeft.LocationY),
            new Translation2d(
                    CompTunerConstants.BackRight.LocationX, CompTunerConstants.BackRight.LocationY)
        };
    }

    public static final Gains kHeadingController = new Gains(0, 0, 0, 0, 0, 0, 0);

    public static final double kDriveMaxSpeed =
            CompTunerConstants.kSpeedAt12Volts.in(MetersPerSecond) * 0.8;
    public static final double kMaxAccelerationMetersPerSecondSquared = 10.0;
    public static final double kMaxXAccelerationMetersPerSecondSquared = 10.0;
    public static final double kMaxYAccelerationMetersPerSecondSquared = 10.0;
    public static final double kDriveMaxAngularRate = 8.2;
    public static final double kMaxAngularSpeedRadiansPerSecondSquared = 20.0;
    public static final double kHeadingControllerP = 5.0;
    public static final double kHeadingControllerI = 0;
    public static final double kHeadingControllerD = 0;
}
