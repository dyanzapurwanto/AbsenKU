package com.example.absenku;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AbsenAdapter extends RecyclerView.Adapter<AbsenAdapter.ViewHolder> {

    private Context context;
    private List<Kelas> list;

    public AbsenAdapter(Context context, List<Kelas> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.absensi_item,parent,
                false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Kelas kelas = list.get(position);
        holder.textNama_Kelas.setText(kelas.getNama_kelas());
        holder.textKode_Kelas.setText(kelas.getJam_kelas());
        Double persenAbsend = Double.parseDouble(kelas.getAbsen())/14*100;
        int persenAbsen = (int) Math.round(persenAbsend);
        holder.progressAbsen.setProgress(persenAbsen);
        holder.textKehadiran.setText(kelas.getAbsen()+"/14 Pertemuan "+ persenAbsen);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textNama_Kelas, textKode_Kelas,textKehadiran;
        public ProgressBar progressAbsen;

        public ViewHolder(View itemView) {
            super(itemView);

            textNama_Kelas = itemView.findViewById(R.id.sts_nama_kelas);
            textKode_Kelas = itemView.findViewById(R.id.sts_kode_kelas);
            textKehadiran = itemView.findViewById(R.id.kehadiran);
            progressAbsen = itemView.findViewById(R.id.progress_absen);

        }
    }
}
