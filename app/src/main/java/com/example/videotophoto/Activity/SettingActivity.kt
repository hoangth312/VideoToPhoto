package com.example.videotophoto.Activity

import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.preference.RingtonePreference
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.videotophoto.R
import kotlinx.android.synthetic.main.activity_setting.*


@Suppress("DEPRECATION")
class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        if (fragment_container != null) {
            if (savedInstanceState != null)
                return;
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, SettingsFragment()).commit()
        }


    }


    class SettingsFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.preferences)
            bindPreferenceSummaryToValue(findPreference("list_file_format"))
            bindPreferenceSummaryToValue(findPreference("list_quality"))
            bindPreferenceSummaryToValue(findPreference("list_size"))

        }



        private val sBindPreferenceSummaryToValueListener =
            Preference.OnPreferenceChangeListener { preference, value ->

                val stringValue = value.toString()
                if (preference is ListPreference) {

                    val listPreference = preference
                    val index = listPreference.findIndexOfValue(stringValue)
                    preference.setSummary(
                        if (index >= 0)
                            listPreference.entries[index]
                        else
                            null)

                } else if (preference is RingtonePreference) {

                    if (TextUtils.isEmpty(stringValue)) {
                        preference.setSummary("Silent")
                    } else {
                        val ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue))

                        if (ringtone == null) {
                            preference.setSummary(null)
                        } else {
                            val name = ringtone.getTitle(preference.getContext())
                            preference.setSummary(name)
                        }
                    }
                }
                else {
                    preference.summary = stringValue
                }
                true
            }

        private fun bindPreferenceSummaryToValue(preference: Preference?) {

            preference?.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener

            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                    .getDefaultSharedPreferences(preference?.context)
                    .getString(preference?.key, ""))
        }
    }
}