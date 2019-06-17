package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.databinding.ActivitySettingDetailBinding;

public class SettingDetailActivity extends AppCompatActivity {

    private ActivitySettingDetailBinding binding;
    private SharedPreferences preferences;

    private final String preferencePortKey = "port";
    private final String preferenceSpeedKey = "speed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting_detail);

        final String preferenceKey = "udp_config";
        preferences = getSharedPreferences(preferenceKey, Context.MODE_PRIVATE);

        setSupportActionBar(binding.settingDetailToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        int port = preferences.getInt(preferencePortKey, 50000);
        binding.settingDetailPortTextEdit.setText(String.valueOf(port));

        int speed = preferences.getInt(preferenceSpeedKey, 80);
        binding.settingDetailSpeedTextEdit.setText(String.valueOf(speed));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveSettings();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveSettings();
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(preferencePortKey, Integer.valueOf(binding.settingDetailPortTextEdit.getText().toString()));
        editor.putInt(preferenceSpeedKey, Integer.valueOf(binding.settingDetailSpeedTextEdit.getText().toString()));
        editor.apply();
    }
}
