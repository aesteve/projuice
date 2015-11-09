import React, { Component } from 'react';

export default class ProjectPreview extends Component {
	render() {
		const {project} = this.props;
		const projectLink = '/projects/' + project.id;
		return (
			<div className="project-preview">
				<div className="name">
					<a href={projectLink}>{project.name}</a>
				</div>
				<div className="description">
					{project.description}
				</div>
			</div>
		);
	}
}