import _ from 'lodash';
import React, { Component } from 'react';
import { PropTypes } from 'react-router';
import { connect } from 'react-redux';
import { protect } from '../utils/composition';
import { fetchProjects } from '../actions/projects';
// sub-components
import ProjectPreview from '../components/project-preview';
import RequestStatus from '../components/request-status';

const mapStateToProps = state => {
	return {
		projects: state.projects
	};
};

export default class Projects extends Component {

	componentDidMount() {
		this.props.dispatch(fetchProjects());
	}

	render() {
		const { projects } = this.props;
		if (!projects.projects) {
			return null;
		}
		const projs = projects.projects;
		const projectsJSX = _.map(projs, (project, projectId) => {
			return <ProjectPreview key={projectId} project={project} />;
		});
		return (
			<div>
				<RequestStatus {...this.state} />
				{projectsJSX}
			</div>
		);
	}
}

Projects.contextTypes = {history: PropTypes.history};

export default protect(connect(mapStateToProps)(Projects));
