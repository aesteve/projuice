import {
	FETCH_PROJECTS_PROGRESS,
	FETCH_PROJECTS_FINISHED
} from './action-types';
import ApiClient from '../io/api';
import { dispatchApiResult } from '../utils/redux-utils';

const apiClient = new ApiClient();

export function fetchProjects() {
	return (dispatch, getState) => {
		dispatch({
			type: FETCH_PROJECTS_PROGRESS
		});
		apiClient.get('/projects', dispatchApiResult(FETCH_PROJECTS_FINISHED, dispatch));
	};
}

export function fetchProject(projectId) {
	return (dispatch, getState) => {
		dispatch({
			type: FETCH_PROJECTS_PROGRESS
		});
		apiClient.get('/projects/' + projectId, dispatchApiResult(FETCH_PROJECTS_FINISHED, dispatch));
	};
}
