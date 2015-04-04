"use strict";

var $ = require('jquery');
var React = require('react');

var factory = require('../../components/project/projects');
var instance = factory({});
$(document).ready(function () {
	React.render(instance, document.getElementById("projects"));
});