import React, { Component } from 'react';
import { connect } from 'react-redux';
import { fetchIssues } from '../actions/issues';
const mapStateToProps = state => {
	return {
		issues: state.issues
	};
}

class Issues extends Component {

	componentDidMount() {
		const { project } = this.props;
		const { issues } = this.props.issues.issues;
		if (!issues || !issues[project.id]) {
			this.props.dispatch(fetchIssues(project.id));
		}
	}

	render() {
		return (
			<div className="issues">
			</div>
		);
	}
}

export default connect(mapStateToProps)(Issues);
