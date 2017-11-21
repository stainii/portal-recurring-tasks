var gulp = require("gulp");
var browserify = require("browserify");
var source = require('vinyl-source-stream');
var sass = require("gulp-sass");
var concat = require("gulp-concat");
var connect = require("gulp-connect");
var open = require("gulp-open");
var Proxy = require('gulp-api-proxy');

var config = {
    port: 3000,
    devBaseUrl: "http://localhost",
    paths: {
        html: "./src/main/webapp/*.html",
        js: "./src/main/webapp/js/**/*.js",
        sass: "./src/main/webapp/**/*.scss",
        mainJS: "./src/main/webapp/js/app.js",
        bootstrapJS: "./src/main/webapp/js/bootstrap.js",
        static: "./src/main/webapp/static/**/*",
        webInf: "./src/main/webapp/WEB-INF/**/*",
        dist: "./src/main/webapp/dist/"
    }

};


gulp.task("connect", function () {
    connect.server({
        root: [config.paths.dist],
        port: config.port,
        base: config.devBaseUrl,
        livereload: true,
        middleware: function (connect, opt) {
            opt.route = '/portal/module/housagotchi';
            opt.context = 'localhost:8080/portal/module/housagotchi';
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

gulp.task("html", function() {
    gulp.src(config.paths.html)
        .pipe(gulp.dest(config.paths.dist));
});

gulp.task("js-for-export", function() {
    browserify(config.paths.mainJS)
        .bundle()
        .pipe(source("bundle.js"))
        .pipe(gulp.dest(config.paths.dist))
});

gulp.task("js-for-lite-server", function() {
    browserify(config.paths.bootstrapJS)
        .bundle()
        .pipe(source("bundle.js"))
        .pipe(gulp.dest(config.paths.dist))
});

gulp.task("web-inf", function() {
    gulp.src(config.paths.webInf)
        .pipe(gulp.dest(config.paths.dist + "/WEB-INF/"));
});


gulp.task("static", function() {
    gulp.src(config.paths.static)
        .pipe(gulp.dest(config.paths.dist + "/static/"));
});

gulp.task('sass', function () {
    return gulp.src(config.paths.sass)
        .pipe(sass())
        .pipe(concat("bundle.css"))
        .pipe(gulp.dest(config.paths.dist));
});

gulp.task("default", [ "js-for-export", "sass", "web-inf", "static" ]);
gulp.task("lite", [ "html", "js-for-lite-server", "sass", "web-inf", "static", "connect", "open" ]);