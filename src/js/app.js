// React / redux
import React, { Component } from 'react';
import { render } from 'react-dom';
import { Router, Route, Link, IndexRoute, PropTypes } from 'react-router';
import { Provider } from 'react-redux';
import { connect } from 'react-redux';
import { createStore } from 'redux';
// Pages
import NoMatch from './pages/404';
import Menu from './components/menu';
import Login from './pages/login';
import Projects from './pages/projects';
import Project from './pages/project';
import Index from './pages/index';
import { fetchMyInfos } from './actions/users';
// Reducers
import { store } from './utils/redux-utils';

const mapStateToProps = (state, props) => {
	return {
		user: state.myInfos.infos
	};
};

export default class App extends Component {

	componentDidMount() {
		store.dispatch(fetchMyInfos());
	}

	render() {
		const { user, children } = this.props;
		return (
			<div>
				<Menu user={user} />
				{children}
			</div>
		);
	}
}

App.contextTypes = {history: PropTypes.history};

const ConnectedApp = connect(mapStateToProps)(App);

const routes = (
	<Router>
		<Route path="/" component={ConnectedApp}>
			<IndexRoute component={Index} />
			<Route path="login" component={Login} />
			<Route path="projects">
				<IndexRoute component={Projects} />
				<Route path=":projectId" component={Project} />
			</Route>
			<Route path="*" component={NoMatch} />
		</Route>
	</Router>
);

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
