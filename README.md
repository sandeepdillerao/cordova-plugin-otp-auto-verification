# cordova-plugin-otp-auto-verification
This plugin is for otp auto verificaion.
NOTE: This plugin is available only for android.

## Getting Started

To install cordova-plugin-otp-auto-verification from npm, run:

```
$ cordova plugin add cordova-plugin-otp-auto-verification
```
It is also possible to install via repo url directly (unstable), run :

```
$ cordova plugin add https://github.com/cordova-sms/cordova-sms-plugin.git
```

## Using the plugin

```
     var success = function(message) {
        alert(message);
      }

      var failure = function() {
        alert("Error calling Hello Plugin");
      }
      
      var options = {
        delimiter : "code is",
        length : 6,
        origin : "IM-WAYSMS"
      };
      
      OTPAutoVerification.startOTPListener(options, success, failure);
```



## License

[MIT License](http://en.wikipedia.org/wiki/MIT_License)
