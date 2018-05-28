# cordova-plugin-otp-auto-verification
This plugin is for otp auto verificaion.
NOTE: This plugin is available only for android.

## Getting Started

To install cordova-plugin-otp-auto-verification from npm, run:

```
$ cordova plugin add cordova-plugin-otp-auto-verification
```
To install cordova-plugin-otp-auto-verification from npm, for ionic run:

```
$ ionic plugin add https://github.com/sandeepdillerao/cordova-plugin-otp-auto-verification
```


## Using the plugin

```
      var options = {
        delimiter : "code is",
        length : 6,
        origin : "WAYSMS"
      };
      
      var success = function (otp) {
        console.log("GOT OTP", otp);
        OTPAutoVerification.stopOTPListener();
      }

      var failure = function () {
        OTPAutoVerification.stopOTPListener();
        console.log("Problem in listening OTP");
      }

      OTPAutoVerification.startOTPListener(options, success, failure);
```

##### delimiter :
This is matching text just before the OTP string.
Suppose your OTP sms is like "One time password for App is 1234"
then your delimiter will be "App is".

##### length : 
This is length of you OTP string. when otp sms received code will extract this length digits after `delimeter` string.

##### origin :
This is 6 digit senderID which your getting in SMS.
eg. if your getting SMS from senderID TD-ABCDEF then origin = "ABCDEF"

##### NOTE: Sender ID should be fixed one to work Auto OTP verification. 

## License

The MIT License (MIT)
Copyright (c) 2016 Sandeep Dillerao

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
