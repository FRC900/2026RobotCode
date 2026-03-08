package com.team900.lib.rosNetworkTablesBridge.conversions;

import edu.wpi.first.math.geometry.Transform3d;
import com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader;

public class Transform3dStamped {
    public RosHeader header;
    public String child_frame_id;
    public Transform3d transform = new Transform3d();

    public Transform3dStamped() {

    }

    public Transform3dStamped(RosHeader header, String child_frame_id, Transform3d transform) {
        this.header = header;
        this.child_frame_id = child_frame_id;
        this.transform = transform;
    }
}
