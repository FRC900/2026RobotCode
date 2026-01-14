import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.team900.lib.CANDeviceId;
import com.team900.lib.subsystems.ServoMotorSubsystemConfig;

public class IntakeConstants {
    public static final CANDeviceId intakeTalon = new CANDeviceId();

    public static final boolean extended = false;
    public static final double reduction = 1.0;
    public static final double rotorToMechanismRatio = 1.0;
    public static final double talonSpeed;
    //Do I need this?

    public static final int kIntakeSensorPort = 1;
    public static final ServoMotorSubsystemConfig config = new ServoMotorSubsystemConfig();

    static{
        config.fxConfig.Feedback.RotorToMechanismRatio = rotorToMechanismRatio;

        config.fxConfig.TorqueCurrent.PeakForwardTorqueCurrent = 40;
        config.fxConfig.TorqueCurrent.PeakReverseTorqueCurrent = -40;

        config.fxConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        config.fxConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;

        config.fxConfig.CurrentLimits.StatorCurrentLimit = 60;
        config.fxConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        config.fxConfig.CurrentLimits.SupplyCurrentLimit = 40;
        config.fxConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        config.fxConfig.CurrentLimits.SupplyCurrentLowerLimit = 40;
        config.fxConfig.CurrentLimits.SupplyCurrentLowerTime = 1;

        config.name = "Intake";

        config.kMinPositionUnits = 0;
        config.kMaxPositionUnits = 0;
        config.momentOfInertia = 1;

        config.talonCANID = intakeTalon;
        config.unitToRotorRatio = reduction;
    }
}
