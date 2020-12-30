package com.tfc.fabrivr.client;

import com.tfc.fabrivr.EnumInputType;
import com.tfc.fabrivr.EnumVRButton;
import com.tfc.fabrivr.Vector2;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Quaternion;
import org.lwjgl.BufferUtils;
import org.lwjgl.openvr.*;

import java.nio.IntBuffer;

public class FabriVROpenVR {
	public static final VRControllerState[] cStates = new VRControllerState[VR.k_unMaxTrackedDeviceCount];
	public static final Quaternion[] rotStore = new Quaternion[VR.k_unMaxTrackedDeviceCount];
	public static final Vector3f[] posStore = new Vector3f[VR.k_unMaxTrackedDeviceCount];
	public static final HmdVector2 lastCallAxis[] = new HmdVector2[VR.k_unMaxTrackedDeviceCount];
	private static final Vector2 tempAxis = new Vector2(), temp2Axis = new Vector2();
	public static TrackedDevicePose.Buffer trackedDevicePose;
	public static TrackedDevicePose[] hmdTrackedDevicePoses;
	public static VRControllerState state;
	public static InputPoseActionData data;
	public static HmdMatrix34 matrix34;
	public static int controllerCount;
	
	public static void initVR() {
		for (int i = 0; i < VR.k_unMaxTrackedDeviceCount; i++) {
			rotStore[i] = new Quaternion(0, 0, 0, true);
			posStore[i] = new Vector3f();
			cStates[i] = VRControllerState.create();
			lastCallAxis[i] = HmdVector2.malloc();
		}
		
		controllerCount = 0;
		for (int i = 0; i < VR.k_unMaxTrackedDeviceCount; i++) {
			int classCallback = VRSystem.VRSystem_GetTrackedDeviceClass(i);
			if (classCallback == VR.ETrackedDeviceClass_TrackedDeviceClass_Controller || classCallback == VR.ETrackedDeviceClass_TrackedDeviceClass_GenericTracker) {
				IntBuffer error = BufferUtils.createIntBuffer(1);
				String controllerName = VRSystem.VRSystem_GetStringTrackedDeviceProperty(i, VR.ETrackedDeviceProperty_Prop_TrackingSystemName_String, error);
				String manufacturerName = VRSystem.VRSystem_GetStringTrackedDeviceProperty(i, VR.ETrackedDeviceProperty_Prop_ManufacturerName_String, error);
				FabriVRClient.controllerIndex[controllerCount] = i;
				
				triggerHapticPulse(controllerCount, 1.0f);
				
				System.out.println(controllerName);
				System.out.println(manufacturerName);
				
				controllerCount++;
			}
		}
	}
	
	public static void triggerHapticPulse(int controllerIndex, float seconds) {
		// apparently only axis ID of 0 works
		VRSystem.VRSystem_TriggerHapticPulse(FabriVRClient.controllerIndex[controllerIndex],
				0, (short) Math.round(3f * seconds / 1e-3f));
	}
	
	public static boolean isButtonDown(int controllerIndex, EnumVRButton checkButton) {
		VRControllerState cs = cStates[FabriVRClient.controllerIndex[controllerIndex]];
		switch (checkButton) {
			default:
				return false;
			case ViveGripButton:
				return (cs.ulButtonPressed() & 4) != 0;
			case ViveMenuButton:
				return (cs.ulButtonPressed() & 2) != 0;
			case ViveTrackpadAxis:
				return (cs.ulButtonPressed() & 4294967296L) != 0;
			case ViveTriggerAxis:
				return (cs.ulButtonPressed() & 8589934592L) != 0;
		}
	}
	
	public static Vector2 getAxisRaw(int controllerIndex, EnumInputType forAxis) {
		VRControllerState cs = cStates[FabriVRClient.controllerIndex[controllerIndex]];
		switch (forAxis) {
			default:
				return null;
			case ViveTriggerAxis:
				tempAxis.x = cs.rAxis(1).x();
				tempAxis.y = tempAxis.x;
				break;
			case ViveTrackpadAxis:
				tempAxis.x = cs.rAxis(0).x();
				tempAxis.y = cs.rAxis(0).y();
				break;
		}
		return tempAxis;
	}
	
	public static void update() {
		for (int i = 0; i < controllerCount; i++) {
			int index = FabriVRClient.controllerIndex[i];
			VRSystem.VRSystem_GetControllerState(index, cStates[index], 64);
			cStates[index].ulButtonPressed();
			cStates[index].rAxis();

//			String controllerName = "Unknown";
//			String manufacturerName = "Unknown";
//			controllerName = VRSystem.VRSystem_GetStringTrackedDeviceProperty(i, VR.ETrackedDeviceProperty_Prop_TrackingSystemName_String, null);
//			manufacturerName = VRSystem.VRSystem_GetStringTrackedDeviceProperty(i, VR.ETrackedDeviceProperty_Prop_ManufacturerName_String, null);
//			System.out.println(controllerName);
//			System.out.println(manufacturerName);
//			System.out.println(index);
//
//			System.out.println(
//					getAxisRaw(
//							index,EnumInputType.ViveTriggerAxis
//					)
//			);
		}
	}
	
	public static void destroy() {
		OpenVR.destroy();
	}
}
