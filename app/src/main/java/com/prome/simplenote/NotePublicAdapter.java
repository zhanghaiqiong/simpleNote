package com.prome.simplenote;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by kingme on 2017/11/24.
 */

public class NotePublicAdapter extends RecyclerView.Adapter<NotePublicAdapter.ViewHolder> {
    private Context mContext;
    private List<NotePublic> mNotePublicList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView noteContent;
        TextView noteDate;
        ImageButton noteMenu;

        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            noteContent=(TextView)view.findViewById(R.id.item_public_textContent);
            noteDate=(TextView)view.findViewById(R.id.item_public_textDate);
            noteMenu=(ImageButton)view.findViewById(R.id.item_public_imageButton_menu);
        }
    }

    public NotePublicAdapter(List<NotePublic> notePublicList){
        mNotePublicList=notePublicList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_note_public,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                NotePublic notePublic=mNotePublicList.get(position);
                Intent intent=new Intent(mContext,EditActivity.class);
                intent.putExtra("mode",EditActivity.EDIT_MODE_EDIT);
                intent.putExtra("name",notePublic.getName());
                mContext.startActivity(intent);
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                popupMenu(holder.noteMenu,mNotePublicList.size(),holder.getAdapterPosition(),mNotePublicList.get(holder.getAdapterPosition()).getName());
                return true;
            }
        });
        holder.noteMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu(view,mNotePublicList.size(),holder.getAdapterPosition(),mNotePublicList.get(holder.getAdapterPosition()).getName());
            }
        });
        return holder;
    }

    public void popupMenu(final View v,final int listSize, final int position, final String name){
        PopupMenu popupMenu=new PopupMenu(mContext,v);
        Menu menu=popupMenu.getMenu();
        MenuInflater inflater=popupMenu.getMenuInflater();
        inflater.inflate(R.menu.note_public_menu,menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_note_public_top:

                        break;
                    case R.id.menu_note_public_del:
                        DataSupport.deleteAll(NotePublic.class,"name=?",name);
                        NotePublicActivity.rm(position);
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NotePublic notePublic=mNotePublicList.get(position);
        holder.noteContent.setText(notePublic.getContent());
        holder.noteDate.setText(notePublic.getDate());
    }

    @Override
    public int getItemCount() {
        return mNotePublicList.size();
    }
}
