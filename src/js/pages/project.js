import React, { Component } from 'react';
import Loader from '../components/loader';
import Issues from '../components/issues';
import TeamMembers from '../components/team-members';
import { get } from '../io/api';
import Error from '../components/display-error';

export default class Project extends Component {

	constructor(props) {
		super(props);
		this.state = {};
	}

	componentDidMount() {
		get('/projects/' + this.props.params.projectId + '/', (err, res) => {
			if (err) {
				this.setState({
					pending: false,
					error: err
				});
			} else {
				this.setState({
					pending: false,
					project: res.body
				});
			}
		});
	}

	render() {
		const { project, pending, error } = this.state;
		return (
			<div>
				{error && <Error error={error} />}
				{pending && <Loader />}
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
