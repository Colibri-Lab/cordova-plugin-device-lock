var exec = require('cordova/exec');

module.exports = {
    watch: function (success, error) {
        exec(success, error, "DeviceLockPlugin", "watch", []);
    }
};