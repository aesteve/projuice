"use strict";

var Reflux = require('reflux');

var ProjectsStore = Reflux.createStore({
	init : function () {
		this.listenTo('updateProjects', this.updateProjects);
	},
	
	updateProjects : function (projects) {
		this.projects = projects;
	},
	
	getProjects : function () {
		return this.projects;
	}
});

module.exports = ProjectsStore;