import React, { Component } from 'react';
import { Link } from'react-router';

class CredentialsSection extends Component {

	constructor(props) {
		super(props);
		this.logout.bind(this);
	}

	logout() {
		console.error("NYI");
	}

	render() {
		const { user } = this.props;
		if (user) {
			return (
				<ul className="right">
					<li><Link to="/profile">Hello {user.username}</Link></li>
					<li><a href="javascript:void(0);" onClick={this.logout}>Log out</a></li>
				</ul>
			);
		} else {
			return (
				<ul className="right">
					<li><Link to="login">Log in</Link></li>
					<li><Link to="register">Sign up</Link></li>
				</ul>
			);
		}
	}
}

export default class Menu extends Component {
	render() {
		return (
			<div id="main-menu">
				<nav className="top-bar" data-topbar role="navigation">
					<ul className="title-area">
						<li className="name">
							<h1><Link to="/">Projuice</Link></h1>
						</li>
					</ul>
					<section className="top-bar-section">
						<CredentialsSection user={this.props.user} />
						<ul className="left">
							<li><Link to="/projects" activeStyle={{background: "#008cba"}}>Projects</Link></li>
						</ul>
					</section>
				</nav>
			</div>
		);
	}
}
