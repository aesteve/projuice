import ApiClient from '../io/api';
import {
	FETCH_ISSUES_PROGRESS,
	FETCH_ISSUES_FINISHED
} from '../actions/action-types';

let initialState = {
	inProgress: false,
	err: null,
	issues:{}
};

export default function updateContext(state = initialState, action = {}) {
	let { issues } = state;
	switch (action.type) {
		case FETCH_ISSUES_PROGRESS:
			return {
				inProgress: true,
				err: null,
				issues: issues
			};
		case FETCH_ISSUES_FINISHED:
			const { res, projectId } = action;
			if (res && res.body) {
				const projectIssues = issues[projectId] || {};
				res.body.forEach(issue => {
					projectIssues[issue.id] = issue;
				});
				issues[projectId] = projectIssues;
			}
			return {
				err: action.err,
				inProgress:false,
				issues: issues
			};
		default:
			return state;
	}
}
