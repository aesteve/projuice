import React from 'react';
import { Router, Route, Link, IndexRoute } from 'react-router';
// Pages
import App from './app';
import NoMatch from './pages/404';
import Login from './pages/login';
import Projects from './pages/projects';
import Project from './pages/project';
import Index from './pages/index';


export default (
	<Router>
		<Route path="/" component={App}>
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
