package com.tfc.fabrivr.utils.openvr;

import org.lwjgl.BufferUtils;
import org.lwjgl.openvr.OpenVR;
import org.lwjgl.openvr.VR;
import org.lwjgl.openvr.VRSystem;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

public class Session {
	public static void init() {
	}
	
	static {
		if (!VR.VR_IsHmdPresent()) throw new RuntimeException("Hmd is not present");
		if (!VR.VR_IsRuntimeInstalled()) throw new RuntimeException("Runtime is not installed");
		MemoryStack stack = MemoryStack.stackPush();
		IntBuffer peError = stack.mallocInt(1);
		
		int token = VR.VR_InitInternal(peError, 0);
		
		OpenVR.create(token);
		
		if (OpenVR.VRCompositor == null) {
			end();
			throw new RuntimeException("VRCompositor does not exist");
		}
		
		for (int i = 0; i < VR.k_unMaxTrackedDeviceCount; i++) {
			int classCallback = VRSystem.VRSystem_GetTrackedDeviceClass(i);
			Tracking.devices.add(new Device(i));
			if (classCallback == VR.ETrackedDeviceClass_TrackedDeviceClass_Controller || classCallback == VR.ETrackedDeviceClass_TrackedDeviceClass_GenericTracker) {
				IntBuffer error = BufferUtils.createIntBuffer(1);
				System.out.println("Device: " + i);
				String controllerName = VRSystem.VRSystem_GetStringTrackedDeviceProperty(i, VR.ETrackedDeviceProperty_Prop_TrackingSystemName_String, error);
				System.out.println(controllerName);
				String controllerType = VRSystem.VRSystem_GetStringTrackedDeviceProperty(i, VR.ETrackedDeviceProperty_Prop_ControllerType_String, error);
				System.out.println(controllerType);
				String manufacturerName = VRSystem.VRSystem_GetStringTrackedDeviceProperty(i, VR.ETrackedDeviceProperty_Prop_ManufacturerName_String, error);
				System.out.println(manufacturerName);
			}
		}
	}
	
	public static void end() {
		OpenVR.destroy();
	}
}
