package com.team900.frc2026.subsystems.coprocessor;

import com.team900.frc2026.subsystems.coprocessor.messages.apriltag_msgs.RawFiducialArrayStamped;
import com.team900.frc2026.subsystems.drive.DriveSubsystem;
import com.team900.lib.util.VirtualSubsystem;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team88.ros.bridge.BridgePublisher;
import frc.team88.ros.bridge.BridgeSubscriber;
import frc.team88.ros.bridge.ROSNetworkTablesBridge;
import frc.team88.ros.conversions.ROSConversions;
import frc.team88.ros.messages.geometry_msgs.Pose;
import frc.team88.ros.messages.geometry_msgs.Pose2D;
import frc.team88.ros.messages.geometry_msgs.TransformStamped;
import frc.team88.ros.messages.std_msgs.RosFloat64;
import frc.team88.ros.messages.tf2_msgs.TFMessage;
import java.util.Optional;

public class CoprocessorSubsystem extends SubsystemBase {
    private final DriveSubsystem m_drive;
    private final ROSNetworkTablesBridge m_ros_interface;

    private final BridgePublisher<RosFloat64> m_pingReturnPub;
    private final BridgePublisher<Pose> m_posePub;

    private final BridgeSubscriber<RosFloat64> m_pingSendSub;
    private final BridgeSubscriber<RawFiducialArrayStamped> m_vid0TagsSub;
    private final BridgeSubscriber<RawFiducialArrayStamped> m_vid1TagsSub;
    // private final BridgeSubscriber<RawFiducialArrayStamped> m_vid2TagsSub;
    private final BridgeSubscriber<TFMessage> m_poseSub;

    public CoprocessorSubsystem(DriveSubsystem drive) {
        long updateDelay = 20;
        NetworkTableInstance instance = NetworkTableInstance.getDefault();
        instance.startServer();

        m_drive = drive;

        m_ros_interface = new ROSNetworkTablesBridge(instance.getTable(""), updateDelay);

        m_pingReturnPub = new BridgePublisher<>(m_ros_interface, "/ping_return");
        m_posePub = new BridgePublisher<>(m_ros_interface, "/wpi_pose");
        
        m_pingSendSub = new BridgeSubscriber<>(m_ros_interface, "/ping_send", RosFloat64.class);
        m_vid0TagsSub =
                new BridgeSubscriber<>(
                        m_ros_interface,
                        "/ov2311_10_9_0_9_video1/raw_fiducials",
                        RawFiducialArrayStamped.class);
        m_vid1TagsSub =
                new BridgeSubscriber<>(
                        m_ros_interface,
                        "/ov2311_10_9_0_9_video1/raw_fiducials",
                        RawFiducialArrayStamped.class);
        // m_vid2TagsSub =
        //         new BridgeSubscriber<>(
        //                 m_ros_interface,
        //                 "/ov2311_10_9_0_9_video2/raw_fiducials",
        //                 RawFiducialArrayStamped.class);
        m_poseSub =
                new BridgeSubscriber<>(
                        m_ros_interface, "/tagslam/odom/body_frc_robot", TFMessage.class);
    }
    ;

    private void checkPing() {
        Optional<RosFloat64> ping;
        if ((ping = m_pingSendSub.receive()).isPresent()) {
            m_pingReturnPub.send(ping.get());
        }
    }

    private void checkFiducialDetections() {
        Optional<RawFiducialArrayStamped> rawFiducialArray;
        // just log it for rn
        if ((rawFiducialArray = m_vid0TagsSub.receive()).isPresent()) {
            System.out.println(rawFiducialArray);
        }
        if ((rawFiducialArray = m_vid1TagsSub.receive()).isPresent()) {
            System.out.println(rawFiducialArray);
        }
        // if ((rawFiducialArray = m_vid2TagsSub.receive()).isPresent()) {
        //     System.out.println(rawFiducialArray);
        // }
    }

    private void checkRosPose() {
        TFMessage pose;
        // just log it for rn
        if ((m_poseSub.receive().isPresent()) && (pose = m_poseSub.receive().get()) != null) {
            for (TransformStamped tf : pose.getTransforms()) {
                // if (tf.getChildFrameId() == "base_link") {
                System.out.println(tf);
                // }
            }
        }
    }

    private void sendSwervePose() {
        Pose2d pose = m_drive.getPose();

        Pose rosPose = ROSConversions.wpiToRosPose(new Pose3d(pose));

        m_posePub.send(rosPose);
    }

    @Override
    public void periodic() {
        checkPing();
        checkFiducialDetections();
        checkRosPose();
        sendSwervePose();
    }
}
