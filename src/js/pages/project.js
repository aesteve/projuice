import React, { Component } from 'react';
import { connect } from 'react-redux';
import { protect } from '../utils/composition';
import { fetchProjects } from '../actions/projects';
// sub-components
import Loader from '../components/loader';
import Issues from '../components/issues';
import TeamMembers from '../components/team-members';
import RequestStatus from '../components/request-status';

const mapStateToProps = state => {
	return {
		projects: state.projects
	};
}

class Project extends Component {

	componentDidMount() {
		const { projectId } = this.props.params;
		if (!this.props.projects.projects[projectId]) {
			this.props.dispatch(fetchProjects()); // FIXME : fetch a single one
		}
	}

	render() {
		const { projects } = this.props;
		const { projectId } = this.props.params;
		const proj = projects.projects[projectId];
		return (
			<div>
				<RequestStatus {...projects} />
				{proj &&
					<div className="project block">
						<h1>{proj.name}</h1>
						<div className="row block project-description">
							{proj.description}
						</div>
						<div className="project-infos row">
							<div className="large-6 columns"><Issues project={proj} /></div>
							<div className="large-6 columns"><TeamMembers project={proj} /></div>
						</div>
					</div>
				}
			</div>
		);
	}
}

export default protect(connect(mapStateToProps)(Project));
