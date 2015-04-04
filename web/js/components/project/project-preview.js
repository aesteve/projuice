"use strict";

var React = require('react');

var ProjectPreview = React.createClass({
	render : function () {
		return (
			<div className="project-preview">
				<div className="box-header">{this.props.project.name}</div>
				<div className="box-content">{this.props.project.description}</div>
			</div>
		);
	}
});

module.exports = ProjectPreview;