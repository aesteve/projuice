import React, { Component, PropTypes } from 'react';
import ProjectPreview from '../components/project-preview';
import RequestStatus from '../components/request-status';
import { connect } from 'react-redux';
import { privatePage } from '../utils/composition';
import { fetchProjects } from '../actions/projects';
import _ from 'lodash';

const mapStateToProps = state => {
	return {
		projects: state.projects,
		user: state.myInfos,
		tokenStatus: state.tokenStatus
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
		const proj = projects.projects;
		const projectsJSX = _.map(proj, (projectId, project) => {
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

export default connect(mapStateToProps)(privatePage(Projects));
