package com.team900.frc2026.subsystems.Intake;

import java.util.Arrays;
import java.util.List;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.CANrangeConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.MotionMagicExpoVoltage;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.PositionTorqueCurrentFOC;
import com.ctre.phoenix6.controls.TorqueCurrentFOC;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.CANrange;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Temperature;
import edu.wpi.first.units.measure.Voltage;

public class IntakeIOReal implements IntakeIO {
    protected final TalonFX intakeTalon;
    protected final TalonFX rRollerTalon;
    protected final TalonFX lRollerTalon;
    protected final CANrange fCANrange;
    protected final CANrange rCANrange;
    protected final CANrange lCANrange;
    protected final CANrange bCANrange;

    private final StatusSignal<AngularVelocity> intakeVelocitySignal;
    private final StatusSignal<Voltage> intakeVoltsSignal;
    private final StatusSignal<Current> intakeCurrentStatorSignal;
    private final StatusSignal<Current> intakeCurrentSupplySignal;
    private final StatusSignal<Temperature> intakeTemperatureSignal;

    private final StatusSignal<AngularVelocity> rRollerVelocitySignal;
    private final StatusSignal<Voltage> rRollerVoltsSignal;
    private final StatusSignal<Current> rRollerCurrentStatorSignal;
    private final StatusSignal<Current> rRollerCurrentSupplySignal;
    private final StatusSignal<Temperature> rRollerTemperatureSignal;

    private final StatusSignal<AngularVelocity> lRollerVelocitySignal;
    private final StatusSignal<Voltage> lRollerVoltsSignal;
    private final StatusSignal<Current> lRollerCurrentStatorSignal;
    private final StatusSignal<Current> lRollerCurrentSupplySignal;
    private final StatusSignal<Temperature> lRollerTemperatureSignal;

    private final StatusSignal<Boolean> fCANrangeIsDectected;
    private final StatusSignal<Boolean> rCANrangeIsDectected;
    private final StatusSignal<Boolean> lCANrangeIsDectected;
    private final StatusSignal<Boolean> bCANrangeIsDectected;

    private final TalonFXConfiguration intakeConfig = new TalonFXConfiguration();
    private final TalonFXConfiguration rollerConfig = new TalonFXConfiguration();

    public IntakeIOReal(){
        intakeTalon = new TalonFX(IntakeConstants.intakeTalonCanID, IntakeConstants.canBUS);
        rRollerTalon = new TalonFX(IntakeConstants.rRollerTalonCanID, IntakeConstants.canBUS);
        lRollerTalon = new TalonFX(IntakeConstants.lRollerTalonCanID, IntakeConstants.canBUS);
        fCANrange = new CANrange(IntakeConstants.fCANrangeCanID, IntakeConstants.canBUS);
        rCANrange = new CANrange(IntakeConstants.rCANrangeCanID, IntakeConstants.canBUS);
        lCANrange = new CANrange(IntakeConstants.lCANrangeCanID, IntakeConstants.canBUS);
        bCANrange = new CANrange(IntakeConstants.bCANrangeCanID, IntakeConstants.canBUS);

        intakeConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;
        rollerConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;

        intakeConfig.CurrentLimits.StatorCurrentLimit = IntakeConstants.intakeStatorCurrentLimit;
        intakeConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        intakeConfig.CurrentLimits.SupplyCurrentLimit = IntakeConstants.intakeSupplyCurrentLimit;
        intakeConfig.CurrentLimits.SupplyCurrentLimitEnable = true;

        rollerConfig.CurrentLimits.StatorCurrentLimit = IntakeConstants.intakeStatorCurrentLimit;
        rollerConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        rollerConfig.CurrentLimits.SupplyCurrentLimit = IntakeConstants.intakeSupplyCurrentLimit;
        rollerConfig.CurrentLimits.SupplyCurrentLimitEnable = true;

        intakeVelocitySignal = intakeTalon.getRotorVelocity();
        intakeVoltsSignal = intakeTalon.getMotorVoltage();
        intakeCurrentStatorSignal = intakeTalon.getStatorCurrent();
        intakeCurrentSupplySignal = intakeTalon.getSupplyCurrent();
        intakeTemperatureSignal = intakeTalon.getDeviceTemp();

        rRollerVelocitySignal = rRollerTalon.getRotorVelocity();
        rRollerVoltsSignal = rRollerTalon.getMotorVoltage();
        rRollerCurrentStatorSignal = rRollerTalon.getStatorCurrent();
        rRollerCurrentSupplySignal = rRollerTalon.getSupplyCurrent();
        rRollerTemperatureSignal = rRollerTalon.getDeviceTemp();

        lRollerVelocitySignal = lRollerTalon.getRotorVelocity();
        lRollerVoltsSignal = lRollerTalon.getMotorVoltage();
        lRollerCurrentStatorSignal = lRollerTalon.getStatorCurrent();
        lRollerCurrentSupplySignal = lRollerTalon.getSupplyCurrent();
        lRollerTemperatureSignal = lRollerTalon.getDeviceTemp();

        fCANrangeIsDectected = fCANrange.getIsDetected();
        rCANrangeIsDectected = rCANrange.getIsDetected();
        lCANrangeIsDectected = lCANrange.getIsDetected();
        bCANrangeIsDectected = rCANrange.getIsDetected();
    }

    public void setIntake(int pos) {
        if(pos == 0){
            setExtended(false);
        } else {
            setExtended(true);
        }
    }

    public void runWheels(double speed) {
        intakeTalon.set(speed);
    }

    public void setExtended(boolean Extended) {
        this.extended = Extended;
    }

    public void retract(){
        intakeTalon.setControl(new NeutralOut());
    }

    public void extend(){

    }

    public void setLRollerDutyCycleOut(double output) {

    }

    public void setLRollerBrakeMode(boolean enabled) {

    }

    public void stopLRoller() {

    }

    public void setRRollerDutyCycleOut(double output) {

    }

    public void setRRollerBrakeMode(boolean enabled) {

    }

    public void stopRRoller() {

    }
}
