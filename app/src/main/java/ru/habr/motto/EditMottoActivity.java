package ru.habr.motto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import java.io.Serializable;

/**
 * Created by komar on 11/6/17.
 */

public class EditMottoActivity extends AppCompatActivity {
    private static final String ARG_MOTTO = "motto";
    private static final String ARG_DELEGATE = "delegate";

    private Delegate delegate;
    private String motto;

    private EditText inputMotto;
    private Button btnSave;

    public interface Delegate extends Serializable {
        void onMottoChanged(Activity activity, String motto);
    }

    public static Intent createIntent(Context context, String motto, Delegate delegate) {
        Intent intent = new Intent(context, EditMottoActivity.class);
        intent.putExtra(ARG_MOTTO, motto);
        intent.putExtra(ARG_DELEGATE, delegate);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_motto);

        setTitle(R.string.activity_edit_motto_title);

        Bundle args = getIntent().getExtras();
        if(args == null || !args.containsKey(ARG_DELEGATE)) {
            throw new IllegalStateException("Delegate is missing");
        }

        motto = args.getString(ARG_MOTTO, "");
        delegate = (Delegate) args.getSerializable(ARG_DELEGATE);

        inputMotto = findViewById(R.id.input_motto);
        btnSave = findViewById(R.id.btn_save);

        inputMotto.setText(motto);
        btnSave.setOnClickListener(v -> delegate
                .onMottoChanged(EditMottoActivity.this, inputMotto.getText().toString()));
    }
}
