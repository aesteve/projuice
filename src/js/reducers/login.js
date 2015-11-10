import {
	LOGIN_PROGRESS,
	LOGIN_FINISHED
} from './action-types';
import { setCookie } from '../io/cookies';

const initialState = {
	accessToken: null,
	loginProgress: false,
	loginError: null
};

export default function updateContext(state = initialState, action = {}) {
	switch(action.type) {
		case LOGIN_PROGRESS:
			return {
				...state,
				loginProgress: true,
				loginError: false,
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
				...state,
				loginProgress: false,
				loginError: action.err,
				accessToken: accessToken
			};
		default:
			return state;
	}
};
