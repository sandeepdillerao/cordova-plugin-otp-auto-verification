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

The MIT License (MIT)
Copyright (c) 2016 Sandeep Dillerao

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
