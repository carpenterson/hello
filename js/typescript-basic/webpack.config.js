const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CleanWebpackPlugin = require('clean-webpack-plugin');

module.exports = {
    entry: "./src/index.tsx",
    output: {
        filename: "bundle.js",
        path: path.resolve(__dirname, 'dist')
    },

    devServer: {
        contentBase: './dist'
    },

    plugins: [
        new CleanWebpackPlugin(['dist']),
        new HtmlWebpackPlugin({
            template: 'index.html'
        })
    ],

    devtool: "source-map",

    resolve: {
        extensions: [".ts", ".tsx", ".js", ".json"]
    },

    module: {
        rules: [
            { test: /\.jsx?$/, loader: ["babel-loader"], exclude: /(node_modules)/, },
            { test: /\.tsx?$/, loader: ["awesome-typescript-loader"] },
            { enforce: "pre", test: /\.js$/, loader: "source-map-loader" }
        ]
    }
}