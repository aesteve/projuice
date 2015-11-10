import request from 'superagent';
import prefix from 'superagent-prefix';
import { getCookie } from './cookies';

const thePrefix = prefix('/api/1');
const DEFAULT_LOGIN_PAGE = "/login";

export default class ApiClient {

	constructor() {
		this.accessToken = getCookie('access_token');
	}

	login(credentials, callback) {
		return this._doReq('POST', '/login', credentials, callback);
	}

	get(path, callback) {
		return this._doReq('GET', path, null, callback);
	}

	post(path, data, callback) {
		return this._doReq('POST', path, data, callback);
	}

	put(path, data, callback) {
		return this._doReq('PUT', path, data, callback);
	}

	delete(path, data, callback) {
		return this._doReq('DELETE', path, data, callback);
	}

	_doReq(method, path, data, callback, redirect) {
		const token = this.accessToken || getCookie('access_token');
		const req = request(method, path)
				.use(thePrefix)
				.type('application/json')
				.accept('application/json');
		if (token) {
			req.set({'Authorization': 'token ' + token})
		}
		if (data) {
			req.send(data);
		}
		req.end(callback);
		return req;
	}
}
