package com.prome.simplenote;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kingme on 2017/11/24.
 */

public class NotePublicActivity extends Fragment{

    private Context context;
    private View view;

    private RecyclerView recyclerView;
    private static List<NotePublic> notePublicList=new ArrayList<>();
    private static NotePublicAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_note_public,container,false);
        init();
        //noteListInit();
        //noteListShow();
        return view;
    }

    private void init(){
        recyclerView=(RecyclerView)view.findViewById(R.id.note_public_recyclerview);
    }

    private static void noteListInit(){
        notePublicList.clear();
        notePublicList= DataSupport.findAll(NotePublic.class);
    }

    private void noteListShow(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new NotePublicAdapter(notePublicList);
        recyclerView.setAdapter(adapter);
    }

    public static void rm(int position){
        Log.d("prome",notePublicList.get(position).getContent());
        notePublicList.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onStart() {
        super.onStart();
        noteListInit();
        noteListShow();
    }
}
