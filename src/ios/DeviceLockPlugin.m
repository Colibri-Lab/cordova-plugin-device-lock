#import <Cordova/CDV.h>

@interface DeviceLockPlugin : CDVPlugin
@end

@implementation DeviceLockPlugin

- (void)pluginInitialize {
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(deviceLocked)
                                                 name:UIApplicationProtectedDataWillBecomeUnavailable
                                               object:nil];

    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(deviceUnlocked)
                                                 name:UIApplicationProtectedDataDidBecomeAvailable
                                               object:nil];
}

- (void)deviceLocked {
    [self.webViewEngine evaluateJavaScript:@"window.dispatchEvent(new CustomEvent('deviceLocked'));" completionHandler:nil];
}

- (void)deviceUnlocked {
    [self.webViewEngine evaluateJavaScript:@"window.dispatchEvent(new CustomEvent('deviceUnlocked'));" completionHandler:nil];
}

@end