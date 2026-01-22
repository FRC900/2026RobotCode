package com.team254.lib.subsystems;

public class ServoMotorSubsystemWithFollowersConfig extends ServoMotorSubsystemConfig {
    public static class FollowerConfig {
        public ServoMotorSubsystemConfig config;
        public boolean inverted = false;

        public FollowerConfig(int CANnum) {
            this.config = new ServoMotorSubsystemConfig(CANnum);

        }
    }


    public ServoMotorSubsystemWithFollowersConfig(int talonCANIDNum) {
        super(talonCANIDNum);
    }
    public FollowerConfig[] followers = new FollowerConfig[] {};
}
