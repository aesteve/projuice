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
		.end(callback);
}
