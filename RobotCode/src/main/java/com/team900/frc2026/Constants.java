package com.team900.frc2026;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.ctre.phoenix6.configs.ClosedLoopRampsConfigs;
import com.ctre.phoenix6.configs.OpenLoopRampsConfigs;
import com.team254.lib.drivers.CANDeviceId;

import edu.wpi.first.math.util.Units;

public class Constants {
    public static final String kCanBusCanivore = "canivore";
    public static final String kPracticeBotMacAddress = "00:80:2F:33:D1:4B";
    public static boolean kIsPracticeBot = hasMacAddress(kPracticeBotMacAddress);

    public static final ClosedLoopRampsConfigs makeDefaultClosedLoopRampConfig() {
        return new ClosedLoopRampsConfigs()
                .withDutyCycleClosedLoopRampPeriod(0.02)
                .withTorqueClosedLoopRampPeriod(0.02)
                .withVoltageClosedLoopRampPeriod(0.02);
    }

    public static final OpenLoopRampsConfigs makeDefaultOpenLoopRampConfig() {
        return new OpenLoopRampsConfigs()
                .withDutyCycleOpenLoopRampPeriod(0.02)
                .withTorqueOpenLoopRampPeriod(0.02)
                .withVoltageOpenLoopRampPeriod(0.02);
    }

    public static final class TurretConstants {
        public static final double kTurretGearRatio = (14. / 52.) * (10. / 125.);
        public static final CANDeviceId kTurretTalonCanID = new CANDeviceId(21, kCanBusCanivore);
        public static final CANDeviceId kTurret1To1CANCoder = new CANDeviceId(13, kCanBusCanivore);
        public static final CANDeviceId kTurret3To1CANCoder = new CANDeviceId(14, kCanBusCanivore);
        public static final double k3To1TurretCancoderOffset = kIsPracticeBot ? -0.254150 : -0.057617;
        public static final double k1To1TurretCancoderOffset = kIsPracticeBot ? 0.270996 : 0.482178;
        public static final double kTurretMinPositionRadians = -2. * Math.PI;
        public static final double kTurretMaxPositionRadians = 2. * Math.PI;

        public static final double kTurretEpsilon = Units.degreesToRadians(2.0);
        public static final double kTurretShootingEpsilon = Units.degreesToRadians(5.0);
    
        public static final double toleranceRad = 0.1;
    }

    public static boolean hasMacAddress(final String mac_address) {
        try {
            Enumeration<NetworkInterface> nwInterface = NetworkInterface.getNetworkInterfaces();
            while (nwInterface.hasMoreElements()) {
                NetworkInterface nis = nwInterface.nextElement();
                if (nis == null) {
                    continue;
                }
                StringBuilder device_mac_sb = new StringBuilder();
                System.out.println("hasMacAddress: NIS: " + nis.getDisplayName());
                byte[] mac = nis.getHardwareAddress();
                if (mac != null) {
                    for (int i = 0; i < mac.length; i++) {
                        device_mac_sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
                    }
                    String device_mac = device_mac_sb.toString();
                    System.out.println("hasMacAddress: NIS " + nis.getDisplayName() + " device_mac: " + device_mac);
                    if (mac_address.equals(device_mac)) {
                        System.out.println("hasMacAddress: ** Mac address match! " + device_mac);
                        return true;
                    }
                } else {
                    System.out.println("hasMacAddress: Address doesn't exist or is not accessible");
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }
        return false;
    }
}
