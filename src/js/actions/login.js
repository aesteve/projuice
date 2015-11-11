import ApiClient from '../io/api';
import {
	LOGIN_PROGRESS,
	LOGIN_FINISHED,
	LOGOUT_PROGRESS,
	LOGOUT_FINISHED,
	UNAUTHORIZED
} from './action-types';

const apiClient = new ApiClient();

function dispatchApiResults(types, dispatch) {
	return (err, res) => {
		types.forEach(type => {
			console.log('dispatch ' + type);
			dispatch({
				type: type,
				err:err,
				res:res
			});
		});
	};
}

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

export function logout() {
	return (dispatch, getState) => {
		console.log('logout');
		dispatch({
			type: LOGOUT_PROGRESS
		});
		const cb = (err, res) => {
			dispatch({
				type: LOGOUT_FINISHED,
				err: err,
				res: res
			});
			if (!err && res.status === 204) {
				console.log('logout success');
				dispatch({
					type: UNAUTHORIZED,
					value: true
				});
			}
		};
		apiClient.logout(cb);
	}
}
