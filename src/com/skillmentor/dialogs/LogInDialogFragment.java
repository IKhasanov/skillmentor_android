package com.skillmentor.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.skillmentor.R;
import com.skillmentor.utils.Consts;

public class LogInDialogFragment extends DialogFragment {
	
	public interface LogInDialogListener {
		public void onDialogLogInClick(DialogFragment dialog, Bundle args);
		public void onDialogCancelClick(DialogFragment dialog);
	}
	
	LogInDialogListener mListener;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View view = inflater.inflate(R.layout.dialog_log_in, null);
		builder.setView(view);
		builder.setPositiveButton(R.string.dialog_btn_log_in, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Bundle args = new Bundle();
				EditText etLogin = (EditText) view.findViewById(R.id.email);
				EditText etPassword = (EditText) view.findViewById(R.id.password);
				args.putString(Consts.loginArg, etLogin.getText().toString());
				args.putString(Consts.passwordArg, etPassword.getText().toString());
				mListener.onDialogLogInClick(LogInDialogFragment.this, args);
			}
		});
		builder.setNegativeButton(R.string.dialog_btn_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mListener.onDialogCancelClick(LogInDialogFragment.this);
			}
		});
		
		return builder.create();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			mListener = (LogInDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement LogInDialogListener");
		}
		
	}
}

