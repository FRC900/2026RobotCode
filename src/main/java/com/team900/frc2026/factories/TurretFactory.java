// package com.team900.frc2026.factories;

// import com.team900.frc2026.RobotContainer;
// import com.team900.frc2026.subsystems.turret.TurretSubsystem;
// // import com.team900.lib.util.ShooterSetpoint;
// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.Commands;
// import java.util.function.Supplier;

// public class TurretFactory {

//     private static RobotContainer getContainer() {
//         return RobotContainer.getInstance();
//     }

//     // Continuously aims the turret in radians
//     // public static Command aimTurretToPose(Supplier<ShooterSetpoint> setpointSupplier) {
//     //     TurretSubsystem turret = getContainer().getTurretSubsystem();
//     //     return Commands.run(
//     //                     () ->
//     //                             turret.setPositionRadians(
//     //                                     setpointSupplier.get().getTurretRadiansFromCenter(),
//     //                                     setpointSupplier.get().getTurretFF()),
//     //                     turret)
//     //             .withName("Aim Turret to Pose (rad)");
//     // }

//     // Goes to a fixed position in radians, then finishes
//     public static Command setPositionRadians(double radians) {
//         TurretSubsystem turret = getContainer().getTurretSubsystem();
//         return Commands.run(() -> turret.setPositionRadians(radians), turret)
//                 .until(turret::atSetpoint)
//                 .withName("Turret Set Position (rad)");
//     }

//     // Holds the turret at a fixed position in radians until it's interrupted
//     public static Command holdPositionRadians(double radians) {
//         TurretSubsystem turret = getContainer().getTurretSubsystem();
//         return Commands.run(() -> turret.setPositionRadians(radians), turret)
//                 .withName("Turret Hold Position (rad)");
//     }

//     public static Command moveTurret(double dutyCycle) {
//         TurretSubsystem turret = getContainer().getTurretSubsystem();
//         return Commands.run(() -> turret.setOpenLoop(dutyCycle), turret).withName("Move Turret");
//     }

//     public static Command stop() {
//         TurretSubsystem turret = getContainer().getTurretSubsystem();
//         return Commands.runOnce(turret::stop, turret).withName("Turret Stop");
//     }
// }
