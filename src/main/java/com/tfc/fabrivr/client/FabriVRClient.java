package com.tfc.fabrivr.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.openvr.*;
import org.lwjgl.ovr.OVRQuatf;
import org.lwjgl.ovr.OVRVector3f;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

//https://github.com/jMonkeyEngine/jmonkeyengine/blob/master/jme3-vr/src/main/java/com/jme3/input/vr/openvr/OpenVRInput.java
@Environment(EnvType.CLIENT)
public class FabriVRClient implements ClientModInitializer {
	public static float offX = 0;
	public static float offY = 0;
	public static float offZ = 0;
	
	public static float trueOffX = 0;
	public static float trueOffZ = 0;
	
	public static OVRQuatf headQuat;
	public static OVRQuatf hand1Quat;
	public static OVRQuatf hand2Quat;
	public static OVRVector3f headPos;
	public static OVRVector3f hand1Pos;
	public static OVRVector3f hand2Pos;
	
	public static final int[] controllerIndex = new int[VR.k_unMaxTrackedDeviceCount];
	public static TrackedDevicePose.Buffer trackedDevicePose;
	public static TrackedDevicePose[] hmdTrackedDevicePoses;
	public static VRControllerState state;
	public static InputPoseActionData data;
	public static HmdMatrix34 matrix34;
	public static boolean useOVR = false;
	
	public static void initVR() {
		if (useOVR) FabriVROculus.initVR();
	}
	
	public static void update() {
		if (useOVR) FabriVROculus.update();
		else FabriVROpenVR.update();
	}
	
	@Override
	public void onInitializeClient() {
		MemoryStack stack = MemoryStack.stackPush();
		IntBuffer peError = stack.mallocInt(1);
		
		int token = VR.VR_InitInternal(peError, 0);
		
		OpenVR.create(token);
		for (int i = 0; i < VR.k_unMaxTrackedDeviceCount; i++) {
			int classCallback = VRSystem.VRSystem_GetTrackedDeviceClass(i);
			if (classCallback == VR.ETrackedDeviceClass_TrackedDeviceClass_Controller || classCallback == VR.ETrackedDeviceClass_TrackedDeviceClass_GenericTracker) {
				String manufacturerName = VRSystem.VRSystem_GetStringTrackedDeviceProperty(i, VR.ETrackedDeviceProperty_Prop_ManufacturerName_String, null);
				
				if (manufacturerName.equals("Oculus"))
					useOVR = true;
			}
		}
		
		trackedDevicePose = TrackedDevicePose.create(VR.k_unMaxTrackedDeviceCount);
		hmdTrackedDevicePoses = new TrackedDevicePose[VR.k_unMaxTrackedDeviceCount];
		data = InputPoseActionData.malloc();
		matrix34 = HmdMatrix34.malloc();
		state = VRControllerState.malloc();
	}
}
