
public class IntakeSubsystem extends ServoMotorSubsystem <MotorInputsAutoLogged, MotorIO> 
        implements IStatus SignalLoop{
    private final RobotState state;
    
    public IntakeSubsystem(ServoMotorSubsystemConfig config, final MotorIO io, 
            final IntakeIO io) {
        super(config, new MotorInputsAutoLogged(), io)
    }

    @Override
    public void periodic() {
        double timestamp = RobotTime.getTimestampSeconds();
        io.updateInputs(inputs);
        Logger.processInputs("Intake", inputs);
        io.updateInputs(inputs);

        Logger.recordOutput("Intake/latencyPeriodicSec", RobotTime.getTimestampSeconds() - timestamp);
    }

    public void setTeleopDefaultCommand() {
        setDefaultCommand();
    }

    public void setIntake(int pos) {
        if(pos == 0){
            setExtended(false);
        } else {
            setExtended(true);
        }
    }

    public Command intake() {
        return run(() -> {
            wheelsIn(1.0);
            setIntake(1.0);
        }).withName("Intake intaking fuel");
    }

    public Command outtake() {
        return run(() -> {
            wheelsIn(-1.0);
            setIntake(1.0);
        }).withName("Intake spitting out fuel");
    }

    public Command retract() {
        return run(() -> {
            wheelsIn(0.0);
            setIntake(0.0);
        }).withName("Intake retracting");
    }

    public void wheelsIn(double speed) {
        intakeTalon.set(speed);
    }

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean Extended) {
        this.extended = Extended;
    }
}