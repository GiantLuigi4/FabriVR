package com.tfc.fabrivr.utils.openvr;

import org.lwjgl.openvr.Texture;
import org.lwjgl.openvr.TrackedDevicePose;
import org.lwjgl.openvr.VRTextureBounds;
import org.lwjgl.ovr.OVR;
import org.lwjgl.ovr.OVRGL;

import static org.lwjgl.openvr.VR.k_unMaxTrackedDeviceCount;

public class VRCompositor {
	public static VRCompositorError upload(Eye eye, Texture texture, int flags) {
		return VRCompositorError.valueOf(org.lwjgl.openvr.VRCompositor.VRCompositor_Submit(eye.getId(), texture, null, flags));
	}
	public static VRCompositorError upload(Eye eye, Texture texture, VRTextureBounds bounds, int flags) {
		return VRCompositorError.valueOf(org.lwjgl.openvr.VRCompositor.VRCompositor_Submit(eye.getId(), texture, bounds, flags));
	}
	
	public static int getFocusProcess() {
		return org.lwjgl.openvr.VRCompositor.VRCompositor_GetCurrentSceneFocusProcess();
	}
	
	//https://github.com/hdonk/lwjgl_vr_framework/blob/4329f2520dff2462afe83e39a3f65af3c73a2101/src/lwjgl_vr_framework/lwjgl_vr_test.java#L122
	public static TrackedDevicePose.Buffer getRenderPoses() {
		TrackedDevicePose.Buffer tdpb = TrackedDevicePose.create(k_unMaxTrackedDeviceCount);
		TrackedDevicePose.Buffer tdpb2 = TrackedDevicePose.create(k_unMaxTrackedDeviceCount);
		
		org.lwjgl.openvr.VRCompositor.VRCompositor_WaitGetPoses(tdpb, tdpb2);
		return tdpb;
	}
}
