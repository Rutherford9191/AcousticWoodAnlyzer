
package com.example.joshua.acoustics.ViolinTuner;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.joshua.acoustics.R;

public class HelpActivity extends Activity {
    private static final int[] HAS_LINKS = {R.id.changelog, R.id.faq, R.id.website,
                                            R.id.open_source, /*R.id.donate, R.id.acknowledgments,
                                            R.id.translations, */ R.id.contact};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Stranglely disabled by default for API level 14+
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setContentView(R.layout.help);

        TextView tv;
        MovementMethod linkMovement = LinkMovementMethod.getInstance();

        for (int i=0; i < HAS_LINKS.length; i++) {
            tv = (TextView) findViewById(HAS_LINKS[i]);
            tv.setMovementMethod(linkMovement);
            tv.setAutoLinkMask(Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES);
        }

        tv = (TextView) findViewById(R.id.version);
        try {
            tv.setText(getResources().getString(R.string.app_full_name) + " " +
                       getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (Exception e) {
            tv.setText("...");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
