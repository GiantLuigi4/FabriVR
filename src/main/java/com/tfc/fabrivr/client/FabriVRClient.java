package com.tfc.fabrivr.client;

import com.tfc.fabrivr.Settings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.Version;
import org.lwjgl.ovr.*;

import java.nio.IntBuffer;

//https://github.com/Gasteclair/LWJGL-libOVR-VR-in-JAVA-
@Environment(EnvType.CLIENT)
public class FabriVRClient implements ClientModInitializer {
	public static float offX = 0;
	public static float offY = 0;
	public static float offZ = 0;
	
	public static float trueOffX = 0;
	public static float trueOffY = 0;
	public static float trueOffZ = 0;
	public static FabriVRFrameBuffer vrFBO;
	
	public static PointerBuffer textureSwapChain;
	
	private static OVRTrackingState trackingState;
	public static OVRQuatf headQuat;
	public static OVRQuatf hand1Quat;
	public static OVRQuatf hand2Quat;
	public static OVRVector3f headPos;
	public static OVRVector3f hand1Pos;
	public static OVRVector3f hand2Pos;

	public static PointerBuffer session;
	public static OVRGraphicsLuid luid;
	
	public static OVRLayerEyeFov layer;
	
	private static OVRFovPort fovPort;
	
	private static OVREyeRenderDesc eyeRenderDesc0;
	private static OVREyeRenderDesc eyeRenderDesc1;
	
	public static OVRPosef hmdToEyeViewPose0;
	public static OVRPosef hmdToEyeViewPose1;
	
	public static OVRSizei bufferSize;
	
	@Override
	public void onInitializeClient() {
//		System.out.println(Version.getVersion());
//		OVR.ovr_Initialize(null);
//		session = BufferUtils.createPointerBuffer(1);
//		luid = OVRGraphicsLuid.create();
//		if(OVR.ovr_Create(session, luid)!=0) {
//			System.err.println("Couldn't create OVR!");
//			System.exit(-1);
//		}
//		trackingState = OVRTrackingState.malloc();
//		layer = OVRLayerEyeFov.malloc();
//
//		eyeRenderDesc0 = OVREyeRenderDesc.malloc();
//		eyeRenderDesc1 = OVREyeRenderDesc.malloc();
//
//
//		hmdToEyeViewPose0 = OVRPosef.malloc();
//		hmdToEyeViewPose1 = OVRPosef.malloc();
//
//		fovPort = OVRFovPort.malloc();
//		fovPort.set(1.43f,1.43f,1.43f,1.43f);
//
//		OVRSizei recommenedTex0Size = OVRSizei.malloc();
//		OVR.ovr_GetFovTextureSize(session.get(0), OVR.ovrEye_Left, fovPort, 1.0f, recommenedTex0Size);
//
//		OVRSizei recommenedTex1Size = OVRSizei.malloc();
//		OVR.ovr_GetFovTextureSize(session.get(0), OVR.ovrEye_Right,fovPort, 1.0f, recommenedTex1Size);
//
//		bufferSize = OVRSizei.malloc();
//		int bufferSizeW = recommenedTex0Size.w() + recommenedTex1Size.w();
//		int bufferSizeH = Math.max(recommenedTex0Size.h(), recommenedTex1Size.h());
//		bufferSize.set(bufferSizeW, bufferSizeH);
//
//
//		layer.Header().Type(OVR.ovrLayerType_EyeFov);
//		layer.Header().Flags(0);
//		layer.Fov(0, eyeRenderDesc0.Fov());
//		layer.Fov(1, eyeRenderDesc1.Fov());
//		layer.Viewport(0, createRecti(0, 0, bufferSize.w() / 2, bufferSize.h()));
//		layer.Viewport(1, createRecti(bufferSize.w() / 2, 0, bufferSize.w() / 2, bufferSize.h()));
//		layer.RenderPose(0, hmdToEyeViewPose0);
//		layer.RenderPose(1, hmdToEyeViewPose1);
	}
	
	private static OVRHmdDesc hmdDesc;
	
