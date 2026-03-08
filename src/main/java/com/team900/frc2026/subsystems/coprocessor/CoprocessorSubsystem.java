// package com.team900.frc2026.subsystems.coprocessor;

import com.team900.frc2026.RobotState;
import com.team900.frc2026.subsystems.coprocessor.messages.apriltag_msgs.RawFiducialArrayStamped;
import com.team900.frc2026.subsystems.coprocessor.messages.apriltag_msgs.RosPoseObservation;
import com.team900.frc2026.subsystems.vision.VisionIO.PoseObservation;
import com.team900.frc2026.subsystems.vision.VisionIO.PoseObservationType;
import com.team900.lib.rosNetworkTablesBridge.bridge.BridgePublisher;
import com.team900.lib.rosNetworkTablesBridge.bridge.BridgeSubscriber;
import com.team900.lib.rosNetworkTablesBridge.bridge.ROSNetworkTablesBridge;
import com.team900.lib.rosNetworkTablesBridge.conversions.ROSConversions;
import com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive;
import com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point;
import com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Pose;
import com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.PoseWithCovariance;
import com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Quaternion;
import com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist;
import com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.TwistWithCovariance;
import com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3;
import com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.Odometry;
import com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosFloat64;
import com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.Optional;

import org.jetbrains.bio.npy.NpyFile.Header;

public class CoprocessorSubsystem extends SubsystemBase {
    private final RobotState m_robotState;
    private final ROSNetworkTablesBridge m_ros_interface;

    private final BridgePublisher<RosFloat64> m_pingReturnPub;
    private final BridgePublisher<Odometry> m_odomPub;

    private final BridgeSubscriber<RosFloat64> m_pingSendSub;
    private final BridgeSubscriber<RawFiducialArrayStamped> m_vid0TagsSub;
    private final BridgeSubscriber<RawFiducialArrayStamped> m_vid1TagsSub;
    // private final BridgeSubscriber<RawFiducialArrayStamped> m_vid2TagsSub;
    // private final BridgeSubscriber<TFMessage> m_poseSub;
    private final BridgeSubscriber<RosPoseObservation> m_poseObsSub;
    // private final BridgeSubscriber<RosTargetObservation> m_targObsSub;

    public static final String MAP_FRAME = "map";
    public static final String ODOM_FRAME = "odom";
    public static final String BASE_FRAME = "base_link";

    // this is ROS Odometry, not wpi
    private final Odometry m_odomMsg = new Odometry(new RosHeader(0, new TimePrimitive(), ODOM_FRAME), BASE_FRAME,
        new PoseWithCovariance(new Pose(new Point(0, 0, 0), new Quaternion(0, 0, 0, 1)), new Double[] {
                5e-4, 0.0, 0.0, 0.0, 0.0, 0.0,
                0.0, 5e-4, 0.0, 0.0, 0.0, 0.0,
                0.0, 0.0, 5e-4, 0.0, 0.0, 0.0,
                0.0, 0.0, 0.0, 5e-4, 0.0, 0.0,
                0.0, 0.0, 0.0, 0.0, 5e-4, 0.0,
                0.0, 0.0, 0.0, 0.0, 0.0, 5e-4
        }),
        new TwistWithCovariance(new Twist(new Vector3(0, 0, 0), new Vector3(0, 0, 0)), new Double[] {
                1e-4, 0.0, 0.0, 0.0, 0.0, 0.0,
                0.0, 1e-4, 0.0, 0.0, 0.0, 0.0,
                0.0, 0.0, 1e-4, 0.0, 0.0, 0.0,
                0.0, 0.0, 0.0, 1e-4, 0.0, 0.0,
                0.0, 0.0, 0.0, 0.0, 1e-4, 0.0,
                0.0, 0.0, 0.0, 0.0, 0.0, 1e-4
        }));

