import React, { Component } from 'react';
import { render } from 'react-dom';
import { Router, Route, Link, IndexRoute, PropTypes } from 'react-router'
import NoMatch from './pages/404';
import Menu from './components/menu';
import Login from './pages/login';
import Projects from './pages/projects';
import Project from './pages/project';
import ApiClient from './io/api';
import RequestStatus from './components/request-status';
import { injectResponseAsState } from './components/request-utils';

const mapStateToProps = (state, props) => {
	return {
		tokenStatus: state.tokenStatus,
		user: state.users.me
	};
};

@connect(mapStateToProps)
export default class App extends Component {

	constructor(props) {
		super(props);
		this.state = {
			pending: true
		};
		this.apiClient = new ApiClient(this.props.history);
	}

	componentDidMount() {
		store.dispatch(fetchMyInfos());
	}

	render() {
		const { user } = this.state;
		return (
			<div>
				<RequestStatus {...this.state} />
				<Menu user={user} />
				{this.props.children}
			</div>
		);
	}
}

App.contextTypes = {history: PropTypes.history};


class ProviderApp extends Component {
	render() {
		return (
			<Provider store={store} key="provider">
				{() => <App store={store} />}
			</Provider>
		);
	}
}

render((
	<Router>
		<Route path="/" component={ProviderApp}>
			<Route path="login" component={Login} />
			<Route path="projects">
				<IndexRoute component={Projects} />
				<Route path=":projectId" component={Project} />
			</Route>
			<Route path="*" component={NoMatch} />
		</Route>
	</Router>
), document.body)
