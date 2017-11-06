package ru.habr.motto;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import ru.habr.motto.dialog.RemoveApprovementDialogFragment;

public class MainActivity extends AppCompatActivity implements RemoveApprovementDialogFragment.Delegate {
    private static final String REMOVE_MOTTO_DIALOG_TAG = "remove_motto_dialog";
    private static final int EDIT_REQUEST = 1001;

    private MottoStorage mottoStorage;
    private TextView txtMotto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mottoStorage = MottoStorage.getInstance(getApplicationContext());

        txtMotto = findViewById(R.id.txt_motto);

        txtMotto.setText(mottoStorage.getMotto());
        txtMotto.setOnClickListener(v -> showEditMottoView());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                showEditMottoView();
                break;
            case R.id.action_remove:
                showRemoveMottoView();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case EDIT_REQUEST:
                if(resultCode == RESULT_OK) {
                    String changedMotto = data.getStringExtra(EditMottoDelegate.CHANGED_MOTTO);
                    mottoStorage.saveMotto(changedMotto);
                    txtMotto.setText(changedMotto);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void showRemoveMottoView() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment editDialog = RemoveApprovementDialogFragment.newInstance(new RemoveApprovementDelegateProvider());
        editDialog.show(ft, REMOVE_MOTTO_DIALOG_TAG);
    }

    private void showEditMottoView() {
        Intent intent = EditMottoActivity.createIntent(getApplicationContext(),
                mottoStorage.getMotto(), new EditMottoDelegate());
        startActivityForResult(intent, EDIT_REQUEST);
    }

    @Override
    public void onApproved() {
        mottoStorage.saveMotto(null);
        txtMotto.setText(null);
    }

    @Override
    public void onCancelled() {
        // nop
    }

    private static class RemoveApprovementDelegateProvider implements RemoveApprovementDialogFragment.DelegateProvider {
        private static final long serialVersionUID = -5986444973089471288L;

        @Override
        public RemoveApprovementDialogFragment.Delegate get(DialogFragment dialogFragment) {
            return (RemoveApprovementDialogFragment.Delegate) dialogFragment.getActivity();
        }
    }

    private static class EditMottoDelegate implements EditMottoActivity.Delegate {
        private static final long serialVersionUID = -4059724486983129176L;
        private static final String CHANGED_MOTTO = "changed_motto";

        @Override
        public void onMottoChanged(Activity activity, String motto) {
            Intent data = new Intent();

            data.putExtra(CHANGED_MOTTO, motto);

            activity.setResult(RESULT_OK, data);
            activity.finish();
        }
    }
}
