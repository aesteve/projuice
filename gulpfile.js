'use strict';

var gulp = require('gulp');
var gutil = require('gulp-util');
var rename = require('gulp-rename');
var browserify = require('browserify');
var watchify = require('watchify');
var babelify = require('babelify');
var source = require('vinyl-source-stream');
var buffer = require('vinyl-buffer');
var es = require('event-stream');
var argv = require('yargs').argv;


var requireFiles = './node_modules/react/react.js';



function compileScripts(page, reload) {
    gutil.log('Starting browserify: ' + page);

    /*
   	var startName = entry.lastIndexOf('/') + 1;
	var endName = entry.lastIndexOf('.js');
	var newName = entry.substring(startName, endName);
	*/

    var browserifyer = browserify({
    	cache: {}, packageCache: {}, fullPaths: true,
    	entries: ['./src/js/pages/' + page + '.js'],
    	extensions: ['.js'],
    	debug:reload
    }).transform(babelify, {presets: ["es2015", "react"]});
    
    var bundler = watchify(browserifyer);
    
    function rebundle(entry) {
    	bundler.bundle()
    		.on('error', function(err) {
    	        console.error(err);
    	        this.emit('end');
    	    })
            .pipe(source('./src/js/pages/' + page + '.js'))
            .pipe(buffer())
            .pipe(rename({
            	dirname:'./',
            	basename:page,
                extname: '.min.js'
            }))
            .pipe(gulp.dest('./web/assets/scripts'));
    }
        	
    if (reload) {
    	bundler.on('update', function() {
    		console.log('-> bundling...');
    		rebundle();
    	});
    }
    rebundle();
}

gulp.task('dev', function () {
	compileScripts(argv.page, true);
});