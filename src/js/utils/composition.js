import React, { Component } from 'react';
import { connect } from 'react-redux';

const mapStateToProps = state => {
	return {
		tokenStatus: state.tokenStatus
	};
}

const privatePage = Composed => class extends Component {

	componentDidMount() {
		this.checkUser(this.props);
	}

	componentWillReceiveProps(newProps) {
		this.checkUser(newProps);
	}

	checkUser(props) {
		const { tokenStatus } = props;
		if (!tokenStatus.authorized) {
			this.props.history.pushState(null, '/login');
		}
	}

	render() {
		return <Composed {...this.props} />;
	}

};

export function protect(page) {
	return connect(mapStateToProps)(privatePage(page));
}
