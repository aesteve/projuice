import { UNAUTHORIZED } from '../actions/action-types';
import { createStore, applyMiddleware, compose } from 'redux';
import thunk from 'redux-thunk';
import reducers from '../reducers/index';

export function createStoreWithMiddleware(someReducers) {
	return compose(applyMiddleware(thunk))(createStore)(someReducers);
}

export const store = createStoreWithMiddleware(reducers);

export function dispatchApiResult(type, dispatch, additional) {
	return (err, res) => {
		dispatchWith401(type, err, res, dispatch, additional);
	}
}

export function dispatchApiResults(types, dispatch, additional) {
	return (err, res) => {
		types.forEach(type => {
			dispatchWith401(type, err, res, dispatch, additional);
		});
	};
}

function dispatchWith401(type, err, res, dispatch, additional) {
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
	let result = additional || {};
	result.type = type;
	result.err = err;
	result.res = res;
	dispatch(result);
}
