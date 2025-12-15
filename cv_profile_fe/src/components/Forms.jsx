import React, { useState } from "react";
import Input from "./common/Input";
import Button from "./common/Button";

export const ProjectForm = ({ onSubmit, isLoading }) => {
	const [formData, setFormData] = useState({
		title: "",
		description: "",
		role: "",
		technologies: "",
		projectUrl: "",
		githubUrl: "",
		startDate: "",
		endDate: "",
	});

	const handleChange = (e) => setFormData({ ...formData, [e.target.id]: e.target.value });

	return (
		<form
			onSubmit={(e) => {
				e.preventDefault();
				onSubmit(formData);
			}}
			className="space-y-4"
		>
			<Input id="title" label="Project Title" value={formData.title} onChange={handleChange} required />
			<Input id="role" label="Your Role" value={formData.role} onChange={handleChange} required />
			<div className="space-y-1">
				<label className="block text-sm font-medium text-gray-700">Description</label>
				<textarea
					id="description"
					rows={3}
					className="block w-full rounded-lg border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2"
					value={formData.description}
					onChange={handleChange}
				/>
			</div>
			<Input
				id="technologies"
				label="Technologies (comma separated)"
				value={formData.technologies}
				onChange={handleChange}
			/>
			<div className="grid grid-cols-2 gap-4">
				<Input
					id="startDate"
					type="date"
					label="Start Date"
					value={formData.startDate}
					onChange={handleChange}
				/>
				<Input id="endDate" type="date" label="End Date" value={formData.endDate} onChange={handleChange} />
			</div>
			<div className="grid grid-cols-2 gap-4">
				<Input id="projectUrl" label="Project URL" value={formData.projectUrl} onChange={handleChange} />
				<Input id="githubUrl" label="GitHub URL" value={formData.githubUrl} onChange={handleChange} />
			</div>
			<Button type="submit" className="w-full" isLoading={isLoading}>
				Save Project
			</Button>
		</form>
	);
};

export const SkillForm = ({ onSubmit, isLoading }) => {
	const [formData, setFormData] = useState({ name: "", category: "", proficiencyLevel: 3 });

	const handleChange = (e) => {
		const value = e.target.id === "proficiencyLevel" ? parseInt(e.target.value) : e.target.value;
		setFormData({ ...formData, [e.target.id]: value });
	};

	const levelLabels = ["", "Beginner", "Intermediate", "Advanced", "Expert", "Master"];

	return (
		<form
			onSubmit={(e) => {
				e.preventDefault();
				onSubmit(formData);
			}}
			className="space-y-4"
		>
			<Input id="name" label="Skill Name" value={formData.name} onChange={handleChange} required />
			<Input
				id="category"
				label="Category (e.g., Frontend, Backend)"
				value={formData.category}
				onChange={handleChange}
				required
			/>
			<div className="space-y-1">
				<label className="block text-sm font-medium text-gray-700">
					Proficiency: {levelLabels[formData.proficiencyLevel] || "Unknown"}
				</label>
				<input
					id="proficiencyLevel"
					type="range"
					min="1"
					max="5"
					className="w-full h-2 bg-gray-200 rounded-lg appearance-none cursor-pointer"
					value={formData.proficiencyLevel}
					onChange={handleChange}
				/>
				<div className="flex justify-between text-xs text-gray-500">
					<span>Beginner</span>
					<span>Intermediate</span>
					<span>Advanced</span>
					<span>Expert</span>
					<span>Master</span>
				</div>
			</div>
			<Button type="submit" className="w-full" isLoading={isLoading}>
				Save Skill
			</Button>
		</form>
	);
};

export const EventForm = ({ onSubmit, isLoading }) => {
	const [formData, setFormData] = useState({
		name: "",
		eventDate: "",
		location: "",
		description: "",
		role: "",
		organizer: "",
	});
	const handleChange = (e) => setFormData({ ...formData, [e.target.id]: e.target.value });

	return (
		<form
			onSubmit={(e) => {
				e.preventDefault();
				onSubmit(formData);
			}}
			className="space-y-4"
		>
			<Input id="name" label="Event Name" value={formData.name} onChange={handleChange} required />
			<Input
				id="eventDate"
				type="date"
				label="Date"
				value={formData.eventDate}
				onChange={handleChange}
				required
			/>
			<Input id="location" label="Location" value={formData.location} onChange={handleChange} />
			<Input id="role" label="Your Role" value={formData.role} onChange={handleChange} />
			<div className="space-y-1">
				<label className="block text-sm font-medium text-gray-700">Description</label>
				<textarea
					id="description"
					rows={3}
					className="block w-full rounded-lg border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2"
					value={formData.description}
					onChange={handleChange}
				/>
			</div>
			<Button type="submit" className="w-full" isLoading={isLoading}>
				Save Event
			</Button>
		</form>
	);
};

export const PublicationForm = ({ onSubmit, isLoading }) => {
	const [formData, setFormData] = useState({ title: "", authors: "", journal: "", publishDate: "", url: "" });
	const handleChange = (e) => setFormData({ ...formData, [e.target.id]: e.target.value });

	return (
		<form
			onSubmit={(e) => {
				e.preventDefault();
				onSubmit(formData);
			}}
			className="space-y-4"
		>
			<Input id="title" label="Title" value={formData.title} onChange={handleChange} required />
			<Input id="journal" label="Journal/Publisher" value={formData.journal} onChange={handleChange} />
			<Input id="authors" label="Authors" value={formData.authors} onChange={handleChange} />
			<Input
				id="publishDate"
				type="date"
				label="Publish Date"
				value={formData.publishDate}
				onChange={handleChange}
			/>
			<Input id="url" label="Link (URL)" value={formData.url} onChange={handleChange} />
			<Button type="submit" className="w-full" isLoading={isLoading}>
				Save Publication
			</Button>
		</form>
	);
};
