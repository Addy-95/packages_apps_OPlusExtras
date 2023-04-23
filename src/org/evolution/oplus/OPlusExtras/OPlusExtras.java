/*
 * Copyright (C) 2018-2022 crDroid Android Project
 *               2023 The Evolution X Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.evolution.oplus.OPlusExtras;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceManager;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;

import java.util.Arrays;
import java.util.Random;

import com.plattysoft.leonids.ParticleSystem;

import org.evolution.oplus.OPlusExtras.R;
import org.evolution.oplus.OPlusExtras.slider.SliderConstants;
import org.evolution.oplus.OPlusExtras.utils.FileUtils;
import org.evolution.oplus.OPlusExtras.utils.Utils;

public class OPlusExtras extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {
    private static final String TAG = OPlusExtras.class.getSimpleName();

    // Alert slider
    private static final String KEY_ALERT_SLIDER = "slider";
    private ListPreference mTopKeyPref;
    private ListPreference mMiddleKeyPref;
    private ListPreference mBottomKeyPref;

    // CPU
    private static final String KEY_POWER_EFFICIENT_WORKQUEUE = "power_efficient_workqueue";
    private SwitchPreference mPowerEfficientWorkqueueModeSwitch;
    private String nodePowerEfficientWorkqueue;

    private static final String KEY_TOUCHBOOST = "touchboost";
    private SwitchPreference mTouchboostModeSwitch;
    private String nodeTouchboost;

    // Display
    private static final String KEY_AMBIENT_DISPLAY_GESTURES = "ambient_display_gestures_settings";

    private static final String KEY_ANTIFLICKER = "antiflicker";
    private static final String KEY_ANTIFLICKER_INFO = "antiflicker_info";
    private SwitchPreference mAntiFlickerModeSwitch;
    private String nodeAntiFlicker;

    private static final String KEY_HBM = "hbm";
    private static final String KEY_HBM_INFO = "hbm_info";
    private SwitchPreference mHBMModeSwitch;
    private String nodeHBM;

    private static final String KEY_KCAL = "kcal";

    private static final String KEY_KEEP_PCC = "keep_pcc";
    private SwitchPreference mKeepPCCModeSwitch;
    private String nodeKeepPCC;

    private static final String KEY_MAX_BRIGHTNESS = "max_brightness";
    private static final String MAX_BRIGHTNESS_DEFAULT = "2047";
    private CustomSeekBarPreference mMaxBrightnessPreference;
    private String nodeMaxBrightness;

    private static final String KEY_PER_APP_COLORSPACE = "per_app_colorspace";

    private static final String KEY_PER_APP_REFRESH_RATE = "per_app_refresh_rate";

    // Filesystem
    private static final String KEY_FSYNC = "fsync";
    private static final String KEY_FSYNC_INFO = "fsync_info";
    private SwitchPreference mFSyncSwitch;
    private String nodeFSync;

    // GPU
    private static final String KEY_ADRENOBOOST = "adrenoboost";
    private static final String ADRENOBOOST_DEFAULT = "0";
    private CustomSeekBarPreference mAdrenoboostPreference;
    private String nodeAdrenoboost;

    // Power
    private static final String KEY_POWERSHARE = "powershare";
    private SwitchPreference mPowershareModeSwitch;
    private String nodePowershare;

    private static final String KEY_QUIET_MODE = "quiet_mode";
    private SwitchPreference mQuietModeSwitch;
    private String nodeQuietMode;

    // Sound control
    private static final String KEY_MIC_GAIN = "mic_gain";
    private static final String MIC_GAIN_DEFAULT = "0";
    private CustomSeekBarPreference mMicGainPreference;
    private String nodeMicGain;

    private static final String KEY_SPEAKER_GAIN = "speaker_gain";
    private static final String SPEAKER_GAIN_DEFAULT = "0";
    private CustomSeekBarPreference mSpeakerGainPreference;
    private String nodeSpeakerGain;

    // Touchscreen
    private static final String KEY_EDGE_LIMIT = "edge_limit";
    private SwitchPreference mEdgeLimitSwitch;
    private String edgeLimit;

    private static final String KEY_HIGH_TOUCH_POLLING_RATE = "high_touch_polling_rate";
    private static final String KEY_HIGH_TOUCH_POLLING_RATE_INFO = "high_touch_polling_rate_info";
    private SwitchPreference mHighTouchPollingRateSwitch;
    private String nodeHighTouchPollingRate;

    private static final String KEY_TOUCH_GESTURES = "touchscreen_gestures";

    // USB
    private static final String KEY_USB2_FAST_CHARGE = "usb2_fast_charge";
    private static final String KEY_USB2_FAST_CHARGE_INFO = "usb2_fast_charge_info";
    private SwitchPreference mUSB2FastChargeSwitch;
    private String nodeUSB2FastCharge;

    private static final String KEY_USB_OTG = "usb_otg";
    private SwitchPreference mUSBOTGSwitch;
    private String nodeOTG;

    // Vibrator
    private static final String KEY_VIBRATOR_STRENGTH = "vibrator_strength";
    private static final String VIBRATOR_STRENGTH_DEFAULT = "3";
    private static final long testVibrationPattern[] = {0,5};
    private CustomSeekBarPreference mVibratorStrengthPreference;
    private String nodeVibratorStrength;
    private Vibrator mVibrator;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.main);

        SharedPreferences prefs = getActivity().getSharedPreferences("main",
                Activity.MODE_PRIVATE);
        if (savedInstanceState == null && !prefs.getBoolean("first_warning_shown", false)) {
            showWarning();
        }

        mVibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());

         // OPlusExtras categories
         PreferenceCategory[] categories = new PreferenceCategory[] {
             (PreferenceCategory) findPreference("category_cpu"),
             (PreferenceCategory) findPreference("category_display"),
             (PreferenceCategory) findPreference("category_filesystem"),
             (PreferenceCategory) findPreference("category_gpu"),
             (PreferenceCategory) findPreference("category_power"),
             (PreferenceCategory) findPreference("category_sound_control"),
             (PreferenceCategory) findPreference("category_touchscreen"),
             (PreferenceCategory) findPreference("category_usb"),
             (PreferenceCategory) findPreference("category_vibrator")
         };

        // OPlusExtras nodes
        nodePowerEfficientWorkqueue = getResources().getString(R.string.node_power_efficient_workqueue_switch);
        nodeTouchboost = getResources().getString(R.string.node_touchboost_switch);
        nodeAntiFlicker = getResources().getString(R.string.node_antiflicker_switch);
        nodeHBM = getResources().getString(R.string.node_hbm_switch);
        nodeKeepPCC = getResources().getString(R.string.node_keep_pcc_switch);
        nodeMaxBrightness = getResources().getString(R.string.node_max_brightness_preference);
        nodeFSync = getResources().getString(R.string.node_fsync_switch);
        nodeAdrenoboost = getResources().getString(R.string.node_adrenoboost_preference);
        nodeMicGain = getResources().getString(R.string.node_microphone_gain_preference);
        nodeSpeakerGain = getResources().getString(R.string.node_speaker_gain_preference);
        nodePowershare = getResources().getString(R.string.node_powershare_switch);
        nodeQuietMode = getResources().getString(R.string.node_quiet_mode_switch);
        edgeLimit = getResources().getString(R.string.node_edge_limit_switch);
        nodeHighTouchPollingRate = getResources().getString(R.string.node_high_touch_polling_rate_switch);
        nodeUSB2FastCharge = getResources().getString(R.string.node_usb2_fast_charge_switch);
        nodeOTG = getResources().getString(R.string.node_usb_otg_switch);
        nodeVibratorStrength = getResources().getString(R.string.node_vibrator_strength_preference);

         // Alert slider
         initNotificationSliderPreference();

       if (!getResources().getBoolean(R.bool.config_deviceSupportsAlertSlider)) {
            findPreference(KEY_ALERT_SLIDER).setVisible(false);
        }

        // Power efficient workqueue switch
        mPowerEfficientWorkqueueModeSwitch = (SwitchPreference) findPreference(KEY_POWER_EFFICIENT_WORKQUEUE);
        if (Utils.fileWritable(nodePowerEfficientWorkqueue)) {
            mPowerEfficientWorkqueueModeSwitch.setEnabled(true);
            mPowerEfficientWorkqueueModeSwitch.setChecked(sharedPrefs.getBoolean(KEY_POWER_EFFICIENT_WORKQUEUE, false));
            mPowerEfficientWorkqueueModeSwitch.setOnPreferenceChangeListener(this);
        } else {
            mPowerEfficientWorkqueueModeSwitch.setEnabled(false);
        }

       if (!getResources().getBoolean(R.bool.config_deviceSupportsPowerEfficientWorkqueue)) {
            findPreference(KEY_POWER_EFFICIENT_WORKQUEUE).setVisible(false);
        }

        // MSM touchboost switch
        mTouchboostModeSwitch = (SwitchPreference) findPreference(KEY_TOUCHBOOST);
        if (Utils.fileWritable(nodeTouchboost)) {
            mTouchboostModeSwitch.setEnabled(true);
            mTouchboostModeSwitch.setChecked(sharedPrefs.getBoolean(KEY_TOUCHBOOST, false));
            mTouchboostModeSwitch.setOnPreferenceChangeListener(this);
        } else {
            mTouchboostModeSwitch.setEnabled(false);
        }

       if (!getResources().getBoolean(R.bool.config_deviceSupportsTouchboost)) {
            findPreference(KEY_TOUCHBOOST).setVisible(false);
        }

       // Ambient display gestures
       if (!getResources().getBoolean(R.bool.config_deviceSupportsAmbientDisplayGestures)) {
            findPreference(KEY_AMBIENT_DISPLAY_GESTURES).setVisible(false);
        }

        // Anti-flicker switch
        mAntiFlickerModeSwitch = (SwitchPreference) findPreference(KEY_ANTIFLICKER);
        if (Utils.fileWritable(nodeAntiFlicker)) {
            mAntiFlickerModeSwitch.setEnabled(true);
            mAntiFlickerModeSwitch.setChecked(sharedPrefs.getBoolean(KEY_ANTIFLICKER, false));
            mAntiFlickerModeSwitch.setOnPreferenceChangeListener(this);
        } else {
            mAntiFlickerModeSwitch.setEnabled(false);
        }

       if (!getResources().getBoolean(R.bool.config_deviceSupportsAntiFlicker)) {
            findPreference(KEY_ANTIFLICKER).setVisible(false);
            findPreference(KEY_ANTIFLICKER_INFO).setVisible(false);
        }

        // High brightness mode switch
        mHBMModeSwitch = (SwitchPreference) findPreference(KEY_HBM);
        if (Utils.fileWritable(nodeHBM)) {
            mHBMModeSwitch.setEnabled(true);
            mHBMModeSwitch.setChecked(sharedPrefs.getBoolean(KEY_HBM, false));
            mHBMModeSwitch.setOnPreferenceChangeListener(this);
        } else {
            mHBMModeSwitch.setEnabled(false);
        }

       if (!getResources().getBoolean(R.bool.config_deviceSupportsHBM)) {
            findPreference(KEY_HBM).setVisible(false);
            findPreference(KEY_HBM_INFO).setVisible(false);
        }

        // Kernel color calibartion
        if (!getResources().getBoolean(R.bool.config_deviceSupportsKcal)) {
            findPreference(KEY_KCAL).setVisible(false);
        }

        // Keep PCC enabled switch
        mKeepPCCModeSwitch = (SwitchPreference) findPreference(KEY_KEEP_PCC);
        if (Utils.fileWritable(nodeKeepPCC)) {
            mKeepPCCModeSwitch.setEnabled(true);
            mKeepPCCModeSwitch.setChecked(sharedPrefs.getBoolean(KEY_KEEP_PCC, false));
            mKeepPCCModeSwitch.setOnPreferenceChangeListener(this);
        } else {
            mKeepPCCModeSwitch.setEnabled(false);
        }

        if (!getResources().getBoolean(R.bool.config_deviceSupportsKeepPCC)) {
            findPreference(KEY_KEEP_PCC).setVisible(false);
        }

        // Maximum brightness preference
        mMaxBrightnessPreference =  (CustomSeekBarPreference) findPreference(KEY_MAX_BRIGHTNESS);
        if (Utils.fileWritable(nodeMaxBrightness)) {
            mMaxBrightnessPreference.setValue(sharedPrefs.getInt(KEY_MAX_BRIGHTNESS,
                Integer.parseInt(Utils.getFileValue(nodeMaxBrightness, MAX_BRIGHTNESS_DEFAULT))));
            mMaxBrightnessPreference.setOnPreferenceChangeListener(this);
        } else {
            mMaxBrightnessPreference.setEnabled(false);
        }

        if (!getResources().getBoolean(R.bool.config_deviceSupportsChangingMaxBrightness)) {
            findPreference(KEY_MAX_BRIGHTNESS).setVisible(false);
        }

        // Per-app colorspace
        if (!getResources().getBoolean(R.bool.config_deviceSupportsPerAppColorSpace)) {
            findPreference(KEY_PER_APP_COLORSPACE).setVisible(false);
        }

        // Per-app refresh rate
        if (!getResources().getBoolean(R.bool.config_deviceSupportsPerAppRefreshRate)) {
            findPreference(KEY_PER_APP_REFRESH_RATE).setVisible(false);
        }

        // Fsync switch
        mFSyncSwitch = (SwitchPreference) findPreference(KEY_FSYNC);
        if (Utils.fileWritable(nodeFSync)) {
            mFSyncSwitch.setEnabled(true);
            mFSyncSwitch.setChecked(sharedPrefs.getBoolean(KEY_FSYNC, true));
            mFSyncSwitch.setOnPreferenceChangeListener(this);
        } else {
            mFSyncSwitch.setEnabled(false);
        }

        if (!getResources().getBoolean(R.bool.config_deviceSupportsFSyncOff)) {
            findPreference(KEY_FSYNC).setVisible(false);
            findPreference(KEY_FSYNC_INFO).setVisible(false);
        }

        // Adrenoboost preference
        mAdrenoboostPreference =  (CustomSeekBarPreference) findPreference(KEY_ADRENOBOOST);
        if (Utils.fileWritable(nodeAdrenoboost)) {
            mAdrenoboostPreference.setValue(sharedPrefs.getInt(KEY_ADRENOBOOST,
                Integer.parseInt(Utils.getFileValue(nodeAdrenoboost, ADRENOBOOST_DEFAULT))));
            mAdrenoboostPreference.setOnPreferenceChangeListener(this);
        } else {
            mAdrenoboostPreference.setEnabled(false);
        }

        if (!getResources().getBoolean(R.bool.config_deviceSupportsAdrenoboost)) {
            findPreference(KEY_ADRENOBOOST).setVisible(false);
        }

        // Mic gain preference
        mMicGainPreference =  (CustomSeekBarPreference) findPreference(KEY_MIC_GAIN);
        if (Utils.fileWritable(nodeMicGain)) {
            mMicGainPreference.setValue(sharedPrefs.getInt(KEY_MIC_GAIN,
                Integer.parseInt(Utils.getFileValue(nodeMicGain, MIC_GAIN_DEFAULT))));
            mMicGainPreference.setOnPreferenceChangeListener(this);
        } else {
            mMicGainPreference.setEnabled(false);
        }

        if (!getResources().getBoolean(R.bool.config_deviceSupportsMicGain)) {
            findPreference(KEY_MIC_GAIN).setVisible(false);
        }

        // Speaker gain preference
        mSpeakerGainPreference =  (CustomSeekBarPreference) findPreference(KEY_SPEAKER_GAIN);
        if (Utils.fileWritable(nodeSpeakerGain)) {
            mSpeakerGainPreference.setValue(sharedPrefs.getInt(KEY_SPEAKER_GAIN,
                Integer.parseInt(Utils.getFileValue(nodeSpeakerGain, SPEAKER_GAIN_DEFAULT))));
            mSpeakerGainPreference.setOnPreferenceChangeListener(this);
        } else {
            mSpeakerGainPreference.setEnabled(false);
        }

        if (!getResources().getBoolean(R.bool.config_deviceSupportsSpeakerGain)) {
            findPreference(KEY_SPEAKER_GAIN).setVisible(false);
        }

        // Powershare switch
        mPowershareModeSwitch = (SwitchPreference) findPreference(KEY_POWERSHARE);
        if (Utils.fileWritable(nodePowershare)) {
            mPowershareModeSwitch.setEnabled(true);
            mPowershareModeSwitch.setChecked(sharedPrefs.getBoolean(KEY_POWERSHARE, false));
            mPowershareModeSwitch.setOnPreferenceChangeListener(this);
        } else {
            mPowershareModeSwitch.setEnabled(false);
        }

        if (!getResources().getBoolean(R.bool.config_deviceSupportsPowerShare)) {
            findPreference(KEY_POWERSHARE).setVisible(false);
        }

        // OEM wireless charger quiet mode switch
        mQuietModeSwitch = (SwitchPreference) findPreference(KEY_QUIET_MODE);
        if (Utils.fileWritable(nodeQuietMode)) {
            mQuietModeSwitch.setEnabled(true);
            mQuietModeSwitch.setChecked(sharedPrefs.getBoolean(KEY_QUIET_MODE, false));
            mQuietModeSwitch.setOnPreferenceChangeListener(this);
        } else {
            mQuietModeSwitch.setEnabled(false);
        }

        if (!getResources().getBoolean(R.bool.config_deviceSupportsQuietMode)) {
            findPreference(KEY_QUIET_MODE).setVisible(false);
        }

        // Edge limit switch
        mEdgeLimitSwitch = (SwitchPreference) findPreference(KEY_EDGE_LIMIT);
        if (Utils.fileWritable(nodeEdgeLimit)) {
            mEdgeLimitSwitch.setEnabled(true);
            mEdgeLimitSwitch.setChecked(sharedPrefs.getBoolean(KEY_EDGE_LIMIT, false));
            mEdgeLimitSwitch.setOnPreferenceChangeListener(this);
        } else {
            mEdgeLimitSwitch.setEnabled(false);
        }

        if (!getResources().getBoolean(R.bool.config_deviceSupportsDisablingEdgeLimit)) {
            findPreference(KEY_EDGE_LIMIT).setVisible(false);
        }

        // High touch polling rate switch
        mHighTouchPollingRateSwitch = (SwitchPreference) findPreference(KEY_HIGH_TOUCH_POLLING_RATE);
        if (Utils.fileWritable(nodeHighTouchPollingRate)) {
            mHighTouchPollingRateSwitch.setEnabled(true);
            mHighTouchPollingRateSwitch.setChecked(sharedPrefs.getBoolean(KEY_HIGH_TOUCH_POLLING_RATE, false));
            mHighTouchPollingRateSwitch.setOnPreferenceChangeListener(this);
        } else {
            mHighTouchPollingRateSwitch.setEnabled(false);
        }

        if (!getResources().getBoolean(R.bool.config_deviceSupportsHightTouchPollingRate)) {
            findPreference(KEY_HIGH_TOUCH_POLLING_RATE).setVisible(false);
            findPreference(KEY_HIGH_TOUCH_POLLING_RATE_INFO).setVisible(false);
        }

        // Touch gestures
        if (!getResources().getBoolean(R.bool.config_deviceSupportsTouchGestures)) {
            findPreference(KEY_TOUCH_GESTURES).setVisible(false);
        }

        // USB 2.0 fast charge switch
        mUSB2FastChargeSwitch = (SwitchPreference) findPreference(KEY_USB2_FAST_CHARGE);
        if (Utils.fileWritable(nodeUSB2FastCharge)) {
            mUSB2FastChargeSwitch.setEnabled(true);
            mUSB2FastChargeSwitch.setChecked(sharedPrefs.getBoolean(KEY_USB2_FAST_CHARGE, false));
            mUSB2FastChargeSwitch.setOnPreferenceChangeListener(this);
        } else {
            mUSB2FastChargeSwitch.setEnabled(false);
        }

        if (!getResources().getBoolean(R.bool.config_deviceSupportsUSB2FC)) {
            findPreference(KEY_USB2_FAST_CHARGE).setVisible(false);
            findPreference(KEY_USB2_FAST_CHARGE_INFO).setVisible(false);
        }

        // USB-OTG switch
        mUSBOTGSwitch = (SwitchPreference) findPreference(KEY_USB_OTG);
        if (Utils.fileWritable(nodeOTG)) {
            mUSBOTGSwitch.setEnabled(true);
            mUSBOTGSwitch.setChecked(sharedPrefs.getBoolean(KEY_USB_OTG, false));
            mUSBOTGSwitch.setOnPreferenceChangeListener(this);
        } else {
            mUSBOTGSwitch.setEnabled(false);
        }

        if (!getResources().getBoolean(R.bool.config_deviceSupportsOTG)) {
            findPreference(KEY_USB_OTG).setVisible(false);
        }

        // Vibrator strength preference
        mVibratorStrengthPreference =  (CustomSeekBarPreference) findPreference(KEY_VIBRATOR_STRENGTH);
        if (Utils.fileWritable(nodeVibratorStrength)) {
            mVibratorStrengthPreference.setValue(sharedPrefs.getInt(KEY_VIBRATOR_STRENGTH,
                Integer.parseInt(Utils.getFileValue(nodeVibratorStrength, VIBRATOR_STRENGTH_DEFAULT))));
            mVibratorStrengthPreference.setOnPreferenceChangeListener(this);
        } else {
            mVibratorStrengthPreference.setEnabled(false);
        }

        if (!getResources().getBoolean(R.bool.config_deviceSupportsVibStrength)) {
            findPreference(KEY_VIBRATOR_STRENGTH).setVisible(false);
        }

        // Remove OPlusExtras categories if none of their preferences are visible
        for (PreferenceCategory category : categories) {
            boolean hasVisiblePreference = false;

            for (int i = 0; i < category.getPreferenceCount(); i++) {
                Preference preference = category.getPreference(i);
                if (preference.isVisible()) {
                    hasVisiblePreference = true;
                    break;
                }
            }

            if (!hasVisiblePreference) {
                getPreferenceScreen().removePreference(category);
            }
        }
    }

    private void initNotificationSliderPreference() {
        registerPreferenceListener(SliderConstants.ALERT_SLIDER_USAGE_KEY);
        registerPreferenceListener(SliderConstants.ALERT_SLIDER_ACTION_TOP_KEY);
        registerPreferenceListener(SliderConstants.ALERT_SLIDER_ACTION_MIDDLE_KEY);
        registerPreferenceListener(SliderConstants.ALERT_SLIDER_ACTION_BOTTOM_KEY);

        ListPreference usagePref = (ListPreference) findPreference(
                SliderConstants.ALERT_SLIDER_USAGE_KEY);
        handleSliderUsageChange(usagePref.getValue());
    }

    private void registerPreferenceListener(String key) {
        Preference p = findPreference(key);
        p.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        // Power efficient workqueue switch
        if  (preference == mPowerEfficientWorkqueueModeSwitch) {
            boolean enabled = (Boolean) newValue;
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPrefs.edit().putBoolean(KEY_POWER_EFFICIENT_WORKQUEUE, enabled).commit();
            Utils.writeValue(nodePowerEfficientWorkqueue, enabled ? "1" : "0");
            return true;
        // MSM touchboost switch
        } else if  (preference == mTouchboostModeSwitch) {
            boolean enabled = (Boolean) newValue;
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPrefs.edit().putBoolean(KEY_TOUCHBOOST, enabled).commit();
            Utils.writeValue(nodeTouchboost, enabled ? "1" : "0");
            return true;
        // Anti-flicker switch
        } else if (preference == mAntiFlickerModeSwitch) {
            boolean enabled = (Boolean) newValue;
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPrefs.edit().putBoolean(KEY_ANTIFLICKER, enabled).commit();
            Utils.writeValue(nodeAntiFlicker, enabled ? "1" : "0");
            return true;
        // High brightness mode switch
        } else if (preference == mHBMModeSwitch) {
            boolean enabled = (Boolean) newValue;
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPrefs.edit().putBoolean(KEY_HBM, enabled).commit();
            Utils.writeValue(nodeHBM, enabled ? "1" : "0");
            return true;
        // Maximum brightness preference
        } else if (preference == mMaxBrightnessPreference) {
            int value = Integer.parseInt(newValue.toString());
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPrefs.edit().putInt(KEY_MAX_BRIGHTNESS, value).commit();
            Utils.writeValue(nodeMaxBrightness, String.valueOf(value));
            return true;
        // Keep PCC enabled switch
        } else if  (preference == mKeepPCCModeSwitch) {
            boolean enabled = (Boolean) newValue;
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPrefs.edit().putBoolean(KEY_KEEP_PCC, enabled).commit();
            Utils.writeValue(nodeKeepPCC, enabled ? "1" : "0");
            return true;
        // Fsync switch
        } else if (preference == mFSyncSwitch) {
            boolean enabled = (Boolean) newValue;
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPrefs.edit().putBoolean(KEY_FSYNC, enabled).commit();
    	    Utils.writeValue(nodeFSync, enabled ? "1" : "0");
            return true;
        // Adrenoboost preference
        } else if (preference == mAdrenoboostPreference) {
            int value = Integer.parseInt(newValue.toString());
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPrefs.edit().putInt(KEY_ADRENOBOOST, value).commit();
            Utils.writeValue(nodeAdrenoboost, String.valueOf(value));
            return true;
        // Powershare switch
        } else if (preference == mPowershareModeSwitch) {
            boolean enabled = (Boolean) newValue;
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPrefs.edit().putBoolean(KEY_POWERSHARE, enabled).commit();
            Utils.writeValue(nodePowershare, enabled ? "1" : "0");
            return true;
        // OEM wireless charger quiet mode switch
        } else if (preference == mQuietModeSwitch) {
            boolean enabled = (Boolean) newValue;
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPrefs.edit().putBoolean(KEY_QUIET_MODE, enabled).commit();
            Utils.writeValue(nodeQuietMode, enabled ? "1" : "0");
            return true;
        // Mic gain preference
        } else if (preference == mMicGainPreference) {
            int value = Integer.parseInt(newValue.toString());
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPrefs.edit().putInt(KEY_MIC_GAIN, value).commit();
            Utils.writeValue(nodeMicGain, String.valueOf(value));
            return true;
        // Speaker gain preference
        } else if (preference == mSpeakerGainPreference) {
            int value = Integer.parseInt(newValue.toString());
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPrefs.edit().putInt(KEY_SPEAKER_GAIN, value).commit();
            Utils.writeValue(nodeSpeakerGain, String.valueOf(value));
            return true;
        // Edge limit switch
        } else if (preference == mEdgeLimitSwitch) {
            boolean enabled = (Boolean) newValue;
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPrefs.edit().putBoolean(KEY_EDGE_LIMIT, enabled).commit();
            Utils.writeValue(nodeEdgeLimit, enabled ? "1" : "0");
            return true;
        // High touch polling rate switch
        } else if (preference == mHighTouchPollingRateSwitch) {
            boolean enabled = (Boolean) newValue;
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPrefs.edit().putBoolean(KEY_HIGH_TOUCH_POLLING_RATE, enabled).commit();
            Utils.writeValue(nodeHightTouchPollingRate, enabled ? "1" : "0");
            return true;
        // USB 2.0 fast charge switch
        } else if (preference == mUSB2FastChargeSwitch) {
            boolean enabled = (Boolean) newValue;
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPrefs.edit().putBoolean(KEY_USB2_FAST_CHARGE, enabled).commit();
            Utils.writeValue(nodeUSB2FastCharge, enabled ? "1" : "0");
            return true;
        // USB-OTG switch
        } else if (preference == mUSBOTGSwitch) {
            boolean enabled = (Boolean) newValue;
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPrefs.edit().putBoolean(KEY_USB_OTG, enabled).commit();
    	    Utils.writeValue(nodeOTG, enabled ? "1" : "0");
            return true;
        // Vibrator strength preference
        } else if (preference == mVibratorStrengthPreference) {
            int value = Integer.parseInt(newValue.toString());
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPrefs.edit().putInt(KEY_VIBRATOR_STRENGTH, value).commit();
            Utils.writeValue(nodeVibratorStrength, String.valueOf(value));
            mVibrator.vibrate(testVibrationPattern, -1);
            return true;
        }

        String key = preference.getKey();
        switch (key) {
            case SliderConstants.ALERT_SLIDER_USAGE_KEY:
                return handleSliderUsageChange((String) newValue) &&
                        handleSliderUsageDefaultsChange((String) newValue) &&
                        notifySliderUsageChange((String) newValue);
            case SliderConstants.ALERT_SLIDER_ACTION_TOP_KEY:
                return notifySliderActionChange(0, (String) newValue);
            case SliderConstants.ALERT_SLIDER_ACTION_MIDDLE_KEY:
                return notifySliderActionChange(1, (String) newValue);
            case SliderConstants.ALERT_SLIDER_ACTION_BOTTOM_KEY:
                return notifySliderActionChange(2, (String) newValue);
            default:
                break;
        }

        String node = SliderConstants.sBooleanNodePreferenceMap.get(key);
        if (!TextUtils.isEmpty(node) && FileUtils.isFileWritable(node)) {
            Boolean value = (Boolean) newValue;
            FileUtils.writeLine(node, value ? "1" : "0");
            return true;
        }
        node = SliderConstants.sStringNodePreferenceMap.get(key);
        if (!TextUtils.isEmpty(node) && FileUtils.isFileWritable(node)) {
            FileUtils.writeLine(node, (String) newValue);
            return true;
        }

        return false;
    }

    @Override
    public void addPreferencesFromResource(int preferencesResId) {
        super.addPreferencesFromResource(preferencesResId);
        // Initialize node preferences
        for (String pref : SliderConstants.sBooleanNodePreferenceMap.keySet()) {
            SwitchPreference b = (SwitchPreference) findPreference(pref);
            if (b == null) continue;
            String node = SliderConstants.sBooleanNodePreferenceMap.get(pref);
            if (FileUtils.isFileReadable(node)) {
                String curNodeValue = Utils.readOneLine(node);
                b.setChecked(curNodeValue.equals("1"));
                b.setOnPreferenceChangeListener(this);
            } else {
                removePref(b);
            }
        }
        for (String pref : SliderConstants.sStringNodePreferenceMap.keySet()) {
            ListPreference l = (ListPreference) findPreference(pref);
            if (l == null) continue;
            String node = SliderConstants.sStringNodePreferenceMap.get(pref);
            if (FileUtils.isFileReadable(node)) {
                l.setValue(Utils.readOneLine(node));
                l.setOnPreferenceChangeListener(this);
            } else {
                removePref(l);
            }
        }
    }

    private void removePref(Preference pref) {
        PreferenceGroup parent = pref.getParent();
        if (parent == null) {
            return;
        }
        parent.removePreference(pref);
        if (parent.getPreferenceCount() == 0) {
            removePref(parent);
        }
    }

    private boolean handleSliderUsageChange(String newValue) {
        switch (newValue) {
            case SliderConstants.ALERT_SLIDER_FOR_NOTIFICATION:
                return updateSliderActions(
                        R.array.alert_slider_mode_entries,
                        R.array.alert_slider_mode_entry_values);
            case SliderConstants.ALERT_SLIDER_FOR_FLASHLIGHT:
                return updateSliderActions(
                        R.array.alert_slider_flashlight_entries,
                        R.array.alert_slider_flashlight_entry_values);
            case SliderConstants.ALERT_SLIDER_FOR_BRIGHTNESS:
                return updateSliderActions(
                        R.array.alert_slider_brightness_entries,
                        R.array.alert_slider_brightness_entry_values);
            case SliderConstants.ALERT_SLIDER_FOR_ROTATION:
                return updateSliderActions(
                        R.array.alert_slider_rotation_entries,
                        R.array.alert_slider_rotation_entry_values);
            case SliderConstants.ALERT_SLIDER_FOR_RINGER:
                return updateSliderActions(
                        R.array.alert_slider_ringer_entries,
                        R.array.alert_slider_ringer_entry_values);
            case SliderConstants.ALERT_SLIDER_FOR_NOTIFICATION_RINGER:
                return updateSliderActions(
                        R.array.notification_ringer_slider_mode_entries,
                        R.array.notification_ringer_slider_mode_entry_values);
            case SliderConstants.ALERT_SLIDER_FOR_EXTRADIM:
                return updateSliderActions(
                        R.array.alert_slider_extradim_entries,
                        R.array.alert_slider_extradim_entry_values);
            case SliderConstants.ALERT_SLIDER_FOR_NIGHTLIGHT:
                return updateSliderActions(
                        R.array.alert_slider_nightlight_entries,
                        R.array.alert_slider_nightlight_entry_values);
            case SliderConstants.ALERT_SLIDER_FOR_REFRESH:
                return updateSliderActions(
                        R.array.alert_slider_refresh_entries,
                        R.array.alert_slider_refresh_entry_values);
            case SliderConstants.ALERT_SLIDER_FOR_COLORSPACE:
                return updateSliderActions(
                        R.array.alert_slider_colorspace_entries,
                        R.array.alert_slider_colorspace_entry_values);
            default:
                return false;
        }
    }

    private boolean handleSliderUsageDefaultsChange(String newValue) {
        int defaultsResId = getDefaultResIdForUsage(newValue);
        if (defaultsResId == 0) {
            return false;
        }
        return updateSliderActionDefaults(defaultsResId);
    }

    private boolean updateSliderActions(int entriesResId, int entryValuesResId) {
        String[] entries = getResources().getStringArray(entriesResId);
        String[] entryValues = getResources().getStringArray(entryValuesResId);
        return updateSliderPreference(SliderConstants.ALERT_SLIDER_ACTION_TOP_KEY,
                entries, entryValues) &&
            updateSliderPreference(SliderConstants.ALERT_SLIDER_ACTION_MIDDLE_KEY,
                    entries, entryValues) &&
            updateSliderPreference(SliderConstants.ALERT_SLIDER_ACTION_BOTTOM_KEY,
                    entries, entryValues);
    }

    private boolean updateSliderActionDefaults(int defaultsResId) {
        String[] defaults = getResources().getStringArray(defaultsResId);
        if (defaults.length != 3) {
            return false;
        }

        return updateSliderPreferenceValue(SliderConstants.ALERT_SLIDER_ACTION_TOP_KEY,
                defaults[0]) &&
            updateSliderPreferenceValue(SliderConstants.ALERT_SLIDER_ACTION_MIDDLE_KEY,
                    defaults[1]) &&
            updateSliderPreferenceValue(SliderConstants.ALERT_SLIDER_ACTION_BOTTOM_KEY,
                    defaults[2]);
    }

    private boolean updateSliderPreference(CharSequence key,
            String[] entries, String[] entryValues) {
        ListPreference pref = (ListPreference) findPreference(key);
        if (pref == null) {
            return false;
        }
        pref.setEntries(entries);
        pref.setEntryValues(entryValues);
        return true;
    }

    private boolean updateSliderPreferenceValue(CharSequence key,
            String value) {
        ListPreference pref = (ListPreference) findPreference(key);
        if (pref == null) {
            return false;
        }
        pref.setValue(value);
        return true;
    }

    private int[] getCurrentSliderActions() {
        int[] actions = new int[3];
        ListPreference p;

        p = (ListPreference) findPreference(
                SliderConstants.ALERT_SLIDER_ACTION_TOP_KEY);
        actions[0] = Integer.parseInt(p.getValue());

        p = (ListPreference) findPreference(
               SliderConstants.ALERT_SLIDER_ACTION_MIDDLE_KEY);
        actions[1] = Integer.parseInt(p.getValue());

        p = (ListPreference) findPreference(
                SliderConstants.ALERT_SLIDER_ACTION_BOTTOM_KEY);
        actions[2] = Integer.parseInt(p.getValue());

        return actions;
    }

    private boolean notifySliderUsageChange(String usage) {
        sendUpdateBroadcast(getActivity().getApplicationContext(), Integer.parseInt(usage),
                getCurrentSliderActions());
        return true;
    }

    private boolean notifySliderActionChange(int index, String value) {
        ListPreference p = (ListPreference) findPreference(
                SliderConstants.ALERT_SLIDER_USAGE_KEY);
        int usage = Integer.parseInt(p.getValue());

        int[] actions = getCurrentSliderActions();
        actions[index] = Integer.parseInt(value);

        sendUpdateBroadcast(getActivity().getApplicationContext(), usage, actions);
        return true;
    }

    public static void sendUpdateBroadcast(Context context,
            int usage, int[] actions) {
        Intent intent = new Intent(SliderConstants.ACTION_UPDATE_SLIDER_SETTINGS);
        intent.putExtra(SliderConstants.EXTRA_SLIDER_USAGE, usage);
        intent.putExtra(SliderConstants.EXTRA_SLIDER_ACTIONS, actions);
        intent.setFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
        context.sendBroadcastAsUser(intent, UserHandle.CURRENT);
        Log.d(TAG, "update slider usage " + usage + " with actions: " +
                Arrays.toString(actions));
    }

    public static void restoreSliderStates(Context context) {
        Resources res = context.getResources();
        SharedPreferences prefs = context.getSharedPreferences(
                context.getPackageName() + "_preferences", Context.MODE_PRIVATE);

        String usage = prefs.getString(SliderConstants.ALERT_SLIDER_USAGE_KEY,
                res.getString(R.string.config_defaultAlertSliderUsage));

        int defaultsResId = getDefaultResIdForUsage(usage);
        if (defaultsResId == 0) {
            return;
        }

        String[] defaults = res.getStringArray(defaultsResId);
        if (defaults.length != 3) {
            return;
        }

        String actionTop = prefs.getString(
                SliderConstants.ALERT_SLIDER_ACTION_TOP_KEY, defaults[0]);

        String actionMiddle = prefs.getString(
                SliderConstants.ALERT_SLIDER_ACTION_MIDDLE_KEY, defaults[1]);

        String actionBottom = prefs.getString(
                SliderConstants.ALERT_SLIDER_ACTION_BOTTOM_KEY, defaults[2]);

        prefs.edit()
            .putString(SliderConstants.ALERT_SLIDER_USAGE_KEY, usage)
            .putString(SliderConstants.ALERT_SLIDER_ACTION_TOP_KEY, actionTop)
            .putString(SliderConstants.ALERT_SLIDER_ACTION_MIDDLE_KEY, actionMiddle)
            .putString(SliderConstants.ALERT_SLIDER_ACTION_BOTTOM_KEY, actionBottom)
            .commit();

        sendUpdateBroadcast(context, Integer.parseInt(usage), new int[] {
            Integer.parseInt(actionTop),
            Integer.parseInt(actionMiddle),
            Integer.parseInt(actionBottom)
        });
    }

    private static int getDefaultResIdForUsage(String usage) {
        switch (usage) {
            case SliderConstants.ALERT_SLIDER_FOR_NOTIFICATION:
                return R.array.config_defaultSliderActionsForNotification;
            case SliderConstants.ALERT_SLIDER_FOR_FLASHLIGHT:
                return R.array.config_defaultSliderActionsForFlashlight;
            case SliderConstants.ALERT_SLIDER_FOR_BRIGHTNESS:
                return R.array.config_defaultSliderActionsForBrightness;
            case SliderConstants.ALERT_SLIDER_FOR_ROTATION:
                return R.array.config_defaultSliderActionsForRotation;
            case SliderConstants.ALERT_SLIDER_FOR_RINGER:
                return R.array.config_defaultSliderActionsForRinger;
            case SliderConstants.ALERT_SLIDER_FOR_NOTIFICATION_RINGER:
                return R.array.config_defaultSliderActionsForNotificationRinger;
            case SliderConstants.ALERT_SLIDER_FOR_EXTRADIM:
                return R.array.config_defaultSliderActionsForExtraDim;
            case SliderConstants.ALERT_SLIDER_FOR_NIGHTLIGHT:
                return R.array.config_defaultSliderActionsForNightLight;
            case SliderConstants.ALERT_SLIDER_FOR_REFRESH:
                return R.array.config_defaultSliderActionsForRefresh;
            case SliderConstants.ALERT_SLIDER_FOR_COLORSPACE:
                return R.array.config_defaultSliderActionsForColorSpace;
            default:
                return 0;
        }
    }

    // Power efficient workqueue switch
    public static void restorePowerEfficientWorkqueueSetting(Context context) {
        if (Utils.fileWritable(nodePowerEfficientWorkqueue)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            boolean value = sharedPrefs.getBoolean(KEY_POWER_EFFICIENT_WORKQUEUE, false);
            Utils.writeValue(nodePowerEfficientWorkqueue, value ? "1" : "0");
        }
    }

    // MSM touchboost switch
    public static void restoreTouchboostSetting(Context context) {
        if (Utils.fileWritable(nodeTouchboost)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            boolean value = sharedPrefs.getBoolean(KEY_TOUCHBOOST, false);
            Utils.writeValue(nodeTouchboost, value ? "1" : "0");
        }
    }

    // Anti-flicker switch
    public static void restoreAntiFlickerSetting(Context context) {
        if (Utils.fileWritable(nodeAntiFlicker)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            boolean value = sharedPrefs.getBoolean(KEY_ANTIFLICKER, false);
            Utils.writeValue(nodeAntiFlicker, value ? "1" : "0");
        }
    }

    // High brightness mode switch
    public static void restoreHBMSetting(Context context) {
        if (Utils.fileWritable(nodeHBM)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            boolean value = sharedPrefs.getBoolean(KEY_HBM, false);
            Utils.writeValue(nodeHBM, value ? "1" : "0");
        }
    }

    // Keep PCC enabled switch
    public static void restoreKeepPCCSetting(Context context) {
        if (Utils.fileWritable(nodeKeepPCC)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            boolean value = sharedPrefs.getBoolean(KEY_KEEP_PCC, false);
            Utils.writeValue(nodeKeepPCC, value ? "1" : "0");
        }
    }

    // Maximum brightness preference
    public static void restoreMaxBrightnessSetting(Context context) {
        if (Utils.fileWritable(nodeMaxBrightness)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            int value = sharedPrefs.getInt(KEY_MAX_BRIGHTNESS,
                Integer.parseInt(Utils.getFileValue(nodeMaxBrightness, MAX_BRIGHTNESS_DEFAULT)));
            Utils.writeValue(nodeMaxBrightness, String.valueOf(value));
        }
    }

    // Fsync switch
    public static void restoreFSyncSetting(Context context) {
        if (Utils.fileWritable(nodeFSync)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            boolean value = sharedPrefs.getBoolean(KEY_FSYNC, true);
            Utils.writeValue(nodeFSync, value ? "1" : "0");
        }
    }

    // Adrenoboost preference
    public static void restoreAdrenoboostSetting(Context context) {
        if (Utils.fileWritable(nodeAdrenoboost)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            int value = sharedPrefs.getInt(KEY_ADRENOBOOST,
                Integer.parseInt(Utils.getFileValue(nodeAdrenoboost, ADRENOBOOST_DEFAULT)));
            Utils.writeValue(nodeAdrenoboost, String.valueOf(value));
        }
    }

    // Powershare switch
    public static void restorePowershareSetting(Context context) {
        if (Utils.fileWritable(nodePowershare)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            boolean value = sharedPrefs.getBoolean(KEY_POWERSHARE, false);
            Utils.writeValue(nodePowershare, value ? "1" : "0");
        }
    }

    // OEM wireless charger quiet mode switch
    public static void restoreQuietModeSetting(Context context) {
        if (Utils.fileWritable(nodeQuietMode)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            boolean value = sharedPrefs.getBoolean(KEY_QUIET_MODE, false);
            Utils.writeValue(nodeQuietMode, value ? "1" : "0");
        }
    }

    // Mic gain preference
    public static void restoreMicGainSetting(Context context) {
        if (Utils.fileWritable(nodeMicGain)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            int value = sharedPrefs.getInt(KEY_MIC_GAIN,
                Integer.parseInt(Utils.getFileValue(nodeMicGain, MIC_GAIN_DEFAULT)));
            Utils.writeValue(nodeMicGain, String.valueOf(value));
        }
    }

    // Speaker gain preference
    public static void restoreSpeakerGainSetting(Context context) {
        if (Utils.fileWritable(nodeSpeakerGain)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            int value = sharedPrefs.getInt(KEY_SPEAKER_GAIN,
                Integer.parseInt(Utils.getFileValue(nodeSpeakerGain, SPEAKER_GAIN_DEFAULT)));
            Utils.writeValue(nodeSpeakerGain, String.valueOf(value));
        }
    }

    // Edge limit switch
    public static void restoreEdgeLimitSetting(Context context) {
        if (Utils.fileWritable(nodeEdgeLimit)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            boolean value = sharedPrefs.getBoolean(KEY_EDGE_LIMIT, false);
            Utils.writeValue(nodeEdgeLimit, value ? "1" : "0");
        }
    }

    // High touch polling rate switch
    public static void restoreHighTouchPollingRateSetting(Context context) {
        if (Utils.fileWritable(nodeHighTouchPollingRate)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            boolean value = sharedPrefs.getBoolean(KEY_HIGH_TOUCH_POLLING_RATE, false);
            Utils.writeValue(nodeHighTouchPollingRate, value ? "1" : "0");
        }
    }

    // USB 2.0 fast charge switch
    public static void restoreFastChargeSetting(Context context) {
        if (Utils.fileWritable(nodeUSB2FastCharge)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            boolean value = sharedPrefs.getBoolean(KEY_USB2_FAST_CHARGE, false);
            Utils.writeValue(nodeUSB2FastCharge, value ? "1" : "0");
        }
    }

    // USB-OTG switch
    public static void restoreOTGSetting(Context context) {
        if (Utils.fileWritable(nodeOTG)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            boolean value = sharedPrefs.getBoolean(KEY_USB_OTG, false);
            Utils.writeValue(nodeOTG, value ? "1" : "0");
        }
    }

    // Vibrator strength preference
    public static void restoreVibratorStrengthSetting(Context context) {
        if (Utils.fileWritable(nodeVibratorStrength)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            int value = sharedPrefs.getInt(KEY_VIBRATOR_STRENGTH,
                Integer.parseInt(Utils.getFileValue(nodeVibratorStrength, VIBRATOR_STRENGTH_DEFAULT)));
            Utils.writeValue(nodeVibratorStrength, String.valueOf(value));
        }
    }

    public static class WarningDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.oplus_extras_warning_title)
                    .setMessage(R.string.oplus_extras_warning_text)
                    .setNegativeButton(R.string.oplus_extras_dialog, (dialog, which) -> dialog.cancel())
                    .create();
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            getActivity().getSharedPreferences("main", Activity.MODE_PRIVATE)
                    .edit()
                    .putBoolean("first_warning_shown", true)
                    .commit();
        }
    }

    private void showWarning() {
        WarningDialogFragment fragment = new WarningDialogFragment();
        fragment.show(getFragmentManager(), "warning_dialog");
    }
}
