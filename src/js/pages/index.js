import React, { Component } from 'react';
import { privatePage } from '../utils/composition';
import { connect } from 'react-redux';

const mapStateToProps = state => {
	return {
		user: state.myInfos,
		tokenStatus: state.tokenStatus
	};
};

class IndexPage extends Component {

	render() {
		return (
			<div>Welcome {this.props.user.username}</div>
		);
	}
}

export default connect(mapStateToProps)(privatePage(IndexPage));
