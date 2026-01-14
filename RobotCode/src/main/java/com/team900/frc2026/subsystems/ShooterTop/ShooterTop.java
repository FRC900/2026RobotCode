package com.team900.frc2026.subsystems.ShooterTop;

import com.team254.lib.subsystems.MotorIO;
import com.team254.lib.subsystems.MotorInputs;
import com.team254.lib.subsystems.ServoMotorSubsystemWithFollowers;
import com.team254.lib.subsystems.ServoMotorSubsystemWithFollowersConfig;

import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class ShooterTop extends ServoMotorSubsystemWithFollowers<MotorInputs, MotorIO> {

    private final RobotState state;

    // private ShooterTopInputsAutoLogged InputsSensors = new ShooterTopInputsAutoLogged();
    private ShooterTopSensorIO ioSensors;



    public ShooterTop(ServoMotorSubsystemWithFollowersConfig leadConfig, 
    MotorIO leadIO, 
    MotorIO[] FollowerIO, 
    final ShooterTopSensorIO sensorIO, 
    RobotState state) {

        super(leadConfig, 
        new MotorInputs(), 
        leadIO, 
        new MotorInputs[] {new MotorInputs()}, 
        FollowerIO);

        this.state = state;
        this.ioSensors = sensorIO;
                
        setCurrentPositionAsZero();

        setDefaultCommand(
        motionMagicSetpointCommand(this::getPositionSetpointUnits)
                .withName(getName() + " Default Command Neutral")
                .ignoringDisable(true));
    }

    @Override
    public void periodic() {
        super.periodic();
        
      //  ioSensors.readInputs(InputsSensors);

    }
}
