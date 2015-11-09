import React, { Component } from 'react';
import { get } from '../io/api';
import ProjectPreview from '../components/project-preview';
import Loader from '../components/loader';

export default class Projects extends Component {

	constructor(props) {
		super(props);
		this.state = {
			pending: true
		};
	}

	componentDidMount() {
		get('/projects', (err, res) => {
			if (err) {
				this.setState({
					error: err,
					pending: false
				});
			} else {
				this.setState({
					error: null,
					pending: false,
					projects: res.body
				});
			}
		});
	}

	render() {
		let projectsJSX;
		const { pending, error, projects } = this.state;
		if (projects) {
			projectsJSX = projects.map(project => {
				return <ProjectPreview project={project} />;
			});
		}
		return (
			<div>
				{this.state.pending && <Loader />}
				{this.state.error && <Error error={this.state.error} />}
				{projectsJSX}
			</div>
		);
	}
}
