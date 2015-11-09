import request from 'superagent';
import prefix from 'superagent-prefix';
import { getCookie } from './cookies';

const thePrefix = prefix('/api/1');

export function post(path, data, callback) {
	return request
		.post(path)
		.use(thePrefix)
		.type('json')
		.accept('json')
		.send(data)
		.end(callback);
}

export function get(path, callback) {
	return request
		.get(path)
		.use(thePrefix)
		.type('application/json')
		.accept('application/json')
		.set({'Authorization': 'token ' + getCookie('access_token')})
		.end(handle401(callback));
}

const handle401 = callback => {
	return (err, res) => {
		if (err && err.status === 401) {
			document.location = '/login';
		} else {
			callback(err, res);
		}
	};
} 
