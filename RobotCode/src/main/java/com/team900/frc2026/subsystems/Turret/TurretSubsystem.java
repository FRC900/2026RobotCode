package com.team900.frc2026.subsystems.Turret;

import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TurretSubsystem extends SubsystemBase {
    private final TurretIO io;
    private RobotState robotState;
    private double turretPositionSetpointRadiansFromCenter = 0.0;

    public TurretSubsystem(final TurretIO io, RobotState robotState) {
        this.io = io;
        this.robotState = robotState;
    }

    @Override
    public void periodic() {
        
    }
}
