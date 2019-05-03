package com.example.imagesaver.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.imagesaver.R;

public class SubFamilyDialogFragment extends DialogFragment {

    private SubFamilyDialogFragment.SelectedItemSubFamily mListener;

    public interface SelectedItemSubFamily{
        void onSelectedSubFamily(int select);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_sub_family_name)
                .setItems(R.array.planet, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onSelectedSubFamily(which);
                    }

                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            this.mListener = (SubFamilyDialogFragment.SelectedItemSubFamily) context;
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
