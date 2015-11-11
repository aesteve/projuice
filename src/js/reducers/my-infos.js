import {
	FETCH_INFO_PROGRESS,
	FETCH_INFO_FINISHED
} from '../actions/action-types';

const initialState = {
	inProgress: false,
	infos: null
};


export default function updateContext(state = initialState, action = {}) {
	switch(action.type) {
		case FETCH_INFO_PROGRESS:
			return {
				inProgress: true,
				infos: null
			};
		case FETCH_INFO_FINISHED:
			const res = action.res;
			const body = res ? res.body : null;
			return {
				inProgress: false,
				infos: body
			};
		default:
			return state;
	}
};
