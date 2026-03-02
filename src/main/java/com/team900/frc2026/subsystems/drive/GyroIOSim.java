// Copyright (c) 2025 FRC 1533
// http://github.com/triplestrange
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package com.team900.frc2026.subsystems.drive;

import static edu.wpi.first.units.Units.RadiansPerSecond;

import com.team900.lib.util.CTREUtil;
import edu.wpi.first.math.util.Units;
import org.ironmaple.simulation.drivesims.GyroSimulation;

public class GyroIOSim implements GyroIO {
    private final GyroSimulation gyroSimulation;

    public GyroIOSim(GyroSimulation gyroSimulation) {
        this.gyroSimulation = gyroSimulation;
    }

    @Override
    public void readInputs(GyroIOInputs inputs) {
        inputs.connected = true;

        inputs.yawPosition = gyroSimulation.getGyroReading();
        inputs.yawVelocityRadPerSec =
                Units.degreesToRadians(
                        gyroSimulation.getMeasuredAngularVelocity().in(RadiansPerSecond));

        inputs.odometryTimestamps = CTREUtil.getSimulationOdometryTimeStamps();
        inputs.odometryYawPositions = gyroSimulation.getCachedGyroReadings();
    }
}
