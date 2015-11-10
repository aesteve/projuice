import React, { Component } from 'react';
import DisplayError from './display-error';
import Loader from './loader';

export default class RequestStatus extends Component {
	render() {
		const { error, status } = this.props;
		if (error) {
			return <DisplayError error={error} />;
		} else if (status === 'pending') {
			return <Loader />;
		} /*else if (status === 'done') {
			return <div className="alert-box success radius">Done</div>;
		} */else {
			return null;
		}
	}
}
