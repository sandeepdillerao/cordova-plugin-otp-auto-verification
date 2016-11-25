var exec = require('cordova/exec');

module.exports = {
    coolMethod: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "OTPAutoVerification", "coolMethod", [name]);
    }
};
