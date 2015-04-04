/*jshint node:true */
"use strict";

var React = require('react');
var _ = require('lodash');
var ProjectsCollection = require('../../io/paginated-collection');
var ProjectPreview = require('./project-preview');


var Projects = React.createClass({

	getInitialState : function () {
		return {
			collection : new ProjectsCollection('projects'),
			projects : []
		}
	},
	
	componentDidMount : function () {
		var self = this;
		this.state.collection.fetch().done(function () {
			self.setState({projects : self.state.collection.currentPageResources});
		});
	},

	render: function() {
		return (
			<div>
				Projects dashboard
				{this.getProjects()}
			</div>
		);
	},
								 
	getProjects : function () {
		return _.map(this.state.projects, function(project) {
			return <ProjectPreview key={project.id} project={project} />
		});
	}
});

module.exports = Projects;