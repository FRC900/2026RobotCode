package com.team900.frc2026.subsystems.ShooterTop;

import com.team254.lib.subsystems.*;
import com.team254.lib.subsystems.MotorIO;
import com.team254.lib.subsystems.MotorInputsAutoLogged;
import com.team254.lib.subsystems.ServoMotorSubsystemWithFollowers;
import com.team254.lib.subsystems.ServoMotorSubsystemWithFollowersConfig;
import edu.wpi.first.wpilibj.RobotState;

public class ShooterTop extends ServoMotorSubsystemWithFollowers<MotorInputsAutoLogged, MotorIO> {

    private final RobotState state;

    private ShooterTopSensorIO.ShooterTopSensorInputs inputsSensors =
            new ShooterTopSensorIO.ShooterTopSensorInputs();
    private ShooterTopSensorIO ioSensors;

    public ShooterTop(
            ServoMotorSubsystemWithFollowersConfig leadConfig,
            MotorIO leadIO,
            MotorIO[] FollowerIO,
            final ShooterTopSensorIO sensorIO,
            RobotState state) {

        super(
                leadConfig,
                new MotorInputsAutoLogged(),
                leadIO,
                new MotorInputsAutoLogged[] {new MotorInputsAutoLogged()},
                FollowerIO);

        this.state = state;
        this.ioSensors = sensorIO;

        setCurrentPositionAsZero();

        setDefaultCommand(
                motionMagicSetpointCommand(this::getPositionSetpointUnits)
                        .withName(getName() + " Default Command Neutral")
                        .ignoringDisable(true));
    }

    @Override
    public void periodic() {
        super.periodic();

        ioSensors.readInputs(inputsSensors);

        // Logger.processInputs("ShooterTop", inputsSensors);

    }
}
