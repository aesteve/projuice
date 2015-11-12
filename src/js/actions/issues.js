import {
	FETCH_ISSUES_PROGRESS,
	FETCH_ISSUES_FINISHED
} from './action-types';
import ApiClient from '../io/api';
import { dispatchApiResult } from '../utils/redux-utils';

const apiClient = new ApiClient();

export function fetchIssues(projectId) {
	return (dispatch, getState) => {
		dispatch({
			type: FETCH_ISSUES_PROGRESS,
			projectId: projectId
		});
		apiClient.get('/projects/' + projectId + '/issues', dispatchApiResult(FETCH_ISSUES_FINISHED, dispatch, {projectId:projectId}));
	};
}
