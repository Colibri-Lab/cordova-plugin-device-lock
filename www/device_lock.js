var exec = require('cordova/exec');

exports.watch = function (success, error) {
    exec(success, error, "DeviceLockPlugin", "watch", []);
};