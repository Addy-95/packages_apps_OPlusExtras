/*
 * Copyright (C) 2023 The Evolution X Project
 *               2018-2022 crDroid Android Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.evolution.oplus.OPlusExtras.kcal;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceManager;
import androidx.preference.Preference;

import org.evolution.oplus.OPlusExtras.CustomSeekBarPreference;
import org.evolution.oplus.OPlusExtras.R;
import org.evolution.oplus.OPlusExtras.utils.Utils;

public class Kcal extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {
    private static final String TAG = Kcal.class.getSimpleName();

    // Red preference
    private static final String KEY_RED = "red";
    private static final String RED_DEFAULT = "256";
    private CustomSeekBarPreference mRedPreference;

    // Green preference
    private static final String KEY_GREEN = "green";
    private static final String GREEN_DEFAULT = "256";
    private CustomSeekBarPreference mGreenPreference;

    // Blue preference
    private static final String KEY_BLUE = "blue";
    private static final String BLUE_DEFAULT = "256";
    private CustomSeekBarPreference mBluePreference;

    // Saturation preference
    private static final String KEY_SATURATION = "saturation";
    private static final String SATURATION_DEFAULT = "255";
    private CustomSeekBarPreference mSaturationPreference;

    // Contrast preference
    private static final String KEY_CONTRAST = "contrast";
    private static final String CONTRAST_DEFAULT = "255";
    private CustomSeekBarPreference mContrastPreference;

    // Hue preference
    private static final String KEY_HUE = "hue";
    private static final String HUE_DEFAULT = "0";
    private CustomSeekBarPreference mHuePreference;

    // Value preference
    private static final String KEY_VALUE = "value";
    private static final String VALUE_DEFAULT = "255";
    private CustomSeekBarPreference mValuePreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.kcal);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        // Red preference
        String nodeRed = getResources().getString(R.string.node_red_preference);
        mRedPreference =  (CustomSeekBarPreference) findPreference(KEY_RED);
        if (Utils.fileWritable(nodeRed)) {
            mRedPreference.setValue(sharedPrefs.getInt(KEY_RED,
                Integer.parseInt(Utils.getFileValue(nodeRed, RED_DEFAULT))));
            mRedPreference.setOnPreferenceChangeListener(this);
        } else {
            mRedPreference.setEnabled(false);
        }

        // Green preference
        String nodeGreen = getResources().getString(R.string.node_green_preference);
        mGreenPreference =  (CustomSeekBarPreference) findPreference(KEY_GREEN);
        if (Utils.fileWritable(nodeGreen)) {
            mGreenPreference.setValue(sharedPrefs.getInt(KEY_GREEN,
                Integer.parseInt(Utils.getFileValue(nodeGreen, GREEN_DEFAULT))));
            mGreenPreference.setOnPreferenceChangeListener(this);
        } else {
            mGreenPreference.setEnabled(false);
        }

        // Blue preference
        String nodeBlue = getResources().getString(R.string.node_blue_preference);
        mBluePreference =  (CustomSeekBarPreference) findPreference(KEY_BLUE);
        if (Utils.fileWritable(nodeBlue)) {
            mBluePreference.setValue(sharedPrefs.getInt(KEY_BLUE,
                Integer.parseInt(Utils.getFileValue(nodeBlue, BLUE_DEFAULT))));
            mBluePreference.setOnPreferenceChangeListener(this);
        } else {
            mBluePreference.setEnabled(false);
        }

        // Saturation preference
        String nodeSaturation = getResources().getString(R.string.node_saturation_preference);
        mSaturationPreference =  (CustomSeekBarPreference) findPreference(KEY_SATURATION);
        if (Utils.fileWritable(nodeSaturation)) {
            mSaturationPreference.setValue(sharedPrefs.getInt(KEY_SATURATION,
                Integer.parseInt(Utils.getFileValue(nodeSaturation, SATURATION_DEFAULT))));
            mSaturationPreference.setOnPreferenceChangeListener(this);
        } else {
            mSaturationPreference.setEnabled(false);
        }

        // Contrast preference
        String nodeContrast = getResources().getString(R.string.node_contrast_preference);
        mContrastPreference =  (CustomSeekBarPreference) findPreference(KEY_CONTRAST);
        if (Utils.fileWritable(nodeContrast)) {
            mContrastPreference.setValue(sharedPrefs.getInt(KEY_CONTRAST,
                Integer.parseInt(Utils.getFileValue(nodeContrast, CONTRAST_DEFAULT))));
            mContrastPreference.setOnPreferenceChangeListener(this);
        } else {
            mContrastPreference.setEnabled(false);
        }

        // Hue preference
        String nodeHue = getResources().getString(R.string.node_hue_preference);
        mHuePreference =  (CustomSeekBarPreference) findPreference(KEY_HUE);
        if (Utils.fileWritable(nodeHue)) {
            mHuePreference.setValue(sharedPrefs.getInt(KEY_HUE,
                Integer.parseInt(Utils.getFileValue(nodeHue, HUE_DEFAULT))));
            mHuePreference.setOnPreferenceChangeListener(this);
        } else {
            mHuePreference.setEnabled(false);
        }

        // Value preference
        String nodeValue = getResources().getString(R.string.node_value_preference);
        mValuePreference =  (CustomSeekBarPreference) findPreference(KEY_VALUE);
        if (Utils.fileWritable(nodeValue)) {
            mValuePreference.setValue(sharedPrefs.getInt(KEY_VALUE,
                Integer.parseInt(Utils.getFileValue(nodeValue, VALUE_DEFAULT))));
            mValuePreference.setOnPreferenceChangeListener(this);
        } else {
            mValuePreference.setEnabled(false);
        }
    }

    private void registerPreferenceListener(String key) {
        Preference p = findPreference(key);
        p.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        // Red preference
        if (preference == mRedPreference) {
            int value = Integer.parseInt(newValue.toString());
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPrefs.edit().putInt(KEY_RED, value).commit();
            String nodeRed = getContext().getResources().getString(R.string.node_red_preference);
            Utils.writeValue(nodeRed, String.valueOf(value));
            return true;
        // Green preference
        } else if (preference == mGreenPreference) {
            int value = Integer.parseInt(newValue.toString());
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPrefs.edit().putInt(KEY_GREEN, value).commit();
            String nodeGreen = getContext().getResources().getString(R.string.node_green_preference);
            Utils.writeValue(nodeGreen, String.valueOf(value));
            return true;
        // Blue preference
        } else if (preference == mBluePreference) {
            int value = Integer.parseInt(newValue.toString());
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPrefs.edit().putInt(KEY_BLUE, value).commit();
            String nodeBlue = getContext().getResources().getString(R.string.node_blue_preference);
            Utils.writeValue(nodeBlue, String.valueOf(value));
            return true;
        // Saturation preference
        } else if (preference == mSaturationPreference) {
            int value = Integer.parseInt(newValue.toString());
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPrefs.edit().putInt(KEY_SATURATION, value).commit();
            String nodeSaturation = getContext().getResources().getString(R.string.node_saturation_preference);
            Utils.writeValue(nodeSaturation, String.valueOf(value));
            return true;
        // Contrast preference
        } else if (preference == mContrastPreference) {
            int value = Integer.parseInt(newValue.toString());
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPrefs.edit().putInt(KEY_CONTRAST, value).commit();
            String nodeContrast = getContext().getResources().getString(R.string.node_contrast_preference);
            Utils.writeValue(nodeContrast, String.valueOf(value));
            return true;
        // Hue preference
        } else if (preference == mHuePreference) {
            int value = Integer.parseInt(newValue.toString());
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPrefs.edit().putInt(KEY_HUE, value).commit();
            String nodeHue = getContext().getResources().getString(R.string.node_hue_preference);
            Utils.writeValue(nodeHue, String.valueOf(value));
            return true;
        // Value preference
        } else if (preference == mValuePreference) {
            int value = Integer.parseInt(newValue.toString());
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPrefs.edit().putInt(KEY_VALUE, value).commit();
            String nodeValue = getResources().getString(R.string.node_value_preference);
            Utils.writeValue(nodeValue, String.valueOf(value));
            return true;
        }

        return false;
    }

    // Red preference
    public static void restoreRedSetting(Context context) {
        String nodeRed = context.getResources().getString(R.string.node_red_preference);
        if (Utils.fileWritable(nodeRed)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            int value = sharedPrefs.getInt(KEY_RED,
                Integer.parseInt(Utils.getFileValue(nodeRed, RED_DEFAULT)));
            Utils.writeValue(nodeRed, String.valueOf(value));
        }
    }

    // Green preference
    public static void restoreGreenSetting(Context context) {
        String nodeGreen = context.getResources().getString(R.string.node_green_preference);
        if (Utils.fileWritable(nodeGreen)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            int value = sharedPrefs.getInt(KEY_GREEN,
                Integer.parseInt(Utils.getFileValue(nodeGreen, GREEN_DEFAULT)));
            Utils.writeValue(nodeGreen, String.valueOf(value));
        }
    }

    // Blue preference
    public static void restoreBlueSetting(Context context) {
        String nodeBlue = context.getResources().getString(R.string.node_blue_preference);
        if (Utils.fileWritable(nodeBlue)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            int value = sharedPrefs.getInt(KEY_BLUE,
                Integer.parseInt(Utils.getFileValue(nodeBlue, BLUE_DEFAULT)));
            Utils.writeValue(nodeBlue, String.valueOf(value));
        }
    }

    // Saturation preference
    public static void restoreSaturationSetting(Context context) {
        String nodeSaturation = context.getResources().getString(R.string.node_saturation_preference);
        if (Utils.fileWritable(nodeSaturation)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            int value = sharedPrefs.getInt(KEY_SATURATION,
                Integer.parseInt(Utils.getFileValue(nodeSaturation, SATURATION_DEFAULT)));
            Utils.writeValue(nodeSaturation, String.valueOf(value));
        }
    }

    // Contrast preference
    public static void restoreContrastSetting(Context context) {
        String nodeContrast = context.getResources().getString(R.string.node_contrast_preference);
        if (Utils.fileWritable(nodeContrast)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            int value = sharedPrefs.getInt(KEY_CONTRAST,
                Integer.parseInt(Utils.getFileValue(nodeContrast, CONTRAST_DEFAULT)));
            Utils.writeValue(nodeContrast, String.valueOf(value));
        }
    }

    // Hue preference
    public static void restoreHueSetting(Context context) {
        String nodeHue = context.getResources().getString(R.string.node_hue_preference);
        if (Utils.fileWritable(nodeHue)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            int value = sharedPrefs.getInt(KEY_HUE,
                Integer.parseInt(Utils.getFileValue(nodeHue, HUE_DEFAULT)));
            Utils.writeValue(nodeHue, String.valueOf(value));
        }
    }

    // Value preference
    public static void restoreValueSetting(Context context) {
        String nodeValue = context.getResources().getString(R.string.node_value_preference);
        if (Utils.fileWritable(nodeValue)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            int value = sharedPrefs.getInt(KEY_VALUE,
                Integer.parseInt(Utils.getFileValue(nodeValue, VALUE_DEFAULT)));
            Utils.writeValue(nodeValue, String.valueOf(value));
        }
    }
}