    public CoprocessorSubsystem(RobotState robotState) {
        long updateDelay = 20;
        NetworkTableInstance instance = NetworkTableInstance.getDefault();
        instance.startServer();

        m_ros_interface = new ROSNetworkTablesBridge(instance.getTable(""), updateDelay);
        
        m_robotState = robotState;

        m_odomPub = new BridgePublisher<>(m_ros_interface, "/wpi_odom");
        m_pingReturnPub = new BridgePublisher<>(m_ros_interface, "/ping_return");
        
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
        // m_poseSub =
        //         new BridgeSubscriber<>(
        //                 m_ros_interface, "/tagslam/odom/body_frc_robot", TFMessage.class);
        m_poseObsSub =
                new BridgeSubscriber<>(
                        m_ros_interface,
                        "/tagslam_bridge/pose_observations",
                        RosPoseObservation.class);
    }
    ;

//     private void checkPing() {
//         Optional<RosFloat64> ping;
//         if ((ping = m_pingSendSub.receive()).isPresent()) {
//             m_pingReturnPub.send(ping.get());
//         }
//     }

//     private void checkFiducialDetections() {
//         Optional<RawFiducialArrayStamped> rawFiducialArray;
//         // just log it for rn
//         if ((rawFiducialArray = m_vid0TagsSub.receive()).isPresent()) {
//             System.out.println(rawFiducialArray);
//         }
//         if ((rawFiducialArray = m_vid1TagsSub.receive()).isPresent()) {
//             System.out.println(rawFiducialArray);
//         }
//         // if ((rawFiducialArray = m_vid2TagsSub.receive()).isPresent()) {
//         //     System.out.println(rawFiducialArray);
//         // }
//     }

    // private void checkRosPose() {
    //     TFMessage pose;
    //     // just log it for rn
    //     if ((m_poseSub.receive().isPresent()) && (pose = m_poseSub.receive().get()) != null) {
    //         for (TransformStamped tf : pose.getTransforms()) {
    //             // if (tf.getChildFrameId() == "base_link") {
    //             System.out.println(tf);
    //             // }
    //         }
    //     }
    // }

    private void checkRosPoseObservation() {
        Optional<RosPoseObservation> rosPoseObsRes;
        RosPoseObservation rosPoseObs;
        if ((rosPoseObsRes = m_poseObsSub.receive()).isPresent()
                && (rosPoseObs = rosPoseObsRes.get()) != null) {
            PoseObservation posObs =
                    new PoseObservation(
                            rosPoseObs.getTimestamp(),
                            ROSConversions.rosToWpiPose(rosPoseObs.getPose()),
                            rosPoseObs.getAmbiguity(),
                            rosPoseObs.getTagCount(),
                            rosPoseObs.getAverageTagDistance(),
                            PoseObservationType.SOLVE_PNP // is this correct?
                            );
        }
    }

    // private void checkRosTargetObservation() {
    //     RosTargetObservation rosTargObs;
    //     if ((m_targObsSub.receive().isPresent()) && (targObs = m_targObsSub.receive().get()) !=
    // null) {
    //         Rotation3d targObsRot = ROSConversions.rosToWpiRotation(targObs.rot);
    //         Rotation2d tx = new Rotation2d(targObsRot.getMeasureZ()); // yaw
    //         Rotation2d ty = new Rotation2d(targObsRot.getMeasureY()); // pitch
    //         TargetObservation targObs = new TargetObservation(tx, ty);
    //     }
    // }

    private void sendOdom() {
        Pose2d pose = m_robotState.getLatestFieldToRobot().getValue();
        ChassisSpeeds velocity = m_robotState.getLatestMeasuredFieldRelativeChassisSpeeds();
        
        m_odomMsg.setHeader(m_odomPub.getHeader(ODOM_FRAME));
        m_odomMsg.getPose().setPose(ROSConversions.wpiToRosPose(new Pose3d(pose)));
        m_odomMsg.getTwist().getTwist()
        .setLinear(new Vector3(velocity.vxMetersPerSecond, velocity.vyMetersPerSecond, 0.0));
        m_odomMsg.getTwist().getTwist().setAngular(new Vector3(0.0, 0.0, velocity.omegaRadiansPerSecond));
        
        m_odomPub.send(m_odomMsg);
    }

    @Override
    public void periodic() {
        checkPing();
        checkFiducialDetections();
        checkRosPoseObservation();
        sendOdom();
    }
}
