import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import StateButton from '../components/state-btn';
import { post } from '../io/api';
import { getCookie, setCookie } from '../io/cookies';

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
		post('/login', credentials, (err, res) => {
			if (err) {
				this.setState({
					status: 'error',
					error: err
				});
			} else {
				setCookie("access_token", res.body.access_token);
				document.location = "/index";
			}
		});
	}

	render() {
		const error = this.state.status === 'error' ? this.state.error : '';
		return (
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
						<td>{error}</td>
						<td><button id="login-btn" onClick={this.login}>Log in</button></td>
					</tr>
				</tbody>
			</table>
		);
	}
}

ReactDOM.render(<LoginForm />, document.querySelector("#login-form"));