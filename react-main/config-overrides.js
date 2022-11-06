/* config-overrides.js */
const webpack = require('webpack');

module.exports = function override(config, env) {
	//http: require.resolve('stream-http'),
	//https: require.resolve('https-browserify'),
	//os: require.resolve('os-browserify/browser'),
	config.resolve.extensions = [...config.resolve.extensions, '.ts', '.js', '.jsx', '.tsx'];
	config.resolve.fallback = {
		...config.resolve.fallback,
        assert: require.resolve('assert'),
        buffer: require.resolve('buffer'),
		crypto: require.resolve('crypto-browserify'),
        stream: require.resolve('stream-browserify'),
		url: require.resolve('url')
	};
//			process: 'process/browser',
//	config.plugins(
//		new webpack.ProvidePlugin({
//			Buffer: ['buffer', 'Buffer'],
//		}),
//	);
//			process: 'process/browser',
	config.plugins.push(
		...config.resolve.plugins,
		new webpack.ProvidePlugin({
			Buffer: ['buffer', 'Buffer'],
		}),
		new webpack.ProvidePlugin({
			process: 'process/browser',
		}),
	);
	return config;
}
