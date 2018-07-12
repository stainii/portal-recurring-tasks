var gulp = require("gulp");
var browserify = require("browserify");
var source = require('vinyl-source-stream');
var sass = require("gulp-sass");
var concat = require("gulp-concat");
var connect = require("gulp-connect");
var open = require("gulp-open");
var Proxy = require('gulp-api-proxy');
var clean = require("gulp-clean");

var config = {
    port: 3001,
    devBaseUrl: "http://localhost",
    paths: {
        html: "./src/main/react/*.html",
        js: ["./src/main/react/js/**/*.js", "!./test"],
        sass: "./src/main/react/**/*.scss",
        mainJS: "./src/main/react/js/app.js",
        static: "./src/main/react/static/**/*",
        dist: "./src/main/public/"
    }

};

gulp.task("clean", function() {
    return gulp.src(config.paths.dist, {read: false})
        .pipe(clean());
});

gulp.task("connect", ["clean", "html", "js", "sass", "static"], function () {
    connect.server({
        root: [config.paths.dist],
        port: config.port,
        base: config.devBaseUrl,
        livereload: true,
        middleware: function (connect, opt) {
            opt.route = '/api';
            opt.context = 'localhost:2002/api';
            return [new Proxy(opt)];
        }
    })
});

gulp.task("open", ["connect"], function() {
    gulp.src(config.paths.dist + "index.html")
        .pipe(open({
            uri: config.devBaseUrl + ":" + config.port + "/",
        }));
});

gulp.task("html", ["clean"], function() {
    gulp.src(config.paths.html)
        .pipe(gulp.dest(config.paths.dist));
});

gulp.task("js", ["clean"], function() {
    browserify(config.paths.mainJS)
        .bundle()
        .pipe(source("bundle.js"))
        .pipe(gulp.dest(config.paths.dist + "/js/"))
});

gulp.task("static", ["clean"], function() {
    gulp.src(config.paths.static)
        .pipe(gulp.dest(config.paths.dist + "/static/"));
});

gulp.task('sass', ["clean"], function () {
    return gulp.src(config.paths.sass)
        .pipe(sass())
        .pipe(concat("bundle.css"))
        .pipe(gulp.dest(config.paths.dist + "/css/"));
});

gulp.task("default", [ "clean", "html", "js", "sass", "static" ]);
gulp.task("lite", [ "clean", "html", "js", "sass", "static", "connect", "open" ]);