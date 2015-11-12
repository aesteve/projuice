import ApiClient from '../io/api';
import {
	FETCH_PROJECTS_PROGRESS,
	FETCH_PROJECTS_FINISHED
} from '../actions/action-types';

let initialState = {
	inProgress: false,
	err: null,
	projects:{}
};

export default function updateContext(state = initialState, action = {}) {
	let projects = state.projects;
	switch (action.type) {
		case FETCH_PROJECTS_PROGRESS:
			return {
				inProgress: true,
				err: null,
				projects: projects
			};
		case FETCH_PROJECTS_FINISHED:
			const { res } = action;
			if (res && res.body) {
				res.body.forEach(project => {
					projects[project.id] = project;
				});
			}
			return {
				err: action.err,
				inProgress:false,
				projects: projects
			};
		default:
			return state;
	}
}
