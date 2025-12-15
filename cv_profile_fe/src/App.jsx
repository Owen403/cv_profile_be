import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import Dashboard from './pages/Dashboard';
import ProtectedRoute from './components/ProtectedRoute';
import AuthLayout from './layouts/AuthLayout';
import MainLayout from './layouts/MainLayout';

function App() {
  return (
    <Router>
        <Routes>
          {/* Auth Routes */}
          <Route element={<AuthLayout />}>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
          </Route>
          
          {/* Protected Main Routes */}
          <Route element={<ProtectedRoute />}>
              <Route element={<MainLayout />}>
                 <Route path="/" element={<Dashboard />} />
              </Route>
          </Route>

          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
    </Router>
  )
}

export default App;
