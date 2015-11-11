import ApiClient from '../io/api';
import {
	FETCH_PROJECTS_PROGRESS,
	FETCH_PROJECTS_FINISHED
} from './action-types';

const apiClient = new ApiClient();

function dispatchApiResult(type, dispatch) {
	return (err, res) => {
		dispatch({
			type: type,
			err:err,
			res:res
		});
	};
}

export function fetchProjects() {
	return (dispatch, getState) => {
		dispatch({
			type: FETCH_PROJECTS_PROGRESS
		});
		apiClient.get('/projects', dispatchApiResult(FETCH_PROJECTS_FINISHED, dispatch));
	};
}
