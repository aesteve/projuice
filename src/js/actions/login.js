import ApiClient from '../io/api';
import {
	LOGIN_PROGRESS,
	LOGIN_FINISHED
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

export function login(credentials) {
	return (dispatch, getState) => {
		dispatch({
			type: LOGIN_PROGRESS
		});
		apiClient.login(credentials, dispatchApiResult(LOGIN_FINISHED, dispatch));
	};
}
