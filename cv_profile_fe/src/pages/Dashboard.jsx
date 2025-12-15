import { useState, useEffect, useContext, useRef } from "react";
import { AuthContext } from "../context/AuthContext";
import userService from "../services/user.service";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import {
	Plus,
	Trash2,
	FileText,
	Briefcase,
	GraduationCap,
	Calendar,
	Download,
	BookOpen,
	ExternalLink,
	MapPin,
	User as UserIcon,
	MessageCircle,
	Video,
} from "lucide-react";
import Button from "../components/common/Button";
import Card from "../components/common/Card";
import Modal from "../components/common/Modal";
import Input from "../components/common/Input";
import { ProjectForm, SkillForm, EventForm, PublicationForm } from "../components/Forms";

const Dashboard = () => {
	const { currentUser, updateUser } = useContext(AuthContext);
	const [data, setData] = useState([]);
	const [activeTab, setActiveTab] = useState("profile");
	const [loading, setLoading] = useState(false);

	// Modal State
	const [isModalOpen, setIsModalOpen] = useState(false);
	const [isSubmitting, setIsSubmitting] = useState(false);

	// Profile State
	const [profile, setProfile] = useState(currentUser || {});
	const [isEditingProfile, setIsEditingProfile] = useState(false);
	const [editedProfile, setEditedProfile] = useState({});
	const [avatarPreview, setAvatarPreview] = useState(null);
	const fileInputRef = useRef(null);

	// Chat State
	const [messages, setMessages] = useState([]);
	const [newMessage, setNewMessage] = useState("");
	const [stompClient, setStompClient] = useState(null);

	// Video Call State
	const [isInCall, setIsInCall] = useState(false);
	const [localStream, setLocalStream] = useState(null);
	const [remoteStream, setRemoteStream] = useState(null);
	const [isCameraOn, setIsCameraOn] = useState(true);
	const [isMicOn, setIsMicOn] = useState(true);
	const localVideoRef = useRef(null);
	const remoteVideoRef = useRef(null);
	const peerConnectionRef = useRef(null);

	// Fetch data based on active tab
	useEffect(() => {
		if (!currentUser) return;

		if (activeTab === "profile") {
			// Profile already loaded from currentUser, no fetch needed
			setProfile(currentUser);
		} else if (activeTab === "chat" || activeTab === "video") {
			// No fetch for now
		} else {
			fetchData();
		}
	}, [activeTab, currentUser]);

	// Fetch data function
	const fetchData = async () => {
		setLoading(true);
		try {
			let response;
			switch (activeTab) {
				case "projects":
					response = await userService.getProjects(currentUser.id);
					break;
				case "skills":
					response = await userService.getSkills(currentUser.id);
					break;
				case "events":
					response = await userService.getEvents(currentUser.id);
					break;
				case "publications":
					response = await userService.getPublications(currentUser.id);
					break;
				default:
					response = { data: { data: [] } };
			}

			// Extract data from response - handle ApiResponse format
			const dataArray = response?.data?.data || response?.data || [];
			setData(Array.isArray(dataArray) ? dataArray : []);
		} catch (error) {
			console.error("Failed to fetch data:", error);
			setData([]);
		} finally {
			setLoading(false);
		}
	};

	// Handle create function
	const handleCreate = async (formData) => {
		setIsSubmitting(true);
		try {
			let response;
			switch (activeTab) {
				case "projects":
					response = await userService.createProject(currentUser.id, formData);
					break;
				case "skills":
					response = await userService.createSkill(currentUser.id, formData);
					break;
				case "events":
					response = await userService.createEvent(currentUser.id, formData);
					break;
				case "publications":
					response = await userService.createPublication(currentUser.id, formData);
					break;
			}
			setIsModalOpen(false);
			fetchData(); // Refresh the data
		} catch (error) {
			console.error("Failed to create:", error);
		} finally {
			setIsSubmitting(false);
		}
	};

	// Handle delete function
	const handleDelete = async (id) => {
		if (!confirm("Are you sure you want to delete this item?")) return;

		try {
			switch (activeTab) {
				case "projects":
					await userService.deleteProject(currentUser.id, id);
					break;
				case "skills":
					await userService.deleteSkill(currentUser.id, id);
					break;
				case "events":
					await userService.deleteEvent(currentUser.id, id);
					break;
				case "publications":
					await userService.deletePublication(currentUser.id, id);
					break;
			}
			fetchData(); // Refresh the data
		} catch (error) {
			console.error("Failed to delete:", error);
		}
	};

	// Handle export PDF function
	const handleExportPdf = async () => {
		try {
			const response = await userService.exportPdf(currentUser.id);
			// For blob response, axios returns data directly
			const blob = new Blob([response.data], { type: "application/pdf" });
			const url = window.URL.createObjectURL(blob);
			const link = document.createElement("a");
			link.href = url;
			link.download = `CV_${currentUser.fullName}_${new Date().toISOString().split("T")[0]}.pdf`;
			document.body.appendChild(link);
			link.click();
			document.body.removeChild(link);
			window.URL.revokeObjectURL(url);
		} catch (error) {
			console.error("Failed to export PDF:", error);
			alert("Failed to export PDF. Please try again.");
		}
	};

	// Handle edit profile
	const handleEditProfile = () => {
		setEditedProfile({ ...profile });
		setIsEditingProfile(true);
	};

	// Handle cancel edit
	const handleCancelEdit = () => {
		setIsEditingProfile(false);
		setEditedProfile({});
	};

	// Handle profile input change
	const handleProfileChange = (field, value) => {
		setEditedProfile((prev) => ({ ...prev, [field]: value }));
	};

	// Handle update profile
	const handleUpdateProfile = async () => {
		setIsSubmitting(true);
		try {
			const response = await userService.updateProfile(currentUser.id, editedProfile);
			// Extract data from response - handle ApiResponse format
			const updatedProfile = response?.data?.data || response?.data || response;
			setProfile(updatedProfile);
			updateUser(updatedProfile); // Update in AuthContext and localStorage
			setIsEditingProfile(false);
			setAvatarPreview(null);
			alert("Profile updated successfully!");
		} catch (error) {
			console.error("Failed to update profile:", error);
			alert("Failed to update profile. Please try again.");
		} finally {
			setIsSubmitting(false);
		}
	};

	// Handle avatar upload button click
	const handleAvatarClick = () => {
		if (fileInputRef.current) {
			fileInputRef.current.click();
		}
	};

	// Handle avatar file selection
	const handleAvatarChange = (e) => {
		const file = e.target.files?.[0];
		if (!file) return;

		// Validate file type
		if (!file.type.startsWith("image/")) {
			alert("Please select an image file");
			return;
		}

		// Validate file size (max 5MB)
		if (file.size > 5 * 1024 * 1024) {
			alert("File size must be less than 5MB");
			return;
		}

		// Create preview
		const reader = new FileReader();
		reader.onloadend = () => {
			setAvatarPreview(reader.result);
			// Convert to base64 and add to editedProfile
			setEditedProfile((prev) => ({ ...prev, avatar: reader.result }));
		};
		reader.readAsDataURL(file);
	};

	// Initialize WebSocket connection when Chat tab is active
	useEffect(() => {
		if (currentUser && (activeTab === "chat" || activeTab === "video")) {
			const socket = new SockJS("http://localhost:8082/ws");
			const client = Stomp.over(socket);
			client.debug = null; // Disable debug logs

			client.connect(
				{},
				() => {
					console.log("WebSocket Connected!");
					setStompClient(client);

					// Subscribe to public chat topic
					client.subscribe("/topic/public", (payload) => {
						const message = JSON.parse(payload.body);
						console.log("Received message:", message);
						console.log("Current user:", currentUser.fullName);
						console.log("Message sender:", message.sender);
						console.log("Is me?", message.sender === currentUser.fullName);

						setMessages((prev) => [
							...prev,
							{
								sender: message.sender,
								text: message.content || message.text || "",
								time: new Date().toLocaleTimeString("vi-VN", {
									hour: "2-digit",
									minute: "2-digit",
								}),
							},
						]);
					});

					// Subscribe to video signaling topic
					client.subscribe("/topic/video-signal", async (payload) => {
						const signal = JSON.parse(payload.body);
						console.log("Received video signal:", signal);

						// Skip own signals
						if (signal.fromUserId === currentUser.id) return;

						const peerConnection = peerConnectionRef.current;
						if (!peerConnection) return;

						try {
							if (signal.type === "offer") {
								// Received offer - create answer
								await peerConnection.setRemoteDescription(
									new RTCSessionDescription(signal.data),
								);
								const answer = await peerConnection.createAnswer();
								await peerConnection.setLocalDescription(answer);

								// Send answer back
								client.send(
									"/app/video.answer",
									{},
									JSON.stringify({
										type: "answer",
										fromUserId: currentUser.id,
										toUserId: signal.fromUserId,
										data: answer,
									}),
								);
							} else if (signal.type === "answer") {
								// Received answer
								await peerConnection.setRemoteDescription(
									new RTCSessionDescription(signal.data),
								);
							} else if (signal.type === "ice-candidate") {
								// Received ICE candidate
								await peerConnection.addIceCandidate(new RTCIceCandidate(signal.data));
							}
						} catch (error) {
							console.error("Error handling video signal:", error);
						}
					});

					// Add user to chat
					client.send(
						"/app/chat.addUser",
						{},
						JSON.stringify({ sender: currentUser.fullName, type: "JOIN" }),
					);
				},
				(err) => {
					console.error("WebSocket Connection Error:", err);
					alert("Could not connect to server. Please make sure the backend is running.");
				},
			);
		}

		return () => {
			if (stompClient && stompClient.connected) {
				stompClient.disconnect(() => {
					console.log("WebSocket Disconnected");
				});
			}
		};
	}, [activeTab, currentUser]);

	// Cleanup video streams on unmount
	useEffect(() => {
		return () => {
			if (localStream) {
				localStream.getTracks().forEach((track) => track.stop());
			}
			if (peerConnectionRef.current) {
				peerConnectionRef.current.close();
			}
		};
	}, [localStream]);

	const handleSendMessage = (e) => {
		e.preventDefault();
		if (!newMessage.trim() || !stompClient) return;

		const chatMessage = {
			sender: currentUser.fullName,
			content: newMessage,
			type: "CHAT",
		};

		stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
		setNewMessage("");
	};

	// Video Call Functions
	const startVideoCall = async () => {
		try {
			// Check if WebSocket is connected
			if (!stompClient || !stompClient.connected) {
				alert("Please wait for connection to establish...");
				return;
			}

			const stream = await navigator.mediaDevices.getUserMedia({
				video: isCameraOn,
				audio: isMicOn,
			});

			setLocalStream(stream);
			if (localVideoRef.current) {
				localVideoRef.current.srcObject = stream;
			}

			setIsInCall(true);

			// Setup WebRTC peer connection
			const configuration = {
				iceServers: [{ urls: "stun:stun.l.google.com:19302" }, { urls: "stun:stun1.l.google.com:19302" }],
			};

			const peerConnection = new RTCPeerConnection(configuration);
			peerConnectionRef.current = peerConnection;

			// Add local stream to peer connection
			stream.getTracks().forEach((track) => {
				peerConnection.addTrack(track, stream);
			});

			// Handle remote stream
			peerConnection.ontrack = (event) => {
				const [remoteStream] = event.streams;
				setRemoteStream(remoteStream);
				if (remoteVideoRef.current) {
					remoteVideoRef.current.srcObject = remoteStream;
				}
			};

			// Send ICE candidates through WebSocket
			peerConnection.onicecandidate = (event) => {
				if (event.candidate && stompClient) {
					stompClient.send(
						"/app/video.signal",
						{},
						JSON.stringify({
							type: "ice-candidate",
							fromUserId: currentUser.id,
							data: event.candidate,
						}),
					);
				}
			};

			// Create offer
			const offer = await peerConnection.createOffer();
			await peerConnection.setLocalDescription(offer);

			// Send offer through WebSocket
			if (stompClient) {
				stompClient.send(
					"/app/video.offer",
					{},
					JSON.stringify({
						type: "offer",
						fromUserId: currentUser.id,
						data: offer,
					}),
				);
			}
		} catch (error) {
			console.error("Error accessing media devices:", error);

			let errorMessage = "Could not access camera/microphone. ";

			if (error.name === "NotAllowedError" || error.name === "PermissionDeniedError") {
				errorMessage += "Please allow camera and microphone permissions in your browser settings.";
			} else if (error.name === "NotFoundError" || error.name === "DevicesNotFoundError") {
				errorMessage += "No camera or microphone found on this device.";
			} else if (error.name === "NotReadableError" || error.name === "TrackStartError") {
				errorMessage += "Camera/microphone is already in use by another application.";
			} else {
				errorMessage += "Error: " + error.message;
			}

			alert(errorMessage);
			setIsInCall(false);
		}
	};

	const endVideoCall = () => {
		if (localStream) {
			localStream.getTracks().forEach((track) => track.stop());
		}
		if (peerConnectionRef.current) {
			peerConnectionRef.current.close();
		}
		setLocalStream(null);
		setRemoteStream(null);
		setIsInCall(false);
	};

	const toggleCamera = () => {
		if (localStream) {
			const videoTrack = localStream.getVideoTracks()[0];
			if (videoTrack) {
				videoTrack.enabled = !videoTrack.enabled;
				setIsCameraOn(videoTrack.enabled);
			}
		}
	};

	const toggleMic = () => {
		if (localStream) {
			const audioTrack = localStream.getAudioTracks()[0];
			if (audioTrack) {
				audioTrack.enabled = !audioTrack.enabled;
				setIsMicOn(audioTrack.enabled);
			}
		}
	};

	const tabs = [
		{ id: "profile", label: "Profile", icon: UserIcon },
		{ id: "projects", label: "Projects", icon: Briefcase },
		{ id: "skills", label: "Skills", icon: GraduationCap },
		{ id: "events", label: "Events", icon: Calendar },
		{ id: "publications", label: "Publications", icon: BookOpen },
		{ id: "chat", label: "Live Chat", icon: MessageCircle },
		{ id: "video", label: "Live Video", icon: Video },
	];

	return (
		<div className="space-y-6">
			<div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
				<div>
					<h1 className="text-2xl font-bold text-slate-900">Dashboard</h1>
					<p className="text-slate-500">Manage your CV and interact.</p>
				</div>
				<div className="flex gap-3">
					<Button onClick={handleExportPdf} variant="secondary" className="gap-2">
						<Download size={16} /> Export PDF
					</Button>
					{["projects", "skills", "events", "publications"].includes(activeTab) && (
						<Button onClick={() => setIsModalOpen(true)} className="gap-2">
							<Plus size={16} /> Add {activeTab?.slice(0, -1)}
						</Button>
					)}
				</div>
			</div>

			{/* Tabs */}
			<div className="border-b border-gray-200 overflow-x-auto">
				<nav className="-mb-px flex space-x-6" aria-label="Tabs">
					{tabs.map((tab) => {
						const Icon = tab.icon;
						const isActive = activeTab === tab.id;
						return (
							<button
								key={tab.id}
								onClick={() => setActiveTab(tab.id)}
								className={`
                                    group inline-flex items-center border-b-2 py-4 px-1 text-sm font-medium transition-colors whitespace-nowrap
                                    ${
									isActive
										? "border-indigo-500 text-indigo-600"
										: "border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700"
								}
                                `}
							>
								<Icon
									className={`-ml-0.5 mr-2 h-4 w-4 ${
										isActive
											? "text-indigo-500"
											: "text-gray-400 group-hover:text-gray-500"
									}`}
								/>
								{tab.label}
							</button>
						);
					})}
				</nav>
			</div>

			{/* Tab Content */}
			<div className="min-h-[400px]">
				{loading ? (
					<div className="flex justify-center py-12">
						<div className="animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-600"></div>
					</div>
				) : (
					<>
						{/* Profile Tab */}
						{activeTab === "profile" && (
							<div className="bg-white rounded-xl shadow-sm border border-slate-200 p-6">
								<div className="flex flex-col md:flex-row gap-8">
									<div className="flex-shrink-0 text-center">
										<div className="h-32 w-32 rounded-full bg-slate-200 mx-auto flex items-center justify-center text-slate-400 text-4xl font-bold uppercase overflow-hidden border-4 border-white shadow-lg">
											{avatarPreview ? (
												<img
													src={avatarPreview}
													alt="Avatar Preview"
													className="w-full h-full object-cover"
												/>
											) : profile.avatar ? (
												<img
													src={profile.avatar}
													alt="Avatar"
													className="w-full h-full object-cover"
												/>
											) : (
												profile.fullName?.charAt(0) || "U"
											)}
										</div>
										<input
											ref={fileInputRef}
											type="file"
											accept="image/*"
											onChange={handleAvatarChange}
											className="hidden"
										/>
										{isEditingProfile && (
											<button
												onClick={handleAvatarClick}
												type="button"
												className="mt-4 text-sm text-indigo-600 hover:text-indigo-500 font-medium"
											>
												Change Photo
											</button>
										)}
									</div>
									<div className="flex-grow space-y-6">
										<div className="grid grid-cols-1 md:grid-cols-2 gap-6">
											<div>
												<label className="block text-sm font-medium text-slate-700">
													Full Name
												</label>
												{isEditingProfile ? (
													<input
														type="text"
														value={editedProfile.fullName || ""}
														onChange={(e) =>
															handleProfileChange(
																"fullName",
																e.target.value,
															)
														}
														className="mt-1 p-2 w-full border rounded-md focus:border-indigo-500 focus:ring-indigo-500"
													/>
												) : (
													<div className="mt-1 p-2 w-full border rounded-md bg-slate-50 text-slate-700">
														{profile.fullName}
													</div>
												)}
											</div>
											<div>
												<label className="block text-sm font-medium text-slate-700">
													Email
												</label>
												<div className="mt-1 p-2 w-full border rounded-md bg-slate-50 text-slate-700">
													{profile.email}
												</div>
											</div>
											<div>
												<label className="block text-sm font-medium text-slate-700">
													Phone
												</label>
												{isEditingProfile ? (
													<input
														type="text"
														value={editedProfile.phone || ""}
														onChange={(e) =>
															handleProfileChange("phone", e.target.value)
														}
														className="mt-1 p-2 w-full border rounded-md focus:border-indigo-500 focus:ring-indigo-500"
														placeholder="Enter phone number"
													/>
												) : (
													<div className="mt-1 p-2 w-full border rounded-md bg-slate-50 text-slate-700">
														{profile.phone || "Not set"}
													</div>
												)}
											</div>
											<div>
												<label className="block text-sm font-medium text-slate-700">
													Organization / Work
												</label>
												{isEditingProfile ? (
													<input
														type="text"
														value={editedProfile.organization || ""}
														onChange={(e) =>
															handleProfileChange(
																"organization",
																e.target.value,
															)
														}
														className="mt-1 p-2 w-full border rounded-md focus:border-indigo-500 focus:ring-indigo-500"
														placeholder="Enter organization"
													/>
												) : (
													<div className="mt-1 p-2 w-full border rounded-md bg-slate-50 text-slate-700">
														{profile.organization || "Freelance / Unemployed"}
													</div>
												)}
											</div>
											<div className="col-span-1 md:col-span-2">
												<label className="block text-sm font-medium text-slate-700">
													Address
												</label>
												{isEditingProfile ? (
													<input
														type="text"
														value={editedProfile.address || ""}
														onChange={(e) =>
															handleProfileChange(
																"address",
																e.target.value,
															)
														}
														className="mt-1 p-2 w-full border rounded-md focus:border-indigo-500 focus:ring-indigo-500"
														placeholder="Enter address"
													/>
												) : (
													<div className="mt-1 p-2 w-full border rounded-md bg-slate-50 text-slate-700">
														{profile.address || "Not set"}
													</div>
												)}
											</div>
											<div>
												<label className="block text-sm font-medium text-slate-700">
													LinkedIn
												</label>
												{isEditingProfile ? (
													<input
														type="text"
														value={editedProfile.linkedin || ""}
														onChange={(e) =>
															handleProfileChange(
																"linkedin",
																e.target.value,
															)
														}
														className="mt-1 p-2 w-full border rounded-md focus:border-indigo-500 focus:ring-indigo-500"
														placeholder="LinkedIn URL"
													/>
												) : (
													<div className="mt-1 p-2 w-full border rounded-md bg-slate-50 text-slate-700 truncate">
														{profile.linkedin ? (
															<a
																href={profile.linkedin}
																target="_blank"
																rel="noopener noreferrer"
																className="text-indigo-600 hover:underline"
															>
																{profile.linkedin}
															</a>
														) : (
															"Not set"
														)}
													</div>
												)}
											</div>
											<div>
												<label className="block text-sm font-medium text-slate-700">
													GitHub
												</label>
												{isEditingProfile ? (
													<input
														type="text"
														value={editedProfile.github || ""}
														onChange={(e) =>
															handleProfileChange("github", e.target.value)
														}
														className="mt-1 p-2 w-full border rounded-md focus:border-indigo-500 focus:ring-indigo-500"
														placeholder="GitHub URL"
													/>
												) : (
													<div className="mt-1 p-2 w-full border rounded-md bg-slate-50 text-slate-700 truncate">
														{profile.github ? (
															<a
																href={profile.github}
																target="_blank"
																rel="noopener noreferrer"
																className="text-indigo-600 hover:underline"
															>
																{profile.github}
															</a>
														) : (
															"Not set"
														)}
													</div>
												)}
											</div>
											<div className="col-span-1 md:col-span-2">
												<label className="block text-sm font-medium text-slate-700">
													Website
												</label>
												{isEditingProfile ? (
													<input
														type="text"
														value={editedProfile.website || ""}
														onChange={(e) =>
															handleProfileChange(
																"website",
																e.target.value,
															)
														}
														className="mt-1 p-2 w-full border rounded-md focus:border-indigo-500 focus:ring-indigo-500"
														placeholder="Website URL"
													/>
												) : (
													<div className="mt-1 p-2 w-full border rounded-md bg-slate-50 text-slate-700 truncate">
														{profile.website ? (
															<a
																href={profile.website}
																target="_blank"
																rel="noopener noreferrer"
																className="text-indigo-600 hover:underline"
															>
																{profile.website}
															</a>
														) : (
															"Not set"
														)}
													</div>
												)}
											</div>
										</div>

										<div>
											<label className="block text-sm font-medium text-slate-700">
												Hobbies & Interests
											</label>
											{isEditingProfile ? (
												<textarea
													value={editedProfile.hobbies || ""}
													onChange={(e) =>
														handleProfileChange("hobbies", e.target.value)
													}
													className="mt-1 p-2 w-full border rounded-md focus:border-indigo-500 focus:ring-indigo-500 min-h-[60px]"
													placeholder="List your hobbies here (e.g., Chess, Reading, Hiking)..."
												/>
											) : (
												<div className="mt-1 p-2 w-full border rounded-md bg-slate-50 text-slate-700 min-h-[60px]">
													{profile.hobbies ||
														"List your hobbies here (e.g., Chess, Reading, Hiking)..."}
												</div>
											)}
										</div>

										<div>
											<label className="block text-sm font-medium text-slate-700">
												Professional Summary
											</label>
											{isEditingProfile ? (
												<textarea
													value={editedProfile.summary || ""}
													onChange={(e) =>
														handleProfileChange("summary", e.target.value)
													}
													className="mt-1 p-2 w-full border rounded-md focus:border-indigo-500 focus:ring-indigo-500 min-h-[100px]"
													placeholder="Write a brief summary about yourself..."
												/>
											) : (
												<div className="mt-1 p-2 w-full border rounded-md bg-slate-50 text-slate-700 min-h-[100px]">
													{profile.summary ||
														"Write a brief summary about yourself..."}
												</div>
											)}
										</div>

										<div className="flex justify-end gap-3">
											{isEditingProfile ? (
												<>
													<Button
														variant="secondary"
														onClick={handleCancelEdit}
														disabled={isSubmitting}
													>
														Cancel
													</Button>
													<Button
														variant="primary"
														onClick={handleUpdateProfile}
														disabled={isSubmitting}
													>
														{isSubmitting ? "Saving..." : "Save Changes"}
													</Button>
												</>
											) : (
												<Button variant="primary" onClick={handleEditProfile}>
													Edit Profile
												</Button>
											)}
										</div>
									</div>
								</div>
							</div>
						)}
						{/* Chat Tab */}
						{activeTab === "chat" && (
							<div className="bg-white rounded-xl shadow-sm border border-slate-200 h-[650px] flex flex-col">
								{/* Chat Header */}
								<div className="p-4 border-b border-slate-200 bg-gradient-to-r from-indigo-50 to-purple-50 rounded-t-xl">
									<div className="flex items-center justify-between">
										<div className="flex items-center gap-3">
											<div className="relative">
												<div className="h-10 w-10 rounded-full bg-gradient-to-br from-indigo-500 to-purple-600 flex items-center justify-center text-white font-semibold">
													{currentUser?.fullName?.charAt(0) || "U"}
												</div>
												<div className="absolute bottom-0 right-0 h-3 w-3 rounded-full bg-green-500 border-2 border-white"></div>
											</div>
											<div>
												<h3 className="font-semibold text-slate-800">
													Public Chat Room
												</h3>
												<p className="text-xs text-slate-500 flex items-center gap-1">
													<span className="inline-block h-1.5 w-1.5 rounded-full bg-green-500"></span>
													Online
												</p>
											</div>
										</div>
										<div className="flex items-center gap-2">
											<button className="p-2 hover:bg-white/50 rounded-lg transition-colors">
												<svg
													className="w-5 h-5 text-slate-600"
													fill="none"
													stroke="currentColor"
													viewBox="0 0 24 24"
												>
													<path
														strokeLinecap="round"
														strokeLinejoin="round"
														strokeWidth={2}
														d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
													/>
												</svg>
											</button>
											<button className="p-2 hover:bg-white/50 rounded-lg transition-colors">
												<svg
													className="w-5 h-5 text-slate-600"
													fill="none"
													stroke="currentColor"
													viewBox="0 0 24 24"
												>
													<path
														strokeLinecap="round"
														strokeLinejoin="round"
														strokeWidth={2}
														d="M12 5v.01M12 12v.01M12 19v.01M12 6a1 1 0 110-2 1 1 0 010 2zm0 7a1 1 0 110-2 1 1 0 010 2zm0 7a1 1 0 110-2 1 1 0 010 2z"
													/>
												</svg>
											</button>
										</div>
									</div>
								</div>

								{/* Messages Container */}
								<div className="flex-grow p-4 overflow-y-auto space-y-1 bg-gradient-to-br from-slate-50 via-white to-indigo-50/30">
									{/* Date Divider */}
									<div className="flex justify-center my-4">
										<span className="text-xs text-slate-500 bg-white/80 backdrop-blur-sm px-3 py-1 rounded-full border border-slate-200 shadow-sm">
											{new Date().toLocaleDateString("en-US", {
												weekday: "long",
												month: "long",
												day: "numeric",
											})}
										</span>
									</div>

									{messages.map((msg, i) => {
										const isMe = msg.sender === currentUser?.fullName;
										const prevMsg = i > 0 ? messages[i - 1] : null;
										const nextMsg = i < messages.length - 1 ? messages[i + 1] : null;

										// Check if this is the first message in a group from the same sender
										const isFirstInGroup = !prevMsg || prevMsg.sender !== msg.sender;
										// Check if this is the last message in a group from the same sender
										const isLastInGroup = !nextMsg || nextMsg.sender !== msg.sender;

										return (
											<div
												key={i}
												className={`flex gap-2 ${
													isMe ? "justify-end" : "justify-start"
												} ${isFirstInGroup ? "mt-4" : "mt-0.5"} animate-fadeIn`}
											>
												{/* Avatar - only show for first message in group from others */}
												{!isMe && (
													<div
														className={`h-8 w-8 flex-shrink-0 ${
															isLastInGroup ? "" : "invisible"
														}`}
													>
														{isLastInGroup && (
															<div className="h-8 w-8 rounded-full bg-gradient-to-br from-slate-400 to-slate-600 flex items-center justify-center text-white text-xs font-semibold">
																{msg.sender?.charAt(0) || "?"}
															</div>
														)}
													</div>
												)}

												<div
													className={`flex flex-col ${
														isMe ? "items-end" : "items-start"
													} max-w-[70%]`}
												>
													{/* Sender name - only show for first message in group from others */}
													{!isMe && isFirstInGroup && (
														<span className="text-xs text-slate-600 font-medium mb-1 px-1">
															{msg.sender}
														</span>
													)}

													{/* Message bubble */}
													<div
														className={`rounded-2xl px-4 py-2.5 break-words ${
															isMe
																? "bg-gradient-to-br from-indigo-600 to-indigo-700 text-white shadow-lg shadow-indigo-200"
																: "bg-white text-slate-800 shadow-md border border-slate-100"
														} ${
															isMe
																? isLastInGroup
																	? "rounded-br-md"
																	: "rounded-br-lg"
																: isLastInGroup
																? "rounded-bl-md"
																: "rounded-bl-lg"
														}`}
													>
														<p className="text-sm leading-relaxed whitespace-pre-wrap">
															{msg.text ||
																msg.content ||
																"(Empty message)"}
														</p>
													</div>

													{/* Timestamp - only show for last message in group */}
													{isLastInGroup && (
														<span className="text-[10px] text-slate-400 mt-1 px-1">
															{msg.time}
														</span>
													)}
												</div>

												{/* Avatar for current user - only show for last message in group */}
												{isMe && (
													<div
														className={`h-8 w-8 flex-shrink-0 ${
															isLastInGroup ? "" : "invisible"
														}`}
													>
														{isLastInGroup && (
															<div className="h-8 w-8 rounded-full bg-gradient-to-br from-indigo-500 to-purple-600 flex items-center justify-center text-white text-xs font-semibold">
																{currentUser?.fullName?.charAt(0) ||
																	"U"}
															</div>
														)}
													</div>
												)}
											</div>
										);
									})}

									{messages.length === 0 && (
										<div className="flex flex-col items-center justify-center h-full text-slate-400">
											<div className="bg-gradient-to-br from-slate-100 to-slate-50 p-8 rounded-2xl border border-slate-200">
												<MessageCircle
													size={56}
													className="mx-auto mb-4 opacity-30"
												/>
												<p className="text-lg font-medium text-slate-500">
													No messages yet
												</p>
												<p className="text-sm text-slate-400 mt-1">
													Start the conversation!
												</p>
											</div>
										</div>
									)}
								</div>

								{/* Input Area */}
								<div className="p-4 bg-white border-t border-slate-200 rounded-b-xl">
									<form onSubmit={handleSendMessage} className="flex gap-3">
										<button
											type="button"
											className="p-2.5 hover:bg-slate-100 rounded-xl transition-colors"
											title="Attach file"
										>
											<svg
												className="w-5 h-5 text-slate-600"
												fill="none"
												stroke="currentColor"
												viewBox="0 0 24 24"
											>
												<path
													strokeLinecap="round"
													strokeLinejoin="round"
													strokeWidth={2}
													d="M15.172 7l-6.586 6.586a2 2 0 102.828 2.828l6.414-6.586a4 4 0 00-5.656-5.656l-6.415 6.585a6 6 0 108.486 8.486L20.5 13"
												/>
											</svg>
										</button>
										<input
											type="text"
											className="flex-grow rounded-xl border-slate-200 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 px-4 py-2.5 border transition-all placeholder:text-slate-400"
											placeholder="Type your message..."
											value={newMessage}
											onChange={(e) => setNewMessage(e.target.value)}
										/>
										<button
											type="button"
											className="p-2.5 hover:bg-slate-100 rounded-xl transition-colors"
											title="Emoji"
										>
											<svg
												className="w-5 h-5 text-slate-600"
												fill="none"
												stroke="currentColor"
												viewBox="0 0 24 24"
											>
												<path
													strokeLinecap="round"
													strokeLinejoin="round"
													strokeWidth={2}
													d="M14.828 14.828a4 4 0 01-5.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
												/>
											</svg>
										</button>
										<Button
											type="submit"
											disabled={!newMessage.trim()}
											className="px-6 bg-gradient-to-r from-indigo-600 to-indigo-700 hover:from-indigo-700 hover:to-indigo-800 shadow-lg shadow-indigo-200 disabled:opacity-50 disabled:cursor-not-allowed"
										>
											<svg
												className="w-5 h-5"
												fill="none"
												stroke="currentColor"
												viewBox="0 0 24 24"
											>
												<path
													strokeLinecap="round"
													strokeLinejoin="round"
													strokeWidth={2}
													d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8"
												/>
											</svg>
										</Button>
									</form>
									<p className="text-[10px] text-slate-400 mt-2 text-center">
										Press Enter to send • Shift + Enter for new line
									</p>
								</div>
							</div>
						)}{" "}
						{/* Video Tab */}
						{activeTab === "video" && (
							<div className="bg-white rounded-xl shadow-sm border border-slate-200 p-6">
								<div className="max-w-5xl mx-auto space-y-6">
									{!isInCall ? (
										<div className="space-y-8">
											<div className="aspect-video bg-slate-900 rounded-xl flex items-center justify-center relative overflow-hidden group">
												<div className="absolute inset-0 bg-gradient-to-t from-black/60 to-transparent"></div>
												<div className="relative z-10 text-center text-white">
													<div
														onClick={startVideoCall}
														className="h-20 w-20 rounded-full bg-white/20 backdrop-blur-sm flex items-center justify-center mx-auto mb-4 cursor-pointer hover:bg-white/30 transition-all hover:scale-105"
													>
														<Video size={40} className="ml-1" />
													</div>
													<h3 className="text-xl font-bold">Start Video Call</h3>
													<p className="text-slate-300">
														Click to start a peer-to-peer video call
													</p>

													{/* Camera and Mic Toggle Before Call */}
													<div className="flex justify-center gap-4 mt-6">
														<button
															onClick={(e) => {
																e.stopPropagation();
																setIsCameraOn(!isCameraOn);
															}}
															className={`px-4 py-2 rounded-lg transition-all ${
																isCameraOn
																	? "bg-white/20 hover:bg-white/30"
																	: "bg-red-500/80 hover:bg-red-600/80"
															}`}
														>
															{isCameraOn
																? "📹 Camera On"
																: "📹 Camera Off"}
														</button>
														<button
															onClick={(e) => {
																e.stopPropagation();
																setIsMicOn(!isMicOn);
															}}
															className={`px-4 py-2 rounded-lg transition-all ${
																isMicOn
																	? "bg-white/20 hover:bg-white/30"
																	: "bg-red-500/80 hover:bg-red-600/80"
															}`}
														>
															{isMicOn ? "🎤 Mic On" : "🎤 Mic Off"}
														</button>
													</div>
												</div>
											</div>

											<div className="text-center text-sm text-slate-600">
												<p className="mb-2">
													✓ Peer-to-peer connection using WebRTC
												</p>
												<p className="mb-2">✓ No third-party services required</p>
												<p>✓ Secure and private communication</p>
											</div>
										</div>
									) : (
										<div className="space-y-4">
											<div className="grid grid-cols-1 md:grid-cols-2 gap-4">
												{/* Local Video */}
												<div className="relative aspect-video bg-slate-900 rounded-xl overflow-hidden">
													<video
														ref={localVideoRef}
														autoPlay
														muted
														playsInline
														className="w-full h-full object-cover"
													/>
													<div className="absolute bottom-4 left-4 bg-black/50 px-3 py-1 rounded-full text-white text-sm">
														You {!isCameraOn && "(Camera Off)"}
													</div>
												</div>

												{/* Remote Video */}
												<div className="relative aspect-video bg-slate-900 rounded-xl overflow-hidden">
													{remoteStream ? (
														<video
															ref={remoteVideoRef}
															autoPlay
															playsInline
															className="w-full h-full object-cover"
														/>
													) : (
														<div className="w-full h-full flex items-center justify-center text-slate-400">
															<div className="text-center">
																<UserIcon
																	size={48}
																	className="mx-auto mb-2 opacity-50"
																/>
																<p>Waiting for other person...</p>
															</div>
														</div>
													)}
													<div className="absolute bottom-4 left-4 bg-black/50 px-3 py-1 rounded-full text-white text-sm">
														Remote
													</div>
												</div>
											</div>

											{/* Call Controls */}
											<div className="flex justify-center gap-3">
												<Button
													variant="secondary"
													onClick={toggleCamera}
													className={`gap-2 ${
														!isCameraOn
															? "bg-red-500 hover:bg-red-600"
															: "bg-slate-600 hover:bg-slate-700"
													} text-white`}
												>
													{isCameraOn
														? "📹 Turn Off Camera"
														: "📹 Turn On Camera"}
												</Button>
												<Button
													variant="secondary"
													onClick={toggleMic}
													className={`gap-2 ${
														!isMicOn
															? "bg-red-500 hover:bg-red-600"
															: "bg-slate-600 hover:bg-slate-700"
													} text-white`}
												>
													{isMicOn ? "🎤 Mute Mic" : "🎤 Unmute Mic"}
												</Button>
												<Button
													variant="secondary"
													onClick={endVideoCall}
													className="bg-red-600 hover:bg-red-700 text-white gap-2"
												>
													<Video size={16} />
													End Call
												</Button>
											</div>
										</div>
									)}
								</div>
							</div>
						)}
						{/* List Grids (Projects, Skills, etc.) - Existing Logic */}
						{["projects", "skills", "events", "publications"].includes(activeTab) && (
							<div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
								{/* ... (Existing Map Logic) ... */}
								{data.map((item) => (
									<Card
										key={item.id}
										className="relative group hover:shadow-md transition-shadow"
									>
										{/* Render different content based on type */}
										{activeTab === "projects" && (
											<div className="space-y-3">
												<div className="flex justify-between items-start">
													<h3 className="font-semibold text-lg text-slate-900">
														{item.title}
													</h3>
													<div className="bg-indigo-50 text-indigo-700 px-2.5 py-0.5 rounded-full text-xs font-medium border border-indigo-100">
														{item.role}
													</div>
												</div>
												<p className="text-sm text-slate-600 line-clamp-3">
													{item.description}
												</p>
												<div className="pt-2 flex flex-wrap gap-2">
													{item.technologies?.split(",").map((tech, i) => (
														<span
															key={i}
															className="text-xs bg-slate-100 text-slate-600 px-2 py-1 rounded"
														>
															{tech.trim()}
														</span>
													))}
												</div>
											</div>
										)}

										{activeTab === "skills" && (
											<div className="flex items-center justify-between">
												<div className="flex items-center gap-3">
													<div className="h-10 w-10 rounded-lg bg-indigo-100 flex items-center justify-center text-indigo-600">
														<GraduationCap size={20} />
													</div>
													<div>
														<h3 className="font-medium text-slate-900">
															{item.name}
														</h3>
														<p className="text-xs text-slate-500">
															{item.category}
														</p>
													</div>
												</div>
												<div className="text-right">
													<span className="text-2xl font-bold text-slate-700">
														{item.proficiencyLevel}%
													</span>
												</div>
											</div>
										)}

										{activeTab === "events" && (
											<div className="space-y-3">
												<div className="flex items-start justify-between">
													<h3 className="font-semibold text-slate-900">
														{item.name}
													</h3>
													<span className="text-xs text-slate-500 bg-slate-100 px-2 py-1 rounded">
														{item.eventDate}
													</span>
												</div>
												<div className="flex items-center gap-1 text-xs text-slate-500">
													<MapPin size={12} />
													{item.location}
												</div>
												<p className="text-sm text-slate-600 line-clamp-2">
													{item.description}
												</p>
											</div>
										)}

										{activeTab === "publications" && (
											<div className="space-y-2">
												<h3 className="font-medium text-slate-900 leading-snug">
													{item.title}
												</h3>
												<div className="text-xs text-slate-500">
													<span className="font-medium text-slate-700">
														{item.journal}
													</span>{" "}
													• {item.publishDate}
												</div>
												<p className="text-xs text-slate-500 italic">
													Authored by {item.authors}
												</p>
												{item.url && (
													<a
														href={item.url}
														target="_blank"
														rel="noopener noreferrer"
														className="inline-flex items-center text-xs text-indigo-600 hover:text-indigo-500 mt-2"
													>
														Read more{" "}
														<ExternalLink size={10} className="ml-1" />
													</a>
												)}
											</div>
										)}

										{/* Action Buttons */}
										<div className="absolute top-4 right-4 opacity-0 group-hover:opacity-100 transition-opacity">
											<button
												onClick={(e) => {
													e.stopPropagation();
													handleDelete(item.id);
												}}
												className="p-1.5 text-slate-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition-colors"
												title="Delete"
											>
												<Trash2 size={16} />
											</button>
										</div>
									</Card>
								))}

								{data.length === 0 && (
									<div className="col-span-full text-center py-12 bg-white rounded-xl border border-dashed border-slate-300">
										<div className="mx-auto h-12 w-12 text-slate-400">
											<Plus size={48} strokeWidth={1} />
										</div>
										<h3 className="mt-2 text-sm font-semibold text-slate-900">
											No content
										</h3>
										<p className="mt-1 text-sm text-slate-500">
											Get started by creating a new {activeTab.slice(0, -1)}.
										</p>
									</div>
								)}
							</div>
						)}
					</>
				)}
			</div>

			{/* Creation Modal */}
			<Modal
				isOpen={isModalOpen}
				onClose={() => setIsModalOpen(false)}
				title={`Add New ${
					activeTab.slice(0, -1).charAt(0).toUpperCase() + activeTab.slice(0, -1).slice(1)
				}`}
			>
				{activeTab === "projects" && <ProjectForm onSubmit={handleCreate} isLoading={isSubmitting} />}
				{activeTab === "skills" && <SkillForm onSubmit={handleCreate} isLoading={isSubmitting} />}
				{activeTab === "events" && <EventForm onSubmit={handleCreate} isLoading={isSubmitting} />}
				{activeTab === "publications" && (
					<PublicationForm onSubmit={handleCreate} isLoading={isSubmitting} />
				)}
			</Modal>
		</div>
	);
};

export default Dashboard;
