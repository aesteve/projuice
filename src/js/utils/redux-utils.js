import { UNAUTHORIZED } from '../actions/action-types';
import { createStore, applyMiddleware, compose } from 'redux';
import thunk from 'redux-thunk';
import reducers from '../reducers/index';

export function createStoreWithMiddleware(someReducers) {
	return compose(applyMiddleware(thunk))(createStore)(someReducers);
}

export const store = createStoreWithMiddleware(reducers);

export function dispatchApiResult(type, dispatch) {
	return (err, res) => {
		dispatchWith401(type, err, res, dispatch);
	}
}

export function dispatchApiResults(types, dispatch) {
	return (err, res) => {
		types.forEach(type => {
			dispatchWith401(type, err, res, dispatch);
		});
	};
}

function dispatchWith401(type, err, res, dispatch) {
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
		err:err,
		res:res
	});
}
