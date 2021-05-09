package com.tfc.fabrivr.utils.openvr;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.openvr.*;

public class Device {
	public final int index;
	
	public Device(int index) {
		this.index = index;
	}
	
	public static Device getDeviceForRole(DeviceRole role) {
		return new Device(VRSystem.VRSystem_GetControllerRoleForTrackedDeviceIndex(role.getID()));
	}
	
	public boolean isLeftHand() {
		return VRSystem.VRSystem_GetControllerRoleForTrackedDeviceIndex(index) == VR.ETrackedControllerRole_TrackedControllerRole_LeftHand;
	}
	
	public boolean isRightHand() {
		return VRSystem.VRSystem_GetControllerRoleForTrackedDeviceIndex(index) == VR.ETrackedControllerRole_TrackedControllerRole_RightHand;
	}
	
	public DeviceRole getRole() {
		return DeviceRole.valueOf(VRSystem.VRSystem_GetControllerRoleForTrackedDeviceIndex(index));
	}
	
	public HmdVector3 getPosition() {
		//TODO:
		VRControllerState state = VRControllerState.malloc();
		TrackedDevicePose pose = TrackedDevicePose.malloc();
		VRSystem.VRSystem_GetControllerStateWithPose(VR.ETrackingUniverseOrigin_TrackingUniverseStanding, index, state, pose);
		HmdMatrix34 matrix34 = pose.mDeviceToAbsoluteTracking();
		float x = matrix34.m((0 * 4) + 3);
		float y = matrix34.m((1 * 4) + 3);
		float z = matrix34.m((2 * 4) + 3);
		HmdVector3 vector3 = HmdVector3.malloc();
		vector3.v(0, x);
		vector3.v(1, y);
		vector3.v(2, z);
		return vector3;
	}
	
	public boolean isPresent() {
		return VRSystem.VRSystem_IsTrackedDeviceConnected(index);
	}
	
	public Quaternionf getRotation() {
		VRControllerState state = VRControllerState.malloc();
		TrackedDevicePose pose = TrackedDevicePose.malloc();
		VRSystem.VRSystem_GetControllerStateWithPose(VR.ETrackingUniverseOrigin_TrackingUniverseStanding, index, state, pose);
		if (pose.eTrackingResult() != VR.ETrackingResult_TrackingResult_Running_OK) return new Quaternionf();
		HmdMatrix34 matrix34 = pose.mDeviceToAbsoluteTracking();
		Matrix4f matrix4fJoml = new Matrix4f();
		int indx = 0;
		matrix4fJoml.set(
				matrix34.m(indx++), matrix34.m(indx++), matrix34.m(indx++), matrix34.m(indx++),
				matrix34.m(indx++), matrix34.m(indx++), matrix34.m(indx++), matrix34.m(indx++),
				matrix34.m(indx++), matrix34.m(indx++), matrix34.m(indx++), matrix34.m(indx++),
				0, 0, 0, 0
		);
		return matrix4fJoml.getNormalizedRotation(new Quaternionf());
	}
}
