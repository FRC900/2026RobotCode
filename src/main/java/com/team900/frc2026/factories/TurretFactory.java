// package com.team900.frc2026.factories;

// import com.team900.frc2026.RobotContainer;
// import com.team900.frc2026.subsystems.turret.TurretSubsystem;
// import com.team900.lib.util.ShooterSetpoint;
// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.Commands;
// import java.util.function.Supplier;

// public class TurretFactory {
//     public static Command aimTurretToPose(
//             RobotContainer container, Supplier<ShooterSetpoint> setpointSupplier) {
//         TurretSubsystem turret = container.getTurretSubsystem();

//         return Commands.run(
//                         () ->
//                                 turret.setPositionRadians(
//                                         setpointSupplier.get().getTurretRadiansFromCenter(),
//                                         setpointSupplier.get().getTurretFF()),
//                         turret)
//                 .withName("Align Turret to Pose");
//     }
// }
