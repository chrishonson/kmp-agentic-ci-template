// Configure webpack-dev-server to ignore .git and other non-source directories
// This prevents random page reloads when git operations occur

// Ignore patterns for static file watching
const ignoredPatterns = [
    '**/.git/**',
    '**/node_modules/**',
    '**/build/**',
    '**/.gradle/**',
    '**/.idea/**',
    '**/*.log'
];

// Configure watchFiles to ignore .git directory
config.devServer = config.devServer || {};
config.devServer.watchFiles = {
    paths: ['src/**/*', 'composeApp/**/*.kt', 'composeApp/**/*.html'],
    options: {
        ignored: ignoredPatterns
    }
};

// Disable watching on static directories to prevent FETCH_HEAD reloads
if (config.devServer.static) {
    if (Array.isArray(config.devServer.static)) {
        config.devServer.static = config.devServer.static.map(staticEntry => {
            if (typeof staticEntry === 'string') {
                return {
                    directory: staticEntry,
                    watch: false  // Disable watching entirely for static dirs
                };
            } else if (typeof staticEntry === 'object') {
                return {
                    ...staticEntry,
                    watch: false
                };
            }
            return staticEntry;
        });
    } else if (typeof config.devServer.static === 'object') {
        config.devServer.static.watch = false;
    }
}

// Also set general watchOptions as a fallback
config.watchOptions = config.watchOptions || {};
config.watchOptions.ignored = ignoredPatterns;
config.watchOptions.poll = false;
