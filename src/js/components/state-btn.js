import React, { Component } from 'react';

export default class StateBtn extends Component {

	
	render() {
		const className = currentState == "pending" ? "pending-btn" : "";
		return (
			<button className={className} onClick={this.props.onClick}>{this.props.label}</button>
		);
	}
}