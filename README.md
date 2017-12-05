# IntervalTrigger
Interval Timer For Amazfit Watch

to install this app, you need to unlock list app first with adb:

adb shell
> /sdcard/launcher_config.ini 
am force-stop com.huami.watch.launcher

then you can install IntervalTrigger.apk
adb install IntervalTrigger.apk

If you want to uninstall it, run
adb uninstall com.Difetis.IntervalTrigger.apk

Usage:
Start the app
- Trigger the stopwatch by clicking on the timer "00:00:00"
- Start a new lap by clicking again on the timer : the lap is added in the list
- When training is over hit the "stop" button
- Click on a lap to save your training on the watch external storage
- Or Click reset to save your training and reset the timer
Exit app by swipping left

Your csv files are in csvIntervalTrigger folder.
Tip: if you don't see your files or csvIntervalTrigger, just restart your watch before plugging it to your computer.

You use this app at your own risk as it's still a work in progress. 
No complains for data loss or potential damage to the watch, it's not an official application.
