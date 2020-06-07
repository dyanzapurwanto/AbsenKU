package com.example.absenku;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class upKelasAdapter extends RecyclerView.Adapter<upKelasAdapter.ViewHolder> {

    private Context context;
    private List<Kelas> list;

    public upKelasAdapter(Context context, List<Kelas> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.upkelas_item,parent,
                false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Kelas kelas = list.get(position);
        holder.textNama_Kelas.setText(kelas.getNama_kelas());
        holder.textJam_Kelas.setText(kelas.getJam_kelas());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textNama_Kelas, textJam_Kelas;

        public ViewHolder(View itemView) {
            super(itemView);

            textNama_Kelas = itemView.findViewById(R.id.nama_upkelas);
            textJam_Kelas = itemView.findViewById(R.id.jam_upkelas);
        }
    }
}
