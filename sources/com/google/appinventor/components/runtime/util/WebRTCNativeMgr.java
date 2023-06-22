package com.google.appinventor.components.runtime.util;

import android.content.Context;
import android.util.Log;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.ReplForm;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RtpReceiver;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

public class WebRTCNativeMgr {
    private static final boolean DEBUG = true;
    private static final String LOG_TAG = "AppInvWebRTC";
    /* access modifiers changed from: private */
    public static final CharsetDecoder utf8Decoder = Charset.forName("UTF-8").newDecoder();
    /* access modifiers changed from: private */
    public DataChannel dataChannel = null;
    DataChannel.Observer dataObserver = new DataChannel.Observer() {
        public void onBufferedAmountChange(long j) {
        }

        public void onMessage(DataChannel.Buffer buffer) {
            try {
                String input = WebRTCNativeMgr.utf8Decoder.decode(buffer.data).toString();
                Log.d(WebRTCNativeMgr.LOG_TAG, "onMessage: received: " + input);
                WebRTCNativeMgr.this.form.evalScheme(input);
            } catch (CharacterCodingException e) {
                Log.e(WebRTCNativeMgr.LOG_TAG, "onMessage decoder error", e);
            }
        }

        public void onStateChange() {
        }
    };
    /* access modifiers changed from: private */
    public boolean first = true;
    /* access modifiers changed from: private */
    public ReplForm form;
    /* access modifiers changed from: private */
    public volatile boolean haveLocalDescription = false;
    private boolean haveOffer = false;
    private List<PeerConnection.IceServer> iceServers = new ArrayList();
    /* access modifiers changed from: private */
    public volatile boolean keepPolling = true;
    PeerConnection.Observer observer = new PeerConnection.Observer() {
        public void onAddStream(MediaStream mediaStream) {
        }

        public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreamArr) {
        }

        public void onDataChannel(DataChannel dataChannel) {
            Log.d(WebRTCNativeMgr.LOG_TAG, "Have Data Channel!");
            Log.d(WebRTCNativeMgr.LOG_TAG, "v5");
            DataChannel unused = WebRTCNativeMgr.this.dataChannel = dataChannel;
            dataChannel.registerObserver(WebRTCNativeMgr.this.dataObserver);
            boolean unused2 = WebRTCNativeMgr.this.keepPolling = false;
            WebRTCNativeMgr.this.timer.cancel();
            Log.d(WebRTCNativeMgr.LOG_TAG, "Poller() Canceled");
            WebRTCNativeMgr.this.seenNonces.clear();
        }

        public void onIceCandidate(IceCandidate iceCandidate) {
            try {
                Log.d(WebRTCNativeMgr.LOG_TAG, "IceCandidate = " + iceCandidate.toString());
                if (iceCandidate.sdp == null) {
                    Log.d(WebRTCNativeMgr.LOG_TAG, "IceCandidate is null");
                } else {
                    Log.d(WebRTCNativeMgr.LOG_TAG, "IceCandidateSDP = " + iceCandidate.sdp);
                }
                JSONObject response = new JSONObject();
                response.put("nonce", WebRTCNativeMgr.this.random.nextInt(Form.MAX_PERMISSION_NONCE));
                JSONObject jsonCandidate = new JSONObject();
                jsonCandidate.put("candidate", iceCandidate.sdp);
                jsonCandidate.put("sdpMLineIndex", iceCandidate.sdpMLineIndex);
                jsonCandidate.put("sdpMid", iceCandidate.sdpMid);
                response.put("candidate", jsonCandidate);
                WebRTCNativeMgr.this.sendRendezvous(response);
            } catch (Exception e) {
                Log.e(WebRTCNativeMgr.LOG_TAG, "Exception during onIceCandidate", e);
            }
        }

        public void onIceCandidatesRemoved(IceCandidate[] iceCandidateArr) {
        }

