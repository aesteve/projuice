import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import { get } from '../io/api';
import Error from '../components/display-error';
import ProjectPreview from '../components/project-preview';

class ProjectList extends Component {
	render() {
		if (!this.props.projects || this.props.projects.length === 0) {
			return null;
		}
		const projects = this.props.projects.map(project => {
			return <ProjectPreview key={project.id} project={project} />;
		});
		return (
			<div>
				<h3>Your projects</h3>
				<div className="project-list">
					{projects}
				</div>
			</div>
		);
	}
}

export default class IndexPage extends Component {
	
	constructor(props) {
		super(props);
		this.state = {
			error: null,
			projects: []
		};
	}
	
	componentDidMount() {
		get('/projects', (err, res) => {
			if (!err) {
				this.setState({
					projects: res.body
				});
			} else {
				this.setState({
					error: err
				});
			}
		});
	}
	
	
	render() {
		return (
			<div>
				{this.state.error && <Error error={err} />}
				<ProjectList projects={this.state.projects} />
			</div>
		);
	}
}

ReactDOM.render(<IndexPage />, document.querySelector("#index-page"));