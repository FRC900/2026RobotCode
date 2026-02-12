package com.team900.frc2026.subsystems.Intake;

public class IntakeConstants {
    //Check all values; idk any ID or the current limits
    //Also do we need more CANranges for the slapdown arm Talons?
    //Also if the slapdown intake is only one side do we need all 4 CANranges?
    public static final int intakeTalonCanID = 21;
    public static final int lRollerTalonCanID = 22;
    public static final int rRollerTalonCanID = 23;
    public static final int lMotorTalonCanID = 22;
    public static final int rMotorTalonCanID = 23;
    public static final int fCANrangeCanID = 24;
    public static final int lCANrangeCanID = 25;
    public static final int rCANrangeCanID = 26;
    public static final int bCANrangeCanID = 27;
    public static final String canBUS = "rio";

    public static final double intakeStatorCurrentLimit = 200;
    public static final double intakeSupplyCurrentLimit = 40;

    public static final double rollerStatorCurrentLimit = 200;
    public static final double rollerSupplyCurrentLimit = 40;

    //Do we need this for the slapdown arms?
    public static final double armStatorCurrentLimit = 200;
    public static final double armSupplyCurrentLimit = 40;
}
