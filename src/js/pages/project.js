import React, { Component, PropTypes } from 'react';
import Loader from '../components/loader';
import Issues from '../components/issues';
import TeamMembers from '../components/team-members';
import ApiClient from '../io/api';
import RequestStatus from '../components/request-status';
import { injectResponseAsState } from '../components/request-utils';

export default class Project extends Component {

	constructor(props) {
		super(props);
		this.state = {
			status: null
		};
		this.apiClient = new ApiClient(this.props.history);
	}

	componentDidMount() {
		this.setState({
			status: 'pending'
		});
		this.apiClient.get('/projects/' + this.props.params.projectId + '/', injectResponseAsState(this, 'project'));
	}

	render() {
		const { project } = this.state;
		return (
			<div>
				<RequestStatus {...this.state} />
				{project &&
					<div>
						<h1>{project.name}</h1>
						<div className="project-description">
							{project.description}
						</div>
						<div className="project-infos">
							<Issues project={project} />
							<TeamMembers project={project} />
						</div>
					</div>
				}
			</div>
		);
	}
}

Project.contextTypes = {history: PropTypes.history};
