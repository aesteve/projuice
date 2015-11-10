import {
	UNAUTHORIZED
} from './action-types';

const initialState = {
	authorized: true
};

export default function updateContext(state = initialState, action = {}) {
	switch(action.type) {
		case UNAUTHORIZED:
			return {
				authorized: !action.value
			};
		default:
			return state;
	}
};
