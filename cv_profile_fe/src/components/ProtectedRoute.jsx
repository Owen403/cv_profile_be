import { Navigate, Outlet } from 'react-router-dom';
import { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';

const ProtectedRoute = () => {
    const { currentUser, loading } = useContext(AuthContext);

    if (loading) {
        return <div className="flex justify-center items-center h-screen">Loading...</div>;
    }

    return currentUser ? <Outlet /> : <Navigate to="/login" />;
};

export default ProtectedRoute;
