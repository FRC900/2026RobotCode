// package com.team900.frc2026.factories;

// import com.team900.frc2026.RobotContainer;
// import com.team900.lib.util.ShooterSetpoint;
// import edu.wpi.first.wpilibj2.command.Command;
// import java.util.function.Supplier;

// public class HoodFactory {

//     static RobotContainer container = RobotContainer.getInstance();

//     public static Command aimHoodToPose(Supplier<ShooterSetpoint> setpointSupplier) {
//         var hood = container.getHoodSubsystem();
//         return hood.positionSetpointCommand(
//                         () -> setpointSupplier.get().getHoodRadians(),
//                         () -> setpointSupplier.get().getHoodFF())
//                 .withName("Align Hood To Pose");
//     }
// }
