import {
	FETCH_USERS_PROGRESS,
	FETCH_USERS_FINISHED
} from '../actions/action-types';

let initialState = {
	inProgress: false,
	err: null,
	users:{}
};

export default function updateContext(state = initialState, action = {}) {
	let users = state.users;
	switch (action.type) {
		case FETCH_USERS_PROGRESS:
			return {
				inProgress: true,
				err: null,
				users: users
			};
		case FETCH_USERS_FINISHED:
			const { res } = action;
			if (res && res.body) {
				res.body.forEach(user => {
					users[user.username] = user;
				});
			}
			return {
				err: action.err,
				inProgress:false,
				users: users
			};
		default:
			return state;
	}
}
