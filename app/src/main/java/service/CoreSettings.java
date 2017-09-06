package service;


import android.content.Context;

import helper.Session;

public class CoreSettings {
	// Default values of core settings
	private static long FREQUENCY_POLL = 1000L;        //milliseconds
	private static long CHECK_HASHES = 60000L;			// 30 sec
	private static boolean AUTO_ENABLE_GPS = true;
	private static boolean BATTERY_SAVER = false;
	private static long VIBRATION_RATE = 15L;
	private static long FREQUENCY_CRON = 40000L; 		// 40 sec
	private static long FREQUENCY_NOTIFIER = 10000L; 		// 10 sec
	private static long CHECK_PROVIDER_PERIOD = 6000L;	      //seconds 60 sec
	private static long RECALCULATE_DIARY = 60000L;
	private static long GENERATE_NOTICE = 60000L;
	private static long CHECK_NOTICE = 20000L;
	private static long CHECK_ALERTS = 30000L;			// 30 sec

	// Current values of core settings. Those values populate from session.
	private static long mFrequencyPoll;  	 // Frequency poll the location of phone (milliseconds)
	private static long mCheckHashes;			// Time checking of application setting. Every time the app requests settings from server.
	private static boolean mAutoEnableGPS;	// Automatically enable GPS sensor if user switch off it.
	private static boolean mBatterySaver;     // Use the battery saver mode
	private static long mVibrationRate; 		// The lower the rate, the more sensitive the phone to the vibration.
	private static long mFrequencyCron; 		// CRON frequency.
	private static long mFrequencyNotifier;    // Notifier frequency. Working with notice chain. Using in Notifier class. 
	private static long mCheckProviderPeriod;	// Period in seconds to check the available location providers if at the moment the App have no any providers.
	private static long mRecalculateDiary;	// Time checking of user diary to recalculating of events on server!.
	private static long mGenerateNotice;      // Period generating notice for user. 
	private static long mCheckNotice;         // Period checking the generated notice of user.
	private static long mLateAlertTime;           // If user late by this time so the alert will send to carer.
	private static long mCheckAlerts;         // Time checking of carer's alerts . Every time the app requests alerts from server.
	
	public static long getFrequencyPoll(){ return mFrequencyPoll; }
	public static long getCheckHashes(){ return mCheckHashes; }
	public static boolean getAutoEnableGPS(){ return mAutoEnableGPS; }
	public static boolean getBatterySaver(){ return mBatterySaver; }
	public static long getVibrationRate(){ return mVibrationRate; }
	public static long getFrequencyCron(){ return mFrequencyCron; }
	public static long getFrequencyNotifier(){ return mFrequencyNotifier; }
	public static long getCheckProviderPeriod(){ return mCheckProviderPeriod; }
	public static long getRecalculateDiary(){ return mRecalculateDiary; }
	public static long getGenerateNotice(){ return mGenerateNotice; }
	public static long getCheckNotice(){ return mCheckNotice; }
	public static long getLateAlertTime(){ return mLateAlertTime; }
	public static long getCheckAlerts(){ return mCheckAlerts; }

	public static void init(Context context) {
		mFrequencyPoll = Session.getLongValue(context, "FREQUENCY_POLL", FREQUENCY_POLL);
		mCheckHashes = Session.getLongValue(context, "CHECK_HASHES", CHECK_HASHES);
		mVibrationRate = Session.getLongValue(context, "VIBRATION_RATE", VIBRATION_RATE);
		mVibrationRate = Session.getLongValue(context, "VIBRATION_RATE", VIBRATION_RATE);
		mFrequencyCron = Session.getLongValue(context, "FREQUENCY_CRON", FREQUENCY_CRON);
		mFrequencyNotifier = Session.getLongValue(context, "FREQUENCY_NOTIFIER", FREQUENCY_NOTIFIER);
		mAutoEnableGPS = Session.getBooleanValue(context, "AUTO_ENABLE_GPS", AUTO_ENABLE_GPS);
		mBatterySaver = Session.getBooleanValue(context, "BATTERY_SAVER", BATTERY_SAVER);
		mCheckProviderPeriod = Session.getLongValue(context, "CHECK_PROVIDER_PERIOD", CHECK_PROVIDER_PERIOD);
		mRecalculateDiary = Session.getLongValue(context, "RECALCULATE_DIARY", RECALCULATE_DIARY);
		mGenerateNotice = Session.getLongValue(context, "GENERATE_NOTICE", GENERATE_NOTICE);
		mCheckNotice = Session.getLongValue(context, "CHECK_NOTICE", CHECK_NOTICE);
		mCheckAlerts = Session.getLongValue(context, "CHECK_ALERTS", CHECK_ALERTS);
		
		/*Settings settings = Settings.loadByCurrUser(context);
		mLateAlertTime = settings.getCarerTime();*/
	}
}
