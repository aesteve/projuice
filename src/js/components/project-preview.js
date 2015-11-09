import React, { Component } from 'react';
import { Link } from 'react-router';

export default class ProjectPreview extends Component {
	render() {
		const {project} = this.props;
		const projectLink = '/projects/' + project.id;
		return (
			<div className="project-preview">
				<div className="name">
					<Link to={projectLink}>{project.name}</Link>
				</div>
				<div className="description">
					{project.description}
				</div>
			</div>
		);
	}
}
