package com.team900.frc2026.subsystems.shooter;

import com.team900.frc2026.RobotState;
import com.team900.lib.subsystems.MotorIO;
import com.team900.lib.subsystems.MotorInputsAutoLogged;
import com.team900.lib.subsystems.ServoMotorSubsystemWithFollowers;
import com.team900.lib.subsystems.ServoMotorSubsystemWithFollowersConfig;

public class ShooterSubsystem
        extends ServoMotorSubsystemWithFollowers<MotorInputsAutoLogged, MotorIO> {

    private final RobotState state = RobotState.getInstance();

    public ShooterSubsystem(
            ServoMotorSubsystemWithFollowersConfig leadConfig,
            MotorIO leadIo,
            MotorIO[] followerIo) {
        super(
                leadConfig,
                new MotorInputsAutoLogged(),
                leadIo,
                new MotorInputsAutoLogged[] {new MotorInputsAutoLogged()},
                followerIo);
    }

    @Override
    public void periodic() {
        super.periodic();
        // Update robot state
        state.setShooterRPS(inputs.velocityUnitsPerSecond);
    }
}
