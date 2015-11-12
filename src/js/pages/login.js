import React, { Component } from 'react';
import { PropTypes } from 'react-router';
import StateButton from '../components/state-btn';
import RequestStatus from '../components/request-status';
import { connect } from 'react-redux';
import { login } from '../actions/login';

const mapStateToProps = state => {
	return {
		loginState: state.login
	};
};

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

	componentWillReceiveProps(newProps) {
		const { loginState } = newProps;
		if (loginState && !loginState.inProgress && loginState.accessToken) {
			// login success
			this.context.history.pushState(null, '/');
		}
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
		this.props.dispatch(login(credentials));
	}

	render() {
		return (
			<div id="login-form">
				<RequestStatus {...this.props.auth} />
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
							<td><button style={{width: '100%'}} id="login-btn" onClick={this.login}>Log in</button></td>
						</tr>
					</tbody>
				</table>
			</div>
		);
	}
}

LoginForm.contextTypes = {history: PropTypes.history};

export default connect(mapStateToProps)(LoginForm);
