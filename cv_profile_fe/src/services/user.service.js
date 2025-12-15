import api from "./api";

const SERVICE = {
	// User Profile
	getProfile: (userId) => api.get(`/users/${userId}`),
	updateProfile: (userId, data) => api.put(`/users/${userId}`, data),

	// Projects
	getProjects: (userId) => api.get(`/users/${userId}/projects`),
	createProject: (userId, data) => api.post(`/users/${userId}/projects`, data),
	updateProject: (userId, projectId, data) => api.put(`/users/${userId}/projects/${projectId}`, data),
	deleteProject: (userId, projectId) => api.delete(`/users/${userId}/projects/${projectId}`),

	// Skills
	getSkills: (userId) => api.get(`/users/${userId}/skills`),
	createSkill: (userId, data) => api.post(`/users/${userId}/skills`, data),
	updateSkill: (userId, skillId, data) => api.put(`/users/${userId}/skills/${skillId}`, data),
	deleteSkill: (userId, skillId) => api.delete(`/users/${userId}/skills/${skillId}`),

	// Events
	getEvents: (userId) => api.get(`/users/${userId}/events`),
	createEvent: (userId, data) => api.post(`/users/${userId}/events`, data),
	updateEvent: (userId, eventId, data) => api.put(`/users/${userId}/events/${eventId}`, data),
	deleteEvent: (userId, eventId) => api.delete(`/users/${userId}/events/${eventId}`),

	// Publications
	getPublications: (userId) => api.get(`/users/${userId}/publications`),
	createPublication: (userId, data) => api.post(`/users/${userId}/publications`, data),
	updatePublication: (userId, pubId, data) => api.put(`/users/${userId}/publications/${pubId}`, data),
	deletePublication: (userId, pubId) => api.delete(`/users/${userId}/publications/${pubId}`),

	// PDF
	exportPdf: (userId) => api.get(`/pdf/export/${userId}`, { responseType: "blob" }),
};

export default SERVICE;
