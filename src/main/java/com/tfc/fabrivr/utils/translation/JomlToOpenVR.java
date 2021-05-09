package com.tfc.fabrivr.utils.translation;

import org.joml.Quaternionf;
import org.lwjgl.openvr.HmdQuaternion;

public class JomlToOpenVR {
	public static HmdQuaternion fromQuaternionf(Quaternionf quaternionf) {
		HmdQuaternion quaternion = HmdQuaternion.malloc();
		quaternion.x(quaternionf.x);
		quaternion.y(quaternionf.y);
		quaternion.z(quaternionf.z);
		quaternion.w(quaternionf.w);
		return quaternion;
	}
}
