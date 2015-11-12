// React / redux
import React, { Component } from 'react';
import { render } from 'react-dom';
import { Provider } from 'react-redux';
import routes from './router';
// Reducers / Actions
import { fetchMyInfos } from './actions/users';
import { store } from './utils/redux-utils';

class ProviderApp extends Component {
	render() {
		return (
			<Provider store={store} key="provider">
				{routes}
			</Provider>
		);
	}
}

render(<ProviderApp />, document.body)
