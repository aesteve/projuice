import ApiClient from '../io/api';
import {
	FETCH_INFO_PROGRESS,
	FETCH_INFO_FINISHED,
	UNAUTHORIZED
} from './action-types';
const apiClient = new ApiClient();

function dispatchApiResult(dispatch, type) {
	return (err, res) => {
		if (err && err.status === 401) {
			dispatch({
				type:UNAUTHORIZED,
				value: true
			});
		} else if (!err) {
			dispatch({
				type:UNAUTHORIZED,
				value: false
			});
		}
		dispatch({
			type: type,
			err: err,
			res: res
		});
	}
}

export function fetchMyInfos() {
	return (dispatch, getState) => {
		dispatch({
			type: FETCH_INFO_PROGRESS
		});
		apiClient.get('/users/me', dispatchApiResult(dispatch, FETCH_INFO_FINISHED));
	};
}


export function fetchUsers() {
	return (dispatch, getState) => {
		dispatch({
			type: FETCH_USERS_PROGRESS
		});
		apiClient.get('/users', dispatchApiResult(dispatch, FETCH_USERS_FINISHED));
	};
}
