import React, { Component } from 'react';

export default class DisplayError extends Component {
	render() {
		return (
			<div className="error">Error: {this.props.error}</div>
		);
	}
}