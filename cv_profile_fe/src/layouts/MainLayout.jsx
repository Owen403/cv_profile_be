import React, { useContext } from 'react';
import { Outlet, Link, useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import { LogOut, User, Download, Menu, X } from 'lucide-react';
import Button from '../components/common/Button';

const MainLayout = () => {
    const { currentUser, logout } = useContext(AuthContext);
    const [isMenuOpen, setIsMenuOpen] = React.useState(false);
    
    // We can assume we have exportPdf from service but usually layout doesn't handle business logic directly like that. 
    // However for header simple action it's fine or pass via props.
    // For now we keep the layout strict to structure.

    return (
        <div className="min-h-screen bg-slate-50 font-sans text-slate-900">
            {/* Top Navigation */}
            <nav className="bg-white/80 backdrop-blur-md sticky top-0 z-50 border-b border-slate-200">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex justify-between h-16">
                        <div className="flex items-center">
                            <Link to="/" className="flex-shrink-0 flex items-center gap-2">
                                <div className="h-8 w-8 rounded-lg bg-indigo-600 flex items-center justify-center">
                                    <span className="text-white font-bold text-sm">CV</span>
                                </div>
                                <span className="font-bold text-xl tracking-tight text-slate-800">Profile<span className="text-indigo-600">App</span></span>
                            </Link>
                        </div>
                        
                        {/* Desktop Menu */}
                        <div className="hidden sm:flex sm:items-center sm:gap-4">
                            <div className="flex items-center gap-2 px-3 py-1 rounded-full bg-slate-100 border border-slate-200">
                                <User size={16} className="text-slate-500" />
                                <span className="text-sm font-medium text-slate-700">{currentUser?.fullName || currentUser?.email}</span>
                            </div>
                            <Button variant="ghost" size="sm" onClick={logout} className="text-slate-500 hover:text-red-600">
                                <LogOut size={18} className="mr-2" />
                                Logout
                            </Button>
                        </div>

                         {/* Mobile menu button */}
                        <div className="flex items-center sm:hidden">
                            <button
                                onClick={() => setIsMenuOpen(!isMenuOpen)}
                                className="p-2 rounded-md text-slate-400 hover:text-slate-500 hover:bg-slate-100 focus:outline-none"
                            >
                                {isMenuOpen ? <X size={24} /> : <Menu size={24} />}
                            </button>
                        </div>
                    </div>
                </div>

                {/* Mobile Menu */}
                {isMenuOpen && (
                    <div className="sm:hidden bg-white border-b border-slate-200">
                        <div className="pt-2 pb-3 space-y-1 px-4">
                            <div className="flex items-center gap-2 py-2 text-slate-600">
                                <User size={18} />
                                <span className="font-medium">{currentUser?.fullName}</span>
                            </div>
                             <button onClick={logout} className="flex w-full items-center gap-2 py-2 text-red-600 font-medium">
                                <LogOut size={18} />
                                Logout
                            </button>
                        </div>
                    </div>
                )}
            </nav>

            {/* Main Content */}
            <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                <Outlet />
            </main>
        </div>
    );
};

export default MainLayout;
