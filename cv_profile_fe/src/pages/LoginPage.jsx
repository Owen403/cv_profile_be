import { useState, useContext } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import { Lock, Mail, ArrowRight } from 'lucide-react';
import Input from '../components/common/Input';
import Button from '../components/common/Button';
import Card from '../components/common/Card';

const LoginPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);
    try {
      await login(email, password);
      navigate('/');
    } catch (err) {
        const msg = err.response?.data?.message || 'Failed to login. Please check your credentials.';
        setError(msg);
    } finally {
        setIsLoading(false);
    }
  };

  return (
    <Card className="w-full bg-white/80 backdrop-blur-sm shadow-xl border-0">
      <div className="text-center mb-8">
        <h2 className="text-2xl font-bold tracking-tight text-slate-900">Welcome Back</h2>
        <p className="mt-2 text-sm text-slate-600">Enter your credentials to access your account</p>
      </div>

      <form className="space-y-6" onSubmit={handleSubmit}>
        <Input
            label="Email Address"
            id="email"
            type="email"
            icon={Mail}
            placeholder="name@example.com"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
        />

        <Input
            label="Password"
            id="password"
            type="password"
            icon={Lock}
            placeholder="••••••••"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
        />

        <div className="flex items-center justify-between">
            <div className="flex items-center">
                <input id="remember-me" name="remember-me" type="checkbox" className="h-4 w-4 rounded border-gray-300 text-indigo-600 focus:ring-indigo-600" />
                <label htmlFor="remember-me" className="ml-2 block text-sm text-gray-900">Remember me</label>
            </div>
            <div className="text-sm">
                <a href="#" className="font-medium text-indigo-600 hover:text-indigo-500">Forgot password?</a>
            </div>
        </div>

        {error && (
            <div className="p-3 rounded-lg bg-red-50 border border-red-200 text-sm text-red-600 text-center">
                {error}
            </div>
        )}

        <Button type="submit" variant="primary" className="w-full" isLoading={isLoading}>
            Sign in
            {!isLoading && <ArrowRight className="ml-2 h-4 w-4" />}
        </Button>

        <div className="relative my-6">
            <div className="absolute inset-0 flex items-center">
                <div className="w-full border-t border-gray-300"></div>
            </div>
            <div className="relative flex justify-center text-sm">
                <span className="bg-white px-2 text-gray-500">Or continue with</span>
            </div>
        </div>

        <div className="text-center text-sm text-gray-600">
            Don't have an account?{' '}
            <Link to="/register" className="font-medium text-indigo-600 hover:text-indigo-500 hover:underline">
                Create an account
            </Link>
        </div>
      </form>
    </Card>
  );
};

export default LoginPage;
