import { useState, useContext } from "react";
import { useNavigate, Link } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import { Lock, Mail, User, ArrowRight } from "lucide-react";
import Input from "../components/common/Input";
import Button from "../components/common/Button";
import Card from "../components/common/Card";

const RegisterPage = () => {
	const [email, setEmail] = useState("");
	const [password, setPassword] = useState("");
	const [fullName, setFullName] = useState("");
	const [error, setError] = useState("");
	const [isLoading, setIsLoading] = useState(false);
	const { register } = useContext(AuthContext);
	const navigate = useNavigate();

	const handleSubmit = async (e) => {
		e.preventDefault();
		setError("");
		setIsLoading(true);
		try {
			await register(email, password, fullName);
			navigate("/login");
		} catch (err) {
			const msg = err.response?.data?.message || "Failed to register. Please try again.";
			setError(msg);
		} finally {
			setIsLoading(false);
		}
	};

	return (
		<Card className="w-full bg-white/80 backdrop-blur-sm shadow-xl border-0">
			<div className="text-center mb-8">
				<h2 className="text-2xl font-bold tracking-tight text-slate-900">Create Account</h2>
				<p className="mt-2 text-sm text-slate-600">Join us to manage your professional profile</p>
			</div>

			<form className="space-y-6" onSubmit={handleSubmit}>
				<Input
					label="Full Name"
					id="fullName"
					type="text"
					icon={User}
					placeholder="John Doe"
					value={fullName}
					onChange={(e) => setFullName(e.target.value)}
					required
				/>

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

				{error && (
					<div className="p-3 rounded-lg bg-red-50 border border-red-200 text-sm text-red-600 text-center">
						{error}
					</div>
				)}

				<Button type="submit" variant="primary" className="w-full" isLoading={isLoading}>
					Create Account
					{!isLoading && <ArrowRight className="ml-2 h-4 w-4" />}
				</Button>

				<div className="text-center text-sm text-gray-600">
					Already have an account?{" "}
					<Link
						to="/login"
						className="font-medium text-indigo-600 hover:text-indigo-500 hover:underline"
					>
						Sign in
					</Link>
				</div>
			</form>
		</Card>
	);
};

export default RegisterPage;
