const path = require('path');

module.exports = {
    entry: './src/SelectList.jsx',
    output: {
        path: path.resolve(__dirname),
        filename: 'index.js',
        libraryTarget: 'commonjs2' // 打包出的bundle包含module.exports
    },
    resolve:{
        extensions: [".js", ".jsx"]
    },
    module: {
        rules: [
            {
                test: /\.jsx?$/,
                exclude: /(node_modules)/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['es2015','stage-0', 'react'],
                        comments: false
                    }
                }
            },
            {
                test: /\.less$/,
                use: [
                    'style-loader',
                    'css-loader',
                    'less-loader'
                ]
            }
        ]
    },
    externals : { // 打出的bundle不包含react
        "react": "react",
        "prop-types": "prop-types"
    }
};