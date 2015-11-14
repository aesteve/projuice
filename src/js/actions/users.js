import {
	FETCH_INFO_PROGRESS,
	FETCH_INFO_FINISHED,
	FETCH_USERS_PROGRESS,
	FETCH_USERS_FINISHED,
	UNAUTHORIZED
} from './action-types';
import { dispatchApiResult } from '../utils/redux-utils';
import ApiClient from '../io/api';

const apiClient = new ApiClient();

export function fetchMyInfos() {
	return (dispatch, getState) => {
		dispatch({
			type: FETCH_INFO_PROGRESS
		});
		apiClient.get('/users/me', dispatchApiResult(FETCH_INFO_FINISHED, dispatch));
	};
}

export function fetchUsers() {
	return (dispatch, getState) => {
		dispatch({
			type: FETCH_USERS_PROGRESS
		});
		apiClient.get('/users', dispatchApiResult(FETCH_USERS_FINISHED, dispatch));
	};
}
