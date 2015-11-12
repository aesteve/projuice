import {
	LOGIN_PROGRESS,
	LOGIN_FINISHED,
	LOGOUT_PROGRESS,
	LOGOUT_FINISHED,
	UNAUTHORIZED
} from './action-types';
import { dispatchApiResult } from '../utils/redux-utils';
import ApiClient from '../io/api';

const apiClient = new ApiClient();

export function login(credentials) {
	return (dispatch, getState) => {
		dispatch({
			type: LOGIN_PROGRESS
		});
		apiClient.login(credentials, dispatchApiResult(LOGIN_FINISHED, dispatch));
	};
}

export function logout() {
	return (dispatch, getState) => {
		dispatch({
			type: LOGOUT_PROGRESS
		});
		apiClient.logout((err, res) => {
			dispatch({
				type: LOGOUT_FINISHED,
				err: err,
				res: res
			});
			if (!err && res.status === 204) {
				dispatch({
					type: UNAUTHORIZED,
					value: true
				});
			}
		});
	}
}
