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
import java.util.ArrayList;

public class ListDialogFragment extends DialogFragment {

    private static final String LIST = "list";
    private ArrayList<String>  mList;
    private SelectedItem mListener;

    public interface SelectedItem{
         void onSelected(int select);
    }

    public static ListDialogFragment newInstance(int type, ArrayList<String> list) {

        Bundle args = new Bundle();
        args.putStringArrayList(LIST, list);

        ListDialogFragment fragment = new ListDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = getArguments().getStringArrayList(LIST);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_supplier_name)
                .setItems(mList.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onSelected(which);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            this.mListener = (SelectedItem) context;
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
