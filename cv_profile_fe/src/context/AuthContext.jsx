import React, { createContext, useState, useEffect } from "react";
import authService from "../services/auth.service";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
	const [currentUser, setCurrentUser] = useState(undefined);
	const [loading, setLoading] = useState(true);

	useEffect(() => {
		const userStr = localStorage.getItem("user");
		if (userStr && userStr !== "undefined") {
			try {
				setCurrentUser(JSON.parse(userStr));
			} catch (e) {
				console.error("Failed to parse user data", e);
				localStorage.removeItem("user");
			}
		} else {
			localStorage.removeItem("user");
		}
		setLoading(false);
	}, []);

	const login = async (email, password) => {
		const response = await authService.login(email, password);
		if (response.success && response.data) {
			// After login, fetch full user profile from backend
			try {
				const userProfile = await authService.getCurrentUser();
				const fullUserData = {
					...response.data,
					...(userProfile.data?.data || userProfile.data || {}),
				};
				setCurrentUser(fullUserData);
				localStorage.setItem("user", JSON.stringify(fullUserData));
			} catch (error) {
				console.error("Failed to fetch user profile:", error);
				setCurrentUser(response.data);
			}
		}
		return response;
	};

	const register = async (email, password, fullName) => {
		return await authService.register({ email, password, fullName });
	};

	const logout = () => {
		authService.logout();
		setCurrentUser(undefined);
	};

	const updateUser = (userData) => {
		setCurrentUser(userData);
		localStorage.setItem("user", JSON.stringify(userData));
	};

	return (
		<AuthContext.Provider value={{ currentUser, login, register, logout, updateUser, loading }}>
			{!loading && children}
		</AuthContext.Provider>
	);
};
