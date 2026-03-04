package com.team900.frc2026.subsystems.coprocessor;

import java.lang.ref.Reference;
import java.util.Optional;

import com.team900.frc2026.subsystems.coprocessor.messages.apriltag_msgs.ApriltagArrayStamped;
import com.team900.frc2026.subsystems.coprocessor.messages.apriltag_msgs.RawFiducialArrayStamped;
import com.team900.lib.util.VirtualSubsystem;

import edu.wpi.first.networktables.NetworkTableInstance;
import frc.team88.ros.bridge.BridgePublisher;
import frc.team88.ros.bridge.BridgeSubscriber;
import frc.team88.ros.bridge.ROSNetworkTablesBridge;
import frc.team88.ros.messages.geometry_msgs.Transform;
import frc.team88.ros.messages.geometry_msgs.TransformStamped;
import frc.team88.ros.messages.std_msgs.RosFloat64;
import frc.team88.ros.messages.tf2_msgs.TFMessage;

public class CoprocessorSubsystem extends VirtualSubsystem {
    private final ROSNetworkTablesBridge m_ros_interface;
    private final BridgeSubscriber<RosFloat64> m_pingSendSub;
    private final BridgePublisher<RosFloat64> m_pingReturnPub;
    private final BridgeSubscriber<RawFiducialArrayStamped> m_vid0TagsSub;
    private final BridgeSubscriber<RawFiducialArrayStamped> m_vid1TagsSub;
    private final BridgeSubscriber<RawFiducialArrayStamped> m_vid2TagsSub;
    private final BridgeSubscriber<TFMessage> m_poseSub;
  
    public CoprocessorSubsystem() {
      long updateDelay = 20;
      NetworkTableInstance instance = NetworkTableInstance.getDefault();
      instance.startServer();
      m_ros_interface = new ROSNetworkTablesBridge(instance.getTable(""), updateDelay);
  
      m_pingSendSub = new BridgeSubscriber<>(m_ros_interface, "/ping_send", RosFloat64.class);
      m_pingReturnPub = new BridgePublisher<>(m_ros_interface, "/ping_return");
      m_vid0TagsSub = new BridgeSubscriber<>(
          m_ros_interface,
          "/apriltag_detection_ov2311_10_9_0_9_video0/tags",
          RawFiducialArrayStamped.class);
      m_vid1TagsSub = new BridgeSubscriber<>(
          m_ros_interface,
          "/apriltag_detection_ov2311_10_9_0_9_video1/tags",
          RawFiducialArrayStamped.class);
      m_vid2TagsSub = new BridgeSubscriber<>(
          m_ros_interface,
          "/apriltag_detection_ov2311_10_9_0_9_video2/tags",
          RawFiducialArrayStamped.class);
      m_poseSub = new BridgeSubscriber<>(
          m_ros_interface,
          "/tagslam/odom/body_frc_robot",
          TFMessage.class);
    };

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
        if ((rawFiducialArray = m_vid2TagsSub.receive()).isPresent()) {
            System.out.println(rawFiducialArray);
        }
    }

    private void checkPose() {
        TFMessage pose;
        // just log it for rn
        if ((pose = m_poseSub.receive().get()) != null) {
          for (TransformStamped tf : pose.getTransforms()) {
            // if (tf.getChildFrameId() == "base_link") {
              System.out.println(tf);
            // }
          }
        }
    }
  
    @Override
    public void periodic() {
      checkPing();
      checkFiducialDetections();
      checkPose();
    }

    @Override
    public void periodicAfterScheduler() {
    }
}  