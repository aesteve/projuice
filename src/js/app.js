import React, { Component } from 'react';
import { render } from 'react-dom';
import { Router, Route, Link, IndexRoute, PropTypes } from 'react-router'
import NoMatch from './pages/404';
import Menu from './components/menu';
import Login from './pages/login';
import Projects from './pages/projects';
import Project from './pages/project';
import { get } from './io/api';
import Loader from './components/loader';
import Error from './components/display-error';

export default class App extends Component {

	constructor(props) {
			super(props);
			this.state = {
				pending: true
			};
	}

	componentDidMount() {
		get('/users/me', (err, res) => {
			if (err && err.status === 401) {
				this.props.history.pushState(null, '/login');
			} else if (err) {
				this.setState({
					error: err,
					pending: false
				});
			} else {
				this.setState({
					error: null,
					pending: false,
					user: res.body
				});
			}
		});
	}

	render() {
		const { error, pending, user } = this.state;
		return (
			<div>
				<Menu user={user} />
				{pending && <Loader />}
				{error && <Error error={error} />}
				{this.props.children}
			</div>
		);
	}
}

App.contextTypes = {history: PropTypes.history};

render((
	<Router>
		<Route path="/" component={App}>
			<Route path="login" component={Login} />
			<Route path="projects">
				<IndexRoute component={Projects} />
				<Route path=":projectId" component={Project} />
			</Route>
			<Route path="*" component={NoMatch} />
		</Route>
	</Router>
), document.body)
