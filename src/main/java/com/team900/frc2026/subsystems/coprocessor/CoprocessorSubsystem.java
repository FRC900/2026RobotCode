package com.team900.frc2026.subsystems.coprocessor;

import java.util.Optional;

import com.team900.frc2026.subsystems.coprocessor.messages.apriltag_msgs.ApriltagArrayStamped;
import com.team900.lib.util.VirtualSubsystem;

import edu.wpi.first.networktables.NetworkTableInstance;
import frc.team88.ros.bridge.BridgePublisher;
import frc.team88.ros.bridge.BridgeSubscriber;
import frc.team88.ros.bridge.ROSNetworkTablesBridge;
import frc.team88.ros.messages.std_msgs.RosFloat64;

public class CoprocessorSubsystem extends VirtualSubsystem {
    private final ROSNetworkTablesBridge m_ros_interface;
    private final BridgeSubscriber<RosFloat64> m_pingSendSub;
    private final BridgePublisher<RosFloat64> m_pingReturnPub;
    private final BridgeSubscriber<ApriltagArrayStamped> m_ov10909TagsSub;
  
    public CoprocessorSubsystem() {
      long updateDelay = 20;
      NetworkTableInstance instance = NetworkTableInstance.getDefault();
      instance.startServer();
      m_ros_interface = new ROSNetworkTablesBridge(instance.getTable(""), updateDelay);
  
      m_pingSendSub = new BridgeSubscriber<>(m_ros_interface, "/ping_send", RosFloat64.class);
      m_pingReturnPub = new BridgePublisher<>(m_ros_interface, "/ping_return");
      m_ov10909TagsSub =
      new BridgeSubscriber<>(
          m_ros_interface,
          "/apriltag_detection_ov2311_10_9_0_9_video0/tags",
          ApriltagArrayStamped.class);
      }
  
    /**
     * Checks if a new ping message has been received from the ROS environment, and if so, sends it
     * back as a response. This measures the round trip response time.
     */
    private void checkPing() {
      double n = 4.0;
      Optional<RosFloat64> ping = Optional.of(new RosFloat64(n));
      m_pingReturnPub.send(ping.get());
      if ((ping = m_pingSendSub.receive()).isPresent()) {
        m_pingReturnPub.send(ping.get());
        System.out.println("Found ping" + ping.get());
      }
    }
  
    private void getTags() {
      Optional<ApriltagArrayStamped> tags;
      if ((tags = m_ov10909TagsSub.receive()).isPresent()) {
        System.out.println("Tag array: " + tags.get());
      }
    }
  
    @Override
    public void periodic() {
      checkPing();
      getTags();
    }

    @Override
    public void periodicAfterScheduler() {
    }
}  