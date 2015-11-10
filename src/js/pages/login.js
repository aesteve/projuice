import React, { Component } from 'react';
import { PropTypes } from 'react-router';
import ReactDOM from 'react-dom';
import StateButton from '../components/state-btn';
import ApiClient from '../io/api';
import { getCookie, setCookie } from '../io/cookies';
import RequestStatus from '../components/request-status';

export default class LoginForm extends Component {

	constructor(props) {
		super(props);
		this.state = {
			username:'',
			password:'',
			status: null
		};
		this.setUsername = this.setUsername.bind(this);
		this.setPassword = this.setPassword.bind(this);
		this.login = this.login.bind(this);
		this.apiClient = new ApiClient(this.props.history);
	}

	setUsername(e) {
		this.setState({
			username: e.target.value
		});
	}

	setPassword(e) {
		this.setState({
			password: e.target.value
		});
	}

	login() {
		const credentials = {
			username: this.state.username,
			password: this.state.password
		};
		this.setState({
			status: 'pending'
		});
		this.apiClient.login(credentials, (err, res) => {
			if (err) {
				this.setState({
					status: 'error',
					error: err
				});
			} else {
				setCookie("access_token", res.body.access_token);
				this.setState({
					pending: 'done',
					error: null
				});
				this.props.history.pushState(null, '/projects');
			}
		});
	}

	render() {
		return (
			<div id="login-form">
				<RequestStatus {...this.state} />
				<table>
					<tbody>
						<tr>
							<td>Username:</td>
							<td><input type="text" onChange={this.setUsername} value={this.state.username} /></td>
						</tr>
						<tr>
							<td>Password:</td>
							<td><input type="password" onChange={this.setPassword} value={this.state.password} /></td>
						</tr>
						<tr>
							<td></td>
							<td><button id="login-btn" onClick={this.login}>Log in</button></td>
						</tr>
					</tbody>
				</table>
			</div>
		);
	}
}

LoginForm.contextTypes = {history: PropTypes.history};
