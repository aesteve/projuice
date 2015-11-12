import React, { Component } from 'react';
import { PropTypes } from 'react-router';
import { connect } from 'react-redux';
import Menu from './components/menu';

class App extends Component {

	componentDidMount() {
		this.props.dispatch(fetchMyInfos());
	}

	render() {
		const { user, children } = this.props;
		return (
			<div>
				<Menu />
				{children}
			</div>
		);
	}

}

App.contextTypes = {history: PropTypes.history};

export default connect()(App);
