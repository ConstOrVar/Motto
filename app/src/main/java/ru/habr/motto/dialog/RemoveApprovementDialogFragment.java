package ru.habr.motto.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.io.Serializable;

import ru.habr.motto.R;

/**
 * Created by komar on 11/6/17.
 */

public class RemoveApprovementDialogFragment extends DialogFragment {
    private static final String ARG_DELEGATE_PROVIDER = "delegate_provider";

    public interface DelegateProvider extends Serializable {
        Delegate get(DialogFragment dialogFragment);
    }

    public interface Delegate {
        void onApproved();
        void onCancelled();
    }

    public static final DialogFragment newInstance(DelegateProvider delegate) {
        DialogFragment dialogFragment = new RemoveApprovementDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_DELEGATE_PROVIDER, delegate);
        dialogFragment.setArguments(args);

        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        if(args == null || !args.containsKey(ARG_DELEGATE_PROVIDER)) {
            throw new IllegalStateException("Delegate provider is missing");
        }

        DelegateProvider delegateProvider =
                (DelegateProvider) args.getSerializable(ARG_DELEGATE_PROVIDER);
        Delegate delegate = delegateProvider.get(this);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_remove_motto_title)
                .setMessage(R.string.dialog_remove_motto_message)
                .setPositiveButton(android.R.string.yes,
                        (dialog, btn) -> delegate.onApproved())
                .setNegativeButton(android.R.string.no,
                        (dialog, btn) -> delegate.onCancelled())
                .create();
    }
}
