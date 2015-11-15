import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { fetchIssues } from '../actions/issues';
import RequestStatus from './request-status';

const mapStateToProps = state => {
	return {
		issues: state.issues
	};
}

class Issue extends Component {
	render() {
		const { issue } = this.props;
		return (
			<div className="issue">
				<div className="issue-header">{issue.name}</div>
				<div className="issue-content">{issue.description}</div>
			</div>
		);
	}
}

class Issues extends Component {

	componentDidMount() {
		const { project } = this.props;
		const { issues } = this.props.issues;
		if (!issues || !issues[project.id]) {
			this.props.dispatch(fetchIssues(project.id));
		}
	}

	render() {
		const { issues, project } = this.props;
		const issuesById = issues.issues[project.id];
		let issuesJSX;
		if (issuesById) {
			issuesJSX = _.map(issuesById, (issue, issueId) => {
				return <Issue key={issueId} issue={issue} />
			});
		}
		return (
			<div className="issues block">
				<RequestStatus {...issues} />
				{issuesJSX}
			</div>
		);
	}
}

export default connect(mapStateToProps)(Issues);