        public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
        }

        public void onIceConnectionReceivingChange(boolean z) {
        }

        public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
            Log.d(WebRTCNativeMgr.LOG_TAG, "onIceGatheringChange: iceGatheringState = " + iceGatheringState);
        }

        public void onRemoveStream(MediaStream mediaStream) {
        }

        public void onRenegotiationNeeded() {
        }

        public void onSignalingChange(PeerConnection.SignalingState signalingState) {
            Log.d(WebRTCNativeMgr.LOG_TAG, "onSignalingChange: signalingState = " + signalingState);
        }
    };
    /* access modifiers changed from: private */
    public PeerConnection peerConnection;
    /* access modifiers changed from: private */
    public String rCode;
    /* access modifiers changed from: private */
    public Random random = new Random();
    private String rendezvousServer = null;
    /* access modifiers changed from: private */
    public String rendezvousServer2 = null;
    SdpObserver sdpObserver = new SdpObserver() {
        public void onCreateFailure(String str) {
            Log.d(WebRTCNativeMgr.LOG_TAG, "onCreateFailure: " + str);
        }

        public void onCreateSuccess(SessionDescription sessionDescription) {
            try {
                Log.d(WebRTCNativeMgr.LOG_TAG, "sdp.type = " + sessionDescription.type.canonicalForm());
                Log.d(WebRTCNativeMgr.LOG_TAG, "sdp.description = " + sessionDescription.description);
                new DataChannel.Init();
                if (sessionDescription.type == SessionDescription.Type.OFFER) {
                    Log.d(WebRTCNativeMgr.LOG_TAG, "Got offer, about to set remote description (again?)");
                    WebRTCNativeMgr.this.peerConnection.setRemoteDescription(WebRTCNativeMgr.this.sdpObserver, sessionDescription);
                } else if (sessionDescription.type == SessionDescription.Type.ANSWER) {
                    Log.d(WebRTCNativeMgr.LOG_TAG, "onCreateSuccess: type = ANSWER");
                    WebRTCNativeMgr.this.peerConnection.setLocalDescription(WebRTCNativeMgr.this.sdpObserver, sessionDescription);
                    boolean unused = WebRTCNativeMgr.this.haveLocalDescription = true;
                    JSONObject offer = new JSONObject();
                    offer.put("type", "answer");
                    offer.put("sdp", sessionDescription.description);
                    JSONObject response = new JSONObject();
                    response.put("offer", offer);
                    WebRTCNativeMgr.this.sendRendezvous(response);
                }
            } catch (Exception e) {
                Log.e(WebRTCNativeMgr.LOG_TAG, "Exception during onCreateSuccess", e);
            }
        }

        public void onSetFailure(String str) {
        }

        public void onSetSuccess() {
        }
    };
    /* access modifiers changed from: private */
    public TreeSet<String> seenNonces = new TreeSet<>();
    Timer timer = new Timer();

    public WebRTCNativeMgr(String rendezvousServer3, String rendezvousResult) {
        this.rendezvousServer = rendezvousServer3;
        try {
            JSONObject resultJson = new JSONObject((rendezvousResult.isEmpty() || rendezvousResult.startsWith("OK")) ? "{\"rendezvous2\" : \"rendezvous.appinventor.mit.edu\",\"iceservers\" : [{ \"server\" : \"stun:stun.l.google.com:19302\" },{ \"server\" : \"turn:turn.appinventor.mit.edu:3478\",\"username\" : \"oh\",\"password\" : \"boy\"}]}" : rendezvousResult);
            this.rendezvousServer2 = resultJson.getString("rendezvous2");
            JSONArray iceServerArray = resultJson.getJSONArray("iceservers");
            this.iceServers = new ArrayList(iceServerArray.length());
            for (int i = 0; i < iceServerArray.length(); i++) {
                JSONObject jsonServer = iceServerArray.getJSONObject(i);
                PeerConnection.IceServer.Builder builder = PeerConnection.IceServer.builder(jsonServer.getString("server"));
                Log.d(LOG_TAG, "Adding iceServer = " + jsonServer.getString("server"));
                if (jsonServer.has("username")) {
                    builder.setUsername(jsonServer.getString("username"));
                }
                if (jsonServer.has("password")) {
                    builder.setPassword(jsonServer.getString("password"));
                }
                this.iceServers.add(builder.createIceServer());
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "parsing iceServers:", e);
        }
    }

    public void initiate(ReplForm form2, Context context, String code) {
        this.form = form2;
        this.rCode = code;
        PeerConnectionFactory.initializeAndroidGlobals(context, false);
        PeerConnectionFactory factory = new PeerConnectionFactory(new PeerConnectionFactory.Options());
        PeerConnection.RTCConfiguration rtcConfig = new PeerConnection.RTCConfiguration(this.iceServers);
        rtcConfig.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY;
        this.peerConnection = factory.createPeerConnection(rtcConfig, new MediaConstraints(), this.observer);
        this.timer.schedule(new TimerTask() {
            public void run() {
                WebRTCNativeMgr.this.Poller();
            }
        }, 0, 1000);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0099 A[SYNTHETIC, Splitter:B:16:0x0099] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void Poller() {
        /*
            r28 = this;
            r0 = r28
            boolean r0 = r0.keepPolling     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r24 = r0
            if (r24 != 0) goto L_0x0009
        L_0x0008:
            return
        L_0x0009:
            java.lang.String r24 = "AppInvWebRTC"
            java.lang.String r25 = "Poller() Called"
            android.util.Log.d(r24, r25)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r24 = "AppInvWebRTC"
            java.lang.StringBuilder r25 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r25.<init>()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r26 = "Poller: rendezvousServer2 = "
            java.lang.StringBuilder r25 = r25.append(r26)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r0 = r28
            java.lang.String r0 = r0.rendezvousServer2     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r26 = r0
            java.lang.StringBuilder r25 = r25.append(r26)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r25 = r25.toString()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            android.util.Log.d(r24, r25)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            org.apache.http.impl.client.DefaultHttpClient r4 = new org.apache.http.impl.client.DefaultHttpClient     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r4.<init>()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            org.apache.http.client.methods.HttpGet r15 = new org.apache.http.client.methods.HttpGet     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.StringBuilder r24 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r24.<init>()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r25 = "http://"
            java.lang.StringBuilder r24 = r24.append(r25)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r0 = r28
            java.lang.String r0 = r0.rendezvousServer2     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r25 = r0
            java.lang.StringBuilder r24 = r24.append(r25)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r25 = "/rendezvous2/"
            java.lang.StringBuilder r24 = r24.append(r25)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r0 = r28
            java.lang.String r0 = r0.rCode     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r25 = r0
            java.lang.StringBuilder r24 = r24.append(r25)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r25 = "-s"
            java.lang.StringBuilder r24 = r24.append(r25)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r24 = r24.toString()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r0 = r24
            r15.<init>(r0)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            org.apache.http.HttpResponse r16 = r4.execute(r15)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.StringBuilder r18 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r18.<init>()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r13 = 0
            java.io.BufferedReader r14 = new java.io.BufferedReader     // Catch:{ all -> 0x033e }
            java.io.InputStreamReader r24 = new java.io.InputStreamReader     // Catch:{ all -> 0x033e }
            org.apache.http.HttpEntity r25 = r16.getEntity()     // Catch:{ all -> 0x033e }
            java.io.InputStream r25 = r25.getContent()     // Catch:{ all -> 0x033e }
            r24.<init>(r25)     // Catch:{ all -> 0x033e }
            r0 = r24
            r14.<init>(r0)     // Catch:{ all -> 0x033e }
            java.lang.String r10 = ""
        L_0x0089:
            java.lang.String r10 = r14.readLine()     // Catch:{ all -> 0x0095 }
            if (r10 == 0) goto L_0x00c0
            r0 = r18
            r0.append(r10)     // Catch:{ all -> 0x0095 }
            goto L_0x0089
        L_0x0095:
            r24 = move-exception
            r13 = r14
        L_0x0097:
            if (r13 == 0) goto L_0x009c
            r13.close()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
        L_0x009c:
            throw r24     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
        L_0x009d:
            r5 = move-exception
            java.lang.String r24 = "AppInvWebRTC"
            java.lang.StringBuilder r25 = new java.lang.StringBuilder
            r25.<init>()
            java.lang.String r26 = "Caught IOException: "
            java.lang.StringBuilder r25 = r25.append(r26)
            java.lang.String r26 = r5.toString()
            java.lang.StringBuilder r25 = r25.append(r26)
            java.lang.String r25 = r25.toString()
            r0 = r24
            r1 = r25
            android.util.Log.e(r0, r1, r5)
            goto L_0x0008
        L_0x00c0:
            if (r14 == 0) goto L_0x00c5
            r14.close()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
        L_0x00c5:
            r0 = r28
            boolean r0 = r0.keepPolling     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r24 = r0
            if (r24 != 0) goto L_0x00f9
            java.lang.String r24 = "AppInvWebRTC"
            java.lang.String r25 = "keepPolling is false, we're done!"
            android.util.Log.d(r24, r25)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            goto L_0x0008
        L_0x00d6:
            r5 = move-exception
            java.lang.String r24 = "AppInvWebRTC"
            java.lang.StringBuilder r25 = new java.lang.StringBuilder
            r25.<init>()
            java.lang.String r26 = "Caught JSONException: "
            java.lang.StringBuilder r25 = r25.append(r26)
            java.lang.String r26 = r5.toString()
            java.lang.StringBuilder r25 = r25.append(r26)
            java.lang.String r25 = r25.toString()
            r0 = r24
            r1 = r25
            android.util.Log.e(r0, r1, r5)
            goto L_0x0008
        L_0x00f9:
            java.lang.String r17 = r18.toString()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r24 = "AppInvWebRTC"
            java.lang.StringBuilder r25 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r25.<init>()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r26 = "response = "
            java.lang.StringBuilder r25 = r25.append(r26)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r0 = r25
            r1 = r17
            java.lang.StringBuilder r25 = r0.append(r1)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r25 = r25.toString()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            android.util.Log.d(r24, r25)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r24 = ""
            r0 = r17
            r1 = r24
            boolean r24 = r0.equals(r1)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            if (r24 == 0) goto L_0x0151
            java.lang.String r24 = "AppInvWebRTC"
            java.lang.String r25 = "Received an empty response"
            android.util.Log.d(r24, r25)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            goto L_0x0008
        L_0x012e:
            r5 = move-exception
            java.lang.String r24 = "AppInvWebRTC"
            java.lang.StringBuilder r25 = new java.lang.StringBuilder
            r25.<init>()
            java.lang.String r26 = "Caught Exception: "
            java.lang.StringBuilder r25 = r25.append(r26)
            java.lang.String r26 = r5.toString()
            java.lang.StringBuilder r25 = r25.append(r26)
            java.lang.String r25 = r25.toString()
            r0 = r24
            r1 = r25
            android.util.Log.e(r0, r1, r5)
            goto L_0x0008
        L_0x0151:
            org.json.JSONArray r9 = new org.json.JSONArray     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r0 = r17
            r9.<init>(r0)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r24 = "AppInvWebRTC"
            java.lang.StringBuilder r25 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r25.<init>()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r26 = "jsonArray.length() = "
            java.lang.StringBuilder r25 = r25.append(r26)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            int r26 = r9.length()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.StringBuilder r25 = r25.append(r26)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r25 = r25.toString()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            android.util.Log.d(r24, r25)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r7 = 0
        L_0x0175:
            int r24 = r9.length()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r0 = r24
            if (r7 >= r0) goto L_0x0335
            java.lang.String r24 = "AppInvWebRTC"
            java.lang.StringBuilder r25 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r25.<init>()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r26 = "i = "
            java.lang.StringBuilder r25 = r25.append(r26)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r0 = r25
            java.lang.StringBuilder r25 = r0.append(r7)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r25 = r25.toString()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            android.util.Log.d(r24, r25)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r24 = "AppInvWebRTC"
            java.lang.StringBuilder r25 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r25.<init>()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r26 = "element = "
            java.lang.StringBuilder r25 = r25.append(r26)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r26 = r9.optString(r7)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.StringBuilder r25 = r25.append(r26)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r25 = r25.toString()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            android.util.Log.d(r24, r25)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.Object r6 = r9.get(r7)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            org.json.JSONObject r6 = (org.json.JSONObject) r6     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r0 = r28
            boolean r0 = r0.haveOffer     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r24 = r0
            if (r24 != 0) goto L_0x0272
            java.lang.String r24 = "offer"
            r0 = r24
            boolean r24 = r6.has(r0)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            if (r24 != 0) goto L_0x01ce
            int r7 = r7 + 1
            goto L_0x0175
        L_0x01ce:
            java.lang.String r24 = "offer"
            r0 = r24
            java.lang.Object r12 = r6.get(r0)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            org.json.JSONObject r12 = (org.json.JSONObject) r12     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r24 = "sdp"
            r0 = r24
            java.lang.String r19 = r12.optString(r0)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r24 = "type"
            r0 = r24
            java.lang.String r23 = r12.optString(r0)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r24 = 1
            r0 = r24
            r1 = r28
            r1.haveOffer = r0     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r24 = "AppInvWebRTC"
            java.lang.StringBuilder r25 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r25.<init>()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r26 = "sdb = "
            java.lang.StringBuilder r25 = r25.append(r26)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r0 = r25
            r1 = r19
            java.lang.StringBuilder r25 = r0.append(r1)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r25 = r25.toString()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            android.util.Log.d(r24, r25)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r24 = "AppInvWebRTC"
            java.lang.StringBuilder r25 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r25.<init>()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r26 = "type = "
            java.lang.StringBuilder r25 = r25.append(r26)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r0 = r25
            r1 = r23
            java.lang.StringBuilder r25 = r0.append(r1)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r25 = r25.toString()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            android.util.Log.d(r24, r25)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r24 = "AppInvWebRTC"
            java.lang.String r25 = "About to set remote offer"
            android.util.Log.d(r24, r25)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r24 = "AppInvWebRTC"
            java.lang.String r25 = "Got offer, about to set remote description (maincode)"
            android.util.Log.d(r24, r25)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r0 = r28
            org.webrtc.PeerConnection r0 = r0.peerConnection     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r24 = r0
            r0 = r28
            org.webrtc.SdpObserver r0 = r0.sdpObserver     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r25 = r0
            org.webrtc.SessionDescription r26 = new org.webrtc.SessionDescription     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            org.webrtc.SessionDescription$Type r27 = org.webrtc.SessionDescription.Type.OFFER     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r0 = r26
            r1 = r27
            r2 = r19
            r0.<init>(r1, r2)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r24.setRemoteDescription(r25, r26)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r0 = r28
            org.webrtc.PeerConnection r0 = r0.peerConnection     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r24 = r0
            r0 = r28
            org.webrtc.SdpObserver r0 = r0.sdpObserver     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r25 = r0
            org.webrtc.MediaConstraints r26 = new org.webrtc.MediaConstraints     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r26.<init>()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r24.createAnswer(r25, r26)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r24 = "AppInvWebRTC"
            java.lang.String r25 = "createAnswer returned"
            android.util.Log.d(r24, r25)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r7 = -1
        L_0x026e:
            int r7 = r7 + 1
            goto L_0x0175
        L_0x0272:
            java.lang.String r24 = "nonce"
            r0 = r24
            boolean r24 = r6.has(r0)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            if (r24 == 0) goto L_0x026e
            r0 = r28
            boolean r0 = r0.haveLocalDescription     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r24 = r0
            if (r24 != 0) goto L_0x028d
            java.lang.String r24 = "AppInvWebRTC"
            java.lang.String r25 = "Incoming candidate before local description set, punting"
            android.util.Log.d(r24, r25)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            goto L_0x0008
        L_0x028d:
            java.lang.String r24 = "offer"
            r0 = r24
            boolean r24 = r6.has(r0)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            if (r24 == 0) goto L_0x02a2
            int r7 = r7 + 1
            java.lang.String r24 = "AppInvWebRTC"
            java.lang.String r25 = "skipping offer, already processed"
            android.util.Log.d(r24, r25)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            goto L_0x0175
        L_0x02a2:
            java.lang.String r24 = "candidate"
            r0 = r24
            boolean r24 = r6.isNull(r0)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            if (r24 == 0) goto L_0x02b0
            int r7 = r7 + 1
            goto L_0x0175
        L_0x02b0:
            java.lang.String r24 = "nonce"
            r0 = r24
            java.lang.String r11 = r6.optString(r0)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r24 = "candidate"
            r0 = r24
            java.lang.Object r3 = r6.get(r0)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            org.json.JSONObject r3 = (org.json.JSONObject) r3     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r24 = "candidate"
            r0 = r24
            java.lang.String r22 = r3.optString(r0)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r24 = "sdpMid"
            r0 = r24
            java.lang.String r21 = r3.optString(r0)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r24 = "sdpMLineIndex"
            r0 = r24
            int r20 = r3.optInt(r0)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r0 = r28
            java.util.TreeSet<java.lang.String> r0 = r0.seenNonces     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r24 = r0
            r0 = r24
            boolean r24 = r0.contains(r11)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            if (r24 != 0) goto L_0x026e
            r0 = r28
            java.util.TreeSet<java.lang.String> r0 = r0.seenNonces     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r24 = r0
            r0 = r24
            r0.add(r11)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r24 = "AppInvWebRTC"
            java.lang.String r25 = "new nonce, about to add candidate!"
            android.util.Log.d(r24, r25)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r24 = "AppInvWebRTC"
            java.lang.StringBuilder r25 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r25.<init>()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r26 = "candidate = "
            java.lang.StringBuilder r25 = r25.append(r26)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r0 = r25
            r1 = r22
            java.lang.StringBuilder r25 = r0.append(r1)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r25 = r25.toString()     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            android.util.Log.d(r24, r25)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            org.webrtc.IceCandidate r8 = new org.webrtc.IceCandidate     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r0 = r21
            r1 = r20
            r2 = r22
            r8.<init>(r0, r1, r2)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r0 = r28
            org.webrtc.PeerConnection r0 = r0.peerConnection     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            r24 = r0
            r0 = r24
            r0.addIceCandidate(r8)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            java.lang.String r24 = "AppInvWebRTC"
            java.lang.String r25 = "addIceCandidate returned"
            android.util.Log.d(r24, r25)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            goto L_0x026e
        L_0x0335:
            java.lang.String r24 = "AppInvWebRTC"
            java.lang.String r25 = "exited loop"
            android.util.Log.d(r24, r25)     // Catch:{ IOException -> 0x009d, JSONException -> 0x00d6, Exception -> 0x012e }
            goto L_0x0008
        L_0x033e:
            r24 = move-exception
            goto L_0x0097
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.util.WebRTCNativeMgr.Poller():void");
    }

    /* access modifiers changed from: private */
    public void sendRendezvous(final JSONObject data) {
        AsynchUtil.runAsynchronously(new Runnable() {
            public void run() {
                try {
                    data.put("first", WebRTCNativeMgr.this.first);
                    data.put("webrtc", true);
                    data.put("key", WebRTCNativeMgr.this.rCode + "-r");
                    if (WebRTCNativeMgr.this.first) {
                        boolean unused = WebRTCNativeMgr.this.first = false;
                        data.put("apiversion", SdkLevel.getLevel());
                    }
                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost("http://" + WebRTCNativeMgr.this.rendezvousServer2 + "/rendezvous2/");
                    try {
                        Log.d(WebRTCNativeMgr.LOG_TAG, "About to send = " + data.toString());
                        post.setEntity(new StringEntity(data.toString()));
                        client.execute(post);
                    } catch (IOException e) {
                        Log.e(WebRTCNativeMgr.LOG_TAG, "sendRedezvous IOException", e);
                    }
                } catch (Exception e2) {
                    Log.e(WebRTCNativeMgr.LOG_TAG, "Exception in sendRendezvous", e2);
                }
            }
        });
    }

    public void send(String output) {
        try {
            if (this.dataChannel == null) {
                Log.w(LOG_TAG, "No Data Channel in Send");
                return;
            }
            this.dataChannel.send(new DataChannel.Buffer(ByteBuffer.wrap(output.getBytes("UTF-8")), false));
        } catch (UnsupportedEncodingException e) {
            Log.e(LOG_TAG, "While encoding data to send to companion", e);
        }
    }
}
