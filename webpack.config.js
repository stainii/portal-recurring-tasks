const CopyWebpackPlugin = require('copy-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');

module.exports = {
    entry: {
        main: './src/main/react/js/app.js'
    },
    output: {
        path: __dirname + "/src/main/public/",
        filename: 'dist.js'
    },
    devtool: 'source-map',
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /(node_modules|bower_components)/,
                loader: 'babel-loader'
            }, {
                test: /\.scss$/,
                use: [
                    MiniCssExtractPlugin.loader,
                'css-loader',
                'sass-loader'
                ]
            },
        ]
    }, plugins: [
        new CopyWebpackPlugin([
            "src/main/react/static", "src/main/react/index.html"
        ]),
        new MiniCssExtractPlugin()
    ]
};