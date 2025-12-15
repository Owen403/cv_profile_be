import api from './api';

const AUTH_URL = '/auth';

const register = async (userData) => {
  const response = await api.post(`${AUTH_URL}/register`, userData);
  return response.data;
};

const login = async (email, password) => {
  const response = await api.post(`${AUTH_URL}/login`, { email, password });
  if (response.data.success && response.data.data && response.data.data.token) {
    localStorage.setItem('token', response.data.data.token);
    localStorage.setItem('user', JSON.stringify(response.data.data)); // Store the whole LoginResponse as user
  }
  return response.data;
};

const logout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
};

const getCurrentUser = async () => {
    const response = await api.get(`${AUTH_URL}/me`);
    return response.data;
}

const authService = {
  register,
  login,
  logout,
  getCurrentUser
};

export default authService;
