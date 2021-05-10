package com.tfc.fabrivr.utils.openvr;

import com.tfc.fabrivr.utils.translation.OVROrOpenVR2JomlAndOpenVR;
import com.tfc.fabrivr.utils.translation.ToRawJava;
import org.joml.Quaternionf;
import org.lwjgl.openvr.*;
import org.lwjgl.ovr.*;

//TODO: ovr only mode
public class Device {
	public final int index;
	
	private static TrackedDevicePose[] poses = new TrackedDevicePose[VR.k_unMaxTrackedDeviceCount];
	
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
	
	protected static void update() {
		TrackedDevicePose.Buffer buffer = VRCompositor.getRenderPoses();
		for (int i = 0 ; i < VR.k_unMaxTrackedDeviceCount; i++) {
			poses[i] = buffer.get(i);
		}
	}
	
	public HmdVector3 getPosition() {
//		if (Session.session != null && VRSystem.VRSystem_GetStringTrackedDeviceProperty(index, VR.ETrackedDeviceProperty_Prop_ManufacturerName_String, null).toLowerCase().equals("oculus")) {
//			OVRPosef pose = (isLeftHand() || isRightHand()) ? Session.trackingState.HandPoses(isLeftHand()?0:1).ThePose() : Session.trackingState.HeadPose().ThePose();
//			return OVROrOpenVR2JomlAndOpenVR.fromOVRVector(pose.Position());
//		}
//		VRControllerState state = VRControllerState.malloc();
//		TrackedDevicePose pose = TrackedDevicePose.malloc();
//		VRSystem.VRSystem_GetControllerStateWithPose(VR.ETrackingUniverseOrigin_TrackingUniverseStanding, index, state, pose);
		TrackedDevicePose pose = poses[index];
		return OVROrOpenVR2JomlAndOpenVR.getTranslation(pose.mDeviceToAbsoluteTracking());
	}
	
	public boolean isPresent() {
		return VRSystem.VRSystem_IsTrackedDeviceConnected(index);
	}
	
	public Quaternionf getRotation() {
		if (Session.session != null && VRSystem.VRSystem_GetStringTrackedDeviceProperty(index, VR.ETrackedDeviceProperty_Prop_ManufacturerName_String, null).toLowerCase().equals("oculus")) {
			OVRPosef pose = (isLeftHand() || isRightHand()) ? Session.trackingState.HandPoses(isLeftHand()?0:1).ThePose() : Session.trackingState.HeadPose().ThePose();
			return OVROrOpenVR2JomlAndOpenVR.fromOVRQuatf(pose.Orientation());
		}
//		VRControllerState state = VRControllerState.malloc();
//		TrackedDevicePose pose = TrackedDevicePose.malloc();
//		VRSystem.VRSystem_GetControllerStateWithPose(VR.ETrackingUniverseOrigin_TrackingUniverseStanding, index, state, pose);
		TrackedDevicePose pose = poses[index];
		if (pose.eTrackingResult() != VR.ETrackingResult_TrackingResult_Running_OK) return new Quaternionf();
		//https://www.codeproject.com/Articles/1171122/How-to-Get-Raw-Positional-Data-from-HTC-Vive
		Quaternionf q = new Quaternionf();
		
		float[][] matrix = ToRawJava.matrixTo2dFloatArray(pose.mDeviceToAbsoluteTracking());
		
		//fmax->max
		//for src, max->fmax
		q.w = (float) (Math.sqrt(Math.max(0, 1 + matrix[0][0] + matrix[1][1] + matrix[2][2])) / 2);
		q.x = (float) (Math.sqrt(Math.max(0, 1 + matrix[0][0] - matrix[1][1] - matrix[2][2])) / 2);
		q.y = (float) (Math.sqrt(Math.max(0, 1 - matrix[0][0] + matrix[1][1] - matrix[2][2])) / 2);
		q.z = (float) (Math.sqrt(Math.max(0, 1 - matrix[0][0] - matrix[1][1] + matrix[2][2])) / 2);
		q.x = Math.copySign(q.x, matrix[2][1] - matrix[1][2]);
		q.y = Math.copySign(q.y, matrix[0][2] - matrix[2][0]);
		q.z = Math.copySign(q.z, matrix[1][0] - matrix[0][1]);
		
		return q.normalize();
	}
}