	public static void initVR() {
		OVR.ovr_Initialize(OVRInitParams.create().Flags(OVR.ovrInit_MixedRendering));
		session = BufferUtils.createPointerBuffer(1);
		luid = OVRGraphicsLuid.create();
		
		if(OVR.ovr_Create(session, luid)!=0) {
			System.err.println("Couldn't create OVR!");
			System.exit(-1);
		}
		
		trackingState = OVRTrackingState.malloc();
		
		//INIT RENDERING
		fovPort = OVRFovPort.malloc();
		fovPort.set(
				Settings.INSTANCE.OVR_FOV1,
				Settings.INSTANCE.OVR_FOV2,
				Settings.INSTANCE.OVR_FOV3,
				Settings.INSTANCE.OVR_FOV4
				);
		
		OVRSizei recommenedTex0Size = OVRSizei.malloc();
		OVR.ovr_GetFovTextureSize(session.get(0), OVR.ovrEye_Left, fovPort, 1.0f, recommenedTex0Size);
		
		OVRSizei recommenedTex1Size = OVRSizei.malloc();
		OVR.ovr_GetFovTextureSize(session.get(0), OVR.ovrEye_Right,fovPort, 1.0f, recommenedTex1Size);
		
		bufferSize = OVRSizei.malloc();
		int bufferSizeW = recommenedTex0Size.w() + recommenedTex1Size.w();
		int bufferSizeH = Math.max(recommenedTex0Size.h(), recommenedTex1Size.h());
		bufferSize.set(bufferSizeW, bufferSizeH);
		
		System.out.println(
				bufferSizeW+","+
				bufferSizeH+":"+bufferSize
		);
		System.out.println(
				bufferSize.w()+","+
				bufferSize.h()
		);
		
		textureSwapChain = PointerBuffer.allocateDirect(1);
		
		OVRTextureSwapChainDesc desc = OVRTextureSwapChainDesc.calloc();
		desc.set(OVR.ovrTexture_2D, OVR.OVR_FORMAT_R8G8B8A8_UNORM_SRGB, 1, bufferSize.w(), bufferSize.h(), 1, 1, true, desc.MiscFlags(), desc.BindFlags());
		OVRGL.ovr_CreateTextureSwapChainGL(session.get(0), desc, textureSwapChain);
		
		
		
		IntBuffer chainTexId = BufferUtils.createIntBuffer(1);
		OVRGL.ovr_GetTextureSwapChainBufferGL(session.get(0), textureSwapChain.get(0), 0, chainTexId);
//		vrFBO = new FabriVRFrameBuffer(true,false,bufferSize, chainTexId.get(0));
		
		// Initialize VR structures, filling out description.
		
		eyeRenderDesc0 = OVREyeRenderDesc.malloc();
		eyeRenderDesc1 = OVREyeRenderDesc.malloc();
		
		hmdToEyeViewPose0 = OVRPosef.malloc();
		hmdToEyeViewPose1 = OVRPosef.malloc();
		
		hmdDesc = OVRHmdDesc.malloc();
		OVR.ovr_GetHmdDesc(session.get(0), hmdDesc);
		
		OVRMirrorTextureDesc textureDesc = OVRMirrorTextureDesc.malloc();
//		memset(&desc, 0, sizeof(desc));
		textureDesc.Width(bufferSize.w());
		textureDesc.Height(bufferSize.h());
		textureDesc.Format(OVR.OVR_FORMAT_R8G8B8A8_UNORM_SRGB);
		
		// Create mirror texture and an FBO used to copy mirror texture to back buffer
		OVRGL.novr_CreateMirrorTextureGL(session.get(0),desc.address(),298329123);
		
		OVR.ovr_GetRenderDesc(session.get(0), OVR.ovrEye_Left, hmdDesc.DefaultEyeFov(0), eyeRenderDesc0);
		OVR.ovr_GetRenderDesc(session.get(0), OVR.ovrEye_Right, hmdDesc.DefaultEyeFov(1), eyeRenderDesc1);
		
		hmdToEyeViewPose0 = eyeRenderDesc0.HmdToEyePose();
		hmdToEyeViewPose1 = eyeRenderDesc1.HmdToEyePose();
		
		// Initialize our single full screen Fov layer.
		layer = OVRLayerEyeFov.malloc();
		
		layer.Header().Type(OVR.ovrLayerType_EyeFov);
		layer.Header().Flags(0);
		layer.ColorTexture(0, textureSwapChain.get(0));
		layer.ColorTexture(1, textureSwapChain.get(0));
		layer.Fov(0, eyeRenderDesc0.Fov());
		layer.Fov(1, eyeRenderDesc1.Fov());
		layer.RenderPose(0, hmdToEyeViewPose0);
		layer.RenderPose(1, hmdToEyeViewPose1);
		layer.Viewport(0, createRecti(0, 0, bufferSize.w() / 2, bufferSize.h()));
		layer.Viewport(1, createRecti(bufferSize.w() / 2, 0, bufferSize.w() / 2, bufferSize.h()));
	}
	
	private static OVRRecti createRecti(int x, int y, int w, int h) {
		OVRVector2i pos = OVRVector2i.malloc();
		pos.set(x, y);
		OVRSizei size = OVRSizei.malloc();
		size.set(w, h);
		
		OVRRecti recti = OVRRecti.malloc();
		recti.set(pos, size);
		return recti;
	}
	
	public static void update() {
		OVR.ovr_GetTrackingState(
				session.get(0),
				OVR.ovr_GetTimeInSeconds(),
				true,
				trackingState
		);
		OVRPoseStatef poseState = trackingState.HeadPose();
		OVRPosef pose = poseState.ThePose();
		headQuat = pose.Orientation();
		headPos = pose.Position();

		for (int i=0;i<=1;i++) {
			try {
				pose = trackingState.HandPoses(i).ThePose();
				if (i == 0) {
					hand1Pos = pose.Position();
					hand1Quat = pose.Orientation();
				} else {
					hand2Pos = pose.Position();
					hand2Quat = pose.Orientation();
				}
			} catch (Throwable ignored) {
			}
		}
	}
}
