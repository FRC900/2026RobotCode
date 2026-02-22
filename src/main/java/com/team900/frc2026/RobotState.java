package com.team900.frc2026;

public class RobotState {
    private static volatile RobotState instance;

    private RobotState()    {

    }

    public static RobotState getInstance()  {
        if (instance == null)   {
            synchronized (RobotState.class) {
                if (instance == null)   {
                    instance = new RobotState();
                }
            }
        }
        return instance;
    }

}
