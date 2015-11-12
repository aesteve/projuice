import React, { Component } from 'react';
import { protect } from '../utils/composition';
import { connect } from 'react-redux';
import RequestStatus from '../components/request-status';
import { fetchMyInfos } from '../actions/users';

const mapStateToProps = state => {
	return {
		user: state.myInfos
	};
};

class IndexPage extends Component {

	componentDidMount() {
		if (!this.props.user.infos) {
			this.props.dispatch(fetchMyInfos());
		}
	}

	render() {
		const { user } = this.props;
		const { infos } = user;
		return (
			<div className="page">
				<RequestStatus {...user} />
				{infos &&
					<div>Welcome {infos.username}</div>
				}
			</div>
		);
	}
}

export default protect(connect(mapStateToProps)(IndexPage));
