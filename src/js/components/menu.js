import React, { Component } from 'react';
import { PropTypes } from 'react-router';
import { Link } from 'react-router';
import { logout } from '../actions/login';
import { connect } from 'react-redux';

const mapStateToProps = state => {
	return {
		user: state.myInfos
	};
};

class CredentialsSection extends Component {

	logout() {
		this.props.dispatch(logout());
	}

	render() {
		const { user } = this.props;
		const { infos } = user;
		if (infos) {
			return (
				<ul className="right">
					<li><Link to="/profile">Hello {infos.username}</Link></li>
					<li><a href="javascript:void(0);" onClick={this.logout.bind(this)}>Log out</a></li>
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

class Menu extends Component {
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
						<CredentialsSection {...this.props} />
						<ul className="left">
							<li><Link to="/projects" activeStyle={{background: "#008cba"}}>Projects</Link></li>
						</ul>
					</section>
				</nav>
			</div>
		);
	}
}

Menu.contextTypes = {history: PropTypes.history};

export default connect(mapStateToProps)(Menu);
