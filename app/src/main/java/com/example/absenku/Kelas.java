package com.example.absenku;

public class Kelas {
    public String nama_kelas;
    public String hari;
    public String kode_kelas;

    public Kelas(){

    }

    public Kelas(String nama_kelas, String hari, String kode_kelas)
    {
        this.nama_kelas = nama_kelas;
        this.hari = hari;
        this.kode_kelas = kode_kelas;
    }

    public String getNama_kelas() {
        return nama_kelas;
    }

    public void setNama_kelas(String nama_kelas) {
        this.nama_kelas = nama_kelas;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public String getKode_kelas() {
        return kode_kelas;
    }

    public void setKode_kelas(String kode_kelas) {
        this.kode_kelas = kode_kelas;
    }
}
