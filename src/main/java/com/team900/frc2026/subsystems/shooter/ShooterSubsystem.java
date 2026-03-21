package com.team900.frc2026.subsystems.shooter;

import com.team900.frc2026.RobotState;
import com.team900.frc2026.subsystems.hood.HoodConstants;
import com.team900.frc2026.subsystems.shooter.ShooterConstants;
import com.team900.lib.subsystems.MotorIO;
import com.team900.lib.subsystems.MotorInputsAutoLogged;
import com.team900.lib.subsystems.ServoMotorSubsystemWithFollowers;
import com.team900.lib.subsystems.ServoMotorSubsystemWithFollowersConfig;
import com.team900.lib.util.AllianceFlipUtil;
import com.team900.lib.util.FieldConstants;
import com.team900.lib.util.ShooterSetpoint;
import com.team900.lib.util.TurretAlignUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import org.littletonrobotics.junction.Logger;

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
        Pose2d robotPose = state.getLatestFieldToRobot().getValue();
        TurretAlignUtil aligner = new TurretAlignUtil(robotPose);
        Pose2d turretPose = aligner.getTurretPositionFromRobotPose();

        double turretX = turretPose.getX();
        double turretY = turretPose.getY();

        Translation2d hub =
                AllianceFlipUtil.apply(FieldConstants.Hub.topCenterPoint).toTranslation2d();

        // Distance from turret to hub
        double distanceToTarget = Math.hypot(hub.getX() - turretX, hub.getY() - turretY);

        state.setShooterRPS(inputs.velocityUnitsPerSecond);
        Logger.recordOutput(getName() + "/DistanceFromHub", distanceToTarget);

        // if (ShooterConstants.kShootingRPS.hasChanged(hashCode()){

        // }

        double hoodSetpoint =
                ShooterSetpoint.getPhi(distanceToTarget, 0.0);

        Logger.recordOutput(getName() + "/HoodSetpoint", hoodSetpoint / (2 * Math.PI));
    }
}
