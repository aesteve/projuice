import {
	LOGIN_PROGRESS,
	LOGIN_FINISHED,
	LOGOUT_PROGRESS,
	LOGOUT_FINISHED
} from '../actions/action-types';
import { setCookie } from '../io/cookies';

const initialState = {
	accessToken: null,
	inProgress: false,
	err: null
};

export default function updateContext(state = initialState, action = {}) {
	switch(action.type) {
		case LOGIN_PROGRESS:
		case LOGOUT_PROGRESS:
			return {
				inProgress: true,
				err: null,
				accessToken: null
			};
		case LOGIN_FINISHED:
			const res = action.res;
			const body = res ? res.body : null;
			const accessToken = body ? body['access_token'] : null;
			if (accessToken) {
				setCookie('access_token', accessToken)
			}
			return {
				inProgress: false,
				err: action.err,
				accessToken: accessToken
			};
		case LOGOUT_FINISHED:
			return {
				inProgress: false,
				err: action.err,
				accessToken: null
			};
		default:
			return state;
	}
};
