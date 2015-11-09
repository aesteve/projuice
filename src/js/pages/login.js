import React, { Component } from 'react';
import ReactDOM from 'react-dom';

export default class LoginForm extends Component {
	
	constructor(props) {
		super(props);
		this.state = {
			username:'',
			password:''
		};
		this.setUsername = this.setUsername.bind(this);
		this.setPassword = this.setPassword.bind(this);
	}
	
	setUsername(e) {
		this.setState({
			username: e.target.value()
		});
	}

	setPassword(e) {
		this.setState({
			password: e.target.value()
		});
	}
	
	login() {
		console.log(this.state);
	}

	render() {
		return (
			<table>
				<tr>
					<td>Username:</td>
					<td><input type="text" onChange={this.setUsername} /></td>
				</tr>
				<tr>
					<td>Password:</td>
					<td><input type="password" onChange={this.setPassword} /></td>
				</tr>
				<tr>
					<td></td>
					<td><button id="login-btn" onClick={this.login}>Log in</button></td>
				</tr>
			</table>
		);
	}
}

ReactDOM.render(<LoginForm />, document.querySelector("#login-form"));