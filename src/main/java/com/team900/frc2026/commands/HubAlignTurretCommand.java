package com.team900.frc2026.commands;

import com.team900.lib.util.TurretAlignUtil;
import com.team900.frc2026.subsystems.turret.TurretSubsystem;
import com.team900.frc2026.subsystems.drive.DriveSubsystem;
import edu.wpi.first.wpilibj2.command.Command;

public class HubAlignTurretCommand extends Command {

    private final TurretSubsystem turret;
    private final DriveSubsystem drive;

    public HubAlignTurretCommand(DriveSubsystem drive, TurretSubsystem turret) {
        this.turret = turret;
        this.drive = drive;
        addRequirements(turret);
    }

    @Override
    public void execute() {
        TurretAlignUtil aligner = new TurretAlignUtil(drive.getPose());
        turret.setPositionDegrees(aligner.turretDegreesFromZero());
    }

    @Override
    public void end(boolean interrupted) {
        turret.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
