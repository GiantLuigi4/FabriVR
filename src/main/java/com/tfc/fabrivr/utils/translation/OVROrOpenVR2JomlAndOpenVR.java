package com.tfc.fabrivr.utils.translation;

import org.joml.Quaternionf;
import org.lwjgl.openvr.HmdMatrix34;
import org.lwjgl.openvr.HmdVector3;
import org.lwjgl.ovr.OVRQuatf;
import org.lwjgl.ovr.OVRVector3f;

public class OVROrOpenVR2JomlAndOpenVR {
	public static Quaternionf fromOVRQuatf(OVRQuatf src) {
		return new Quaternionf(src.x(), src.y(),src.z(), src.w());
	}
	
	public static HmdVector3 fromOVRVector(OVRVector3f position) {
		HmdVector3 vector3 = HmdVector3.malloc();
		vector3.v(0, position.x());
		vector3.v(1, position.y() + 1.1f);
		vector3.v(2, position.z());
		return vector3;
	}
	
	public static HmdVector3 getTranslation(HmdMatrix34 matrix34) {
		float x = matrix34.m((0 * 4) + 3);
		float y = matrix34.m((1 * 4) + 3);
		float z = matrix34.m((2 * 4) + 3);
		HmdVector3 vector3 = HmdVector3.malloc();
		vector3.v(0, x);
		vector3.v(1, y);
		vector3.v(2, z);
		return vector3;
	}
}
