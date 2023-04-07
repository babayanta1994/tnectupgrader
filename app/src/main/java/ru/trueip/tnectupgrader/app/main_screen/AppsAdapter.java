package ru.trueip.tnectupgrader.app.main_screen;

import android.content.Context;
import android.support.annotation.LayoutRes;

import java.util.List;

import ru.trueip.tnectupgrader.app.App;
import ru.trueip.tnectupgrader.base.adapters.BindingRecyclerAdapter;
import ru.trueip.tnectupgrader.databinding.AppViewBinding;
import ru.trueip.tnectupgrader.models.Apps;

public class AppsAdapter extends BindingRecyclerAdapter<Apps> {
    private final static String TAG = AppsAdapter.class.getSimpleName();
    private Context context;

    private boolean isUpdateMode = false;

    public AppsAdapter(@LayoutRes Integer holderLayout, List<Apps> items){
        super(holderLayout, items);
        this.context = App.getContext();
    }

    public void setEditMode(boolean isUpdate) {
        this.isUpdateMode = isUpdate;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final BindingRecyclerAdapter.BindingHolder holder, final int position) {
        final Apps apps = items.get(holder.getAdapterPosition());
        AppViewBinding bind = (AppViewBinding) holder.getBinding();

        bind.appName.setText(apps.getName());
        bind.currentVersion.setText(apps.getCurVersion());
        bind.lastVersion.setText(apps.getLastVersion());
        bind.upgrade.setEnabled(!isUpdateMode);
        bind.description.setText(apps.getDescription());


        bind.upgrade.setOnClickListener(view -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(holder.getAdapterPosition(), apps);
            }
        });




        /*
         bind.getRoot().setOnClickListener(view -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(holder.getAdapterPosition(), contact);
            }
        });

        bind.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener!= null) {
                    mOnItemLongClickListener.onItemLongClicked(holder.getAdapterPosition(), contact);
                    return true;
                }
                return false;
            }
        });
        */
    }



}