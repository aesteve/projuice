import React, { Component } from 'react';

export const privatePage = Composed => class extends Component {

	componentDidMount() {
		this.checkUser(this.props);
	}

	componentWillReceiveProps(newProps) {
		this.checkUser(newProps);
	}

	checkUser(props) {
		console.log(props);
		const { tokenStatus } = props;
		if (!tokenStatus.authorized) {
			this.props.history.pushState(null, '/login');
		}
	}

	render() {
		return <Composed {...this.props} />;
	}

};
