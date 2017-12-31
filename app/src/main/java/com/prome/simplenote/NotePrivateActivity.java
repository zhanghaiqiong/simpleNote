package com.prome.simplenote;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import skin.support.SkinCompatManager;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by kingme on 2017/11/24.
 */

public class NotePrivateActivity extends Fragment{
    private Context context;
    private View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_note_private,container,false);
        init();
        return view;
    }

    private void init(){

    }
}
