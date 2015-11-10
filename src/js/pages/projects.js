import React, { Component, PropTypes } from 'react';
import { get } from '../io/api';
import ProjectPreview from '../components/project-preview';
import RequestStatus from '../components/request-status';
import ApiClient from '../io/api';
import { injectResponseAsState } from '../components/request-utils';

export default class Projects extends Component {

	constructor(props) {
		super(props);
		this.apiClient = new ApiClient(this.props.history);
		this.state = {
			requests:this.apiClient.requests
		};
	}

	componentDidMount() {
		this.apiClient.get('/projects', injectResponseAsState(this, 'projects'));
	}

	render() {
		let projectsJSX;
		const { projects } = this.state;
		if (projects) {
			projectsJSX = projects.map(project => {
				return <ProjectPreview key={project} project={project} />;
			});
		}
		return (
			<div>
				<RequestStatus {...this.state} />
				{projectsJSX}
			</div>
		);
	}
}

Projects.contextTypes = {history: PropTypes.history};
