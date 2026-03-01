// package com.team900.frc2026.factories;

// import com.team900.frc2026.RobotContainer;
// import edu.wpi.first.wpilibj2.command.Command;
// import java.util.function.Supplier;

// public class HoodFactory {

//     RobotContainer container;

//     public static Command aimHoodToPose(
//             RobotContainer container, Supplier<Angle> setpointSupplier) {
//         var hood = container.getHood();
//         return hood.positionSetpointCommand(
//                         () -> setpointSupplier.get().getHoodRadians(),
//                         () -> setpointSupplier.get().getHoodFF())
//                 .withName("Align Hood To Pose");
//     }
// }
