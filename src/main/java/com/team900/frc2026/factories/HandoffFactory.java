// package com.team900.frc2026.factories;

// import com.team900.frc2026.RobotContainer;
// import com.team900.frc2026.subsystems.handoff.HandoffConstants;
// import edu.wpi.first.wpilibj2.command.Command;

// public class HandoffFactory {

//      static   RobotContainer container = RobotContainer.getInstance();

//     public static Command runHandoff() {
//         return container
//                 .getHandoffSubsystem()
//                 .dutyCycleCommand(() -> HandoffConstants.kHandoffDutyCycle);
//     }

//     public static Command exhaustHandoff() {
//         return container
//                 .getHandoffSubsystem()
//                 .dutyCycleCommand(() -> HandoffConstants.kHandoffDutyCycleExhaust);
//     }
// }
