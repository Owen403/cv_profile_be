import React from 'react';
import { Outlet } from 'react-router-dom';

const AuthLayout = () => {
    return (
        <div className="min-h-screen bg-slate-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8 relative overflow-hidden">
             {/* Abstract Background Decoration */}
             <div className="absolute top-0 left-0 w-full h-full overflow-hidden z-0">
                <div className="absolute top-0 left-1/4 w-96 h-96 bg-indigo-200 rounded-full mix-blend-multiply filter blur-3xl opacity-30 animate-blob"></div>
                <div className="absolute top-0 right-1/4 w-96 h-96 bg-purple-200 rounded-full mix-blend-multiply filter blur-3xl opacity-30 animate-blob animation-delay-2000"></div>
                <div className="absolute -bottom-32 left-1/3 w-96 h-96 bg-pink-200 rounded-full mix-blend-multiply filter blur-3xl opacity-30 animate-blob animation-delay-4000"></div>
            </div>

            <div className="relative z-10 sm:mx-auto sm:w-full sm:max-w-md">
                <Link to="/" className="flex justify-center mb-6">
                   <div className="h-12 w-12 rounded-xl bg-indigo-600 flex items-center justify-center shadow-lg">
                        <span className="text-white font-bold text-2xl">CV</span>
                   </div>
                </Link>
                <Outlet />
            </div>
        </div>
    );
};

// Helper for 'Link' not defined above, fix by importing or just using text.
// Actually AuthLayout wrapping outlet is better.
import { Link } from 'react-router-dom';

export default AuthLayout;
