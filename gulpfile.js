var gulp = require("gulp");
var browserify = require("browserify");
var source = require('vinyl-source-stream');
var babelify = require("babelify");
var sass = require("gulp-sass");
var concat = require("gulp-concat");

var config = {
    paths: {
        html: "./src/main/webapp/*.html",
        js: "./src/main/webapp/js/**/*.js",
        sass: "./src/main/webapp/**/*.scss",
        mainJS: "./src/main/webapp/js/app.js",
        static: "./src/main/webapp/static/**/*",
        webInf: "./src/main/webapp/WEB-INF/**/*",
        dist: "./src/main/webapp/dist/"
    }

}

gulp.task("html", function() {
    gulp.src(config.paths.html)
        .pipe(gulp.dest(config.paths.dist));
});

gulp.task("js", function() {
    browserify(config.paths.mainJS)
        .transform("babelify", {
            presets: ["es2015", "react"]
        })
        .bundle()
        .on("error", console.error.bind(console))
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

gulp.task("default", [ "html", "js", "sass", "web-inf", "static" ]);