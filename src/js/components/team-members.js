import React, { Component } from 'react';
import { connect } from 'react-redux';
import { fetchUsers } from '../actions/users';
import _ from 'lodash';
import RequestStatus from './request-status';

const mapStateToProps = state => {
	return {
		users: state.users
	};
};

class UserRole extends Component {
	render() {
		const { user, role } = this.props;
		return (
			<div className="user-role">
				<div className="username">{user.username}</div>
				<div className="role">{role}</div>
			</div>
		);
	}
}

class TeamMembers extends Component {

	componentDidMount() {
		this.props.dispatch(fetchUsers());
	}

	render() {
		const { users, project } = this.props;
		const members = project.participants;
		let usersJSX = [];
		if (users.users && members && members.length > 0) {
				members.forEach(member => {
					const username = member.username;
					const user = users.users[username];
					if (user) {
						usersJSX.push(<UserRole key={username} user={users.users[username]} role={member.role} />)
					}
				});
		}
		return (
			<div>
				<RequestStatus {...users} />
				{usersJSX}
			</div>
		);
	}
}

export default connect(mapStateToProps)(TeamMembers);
