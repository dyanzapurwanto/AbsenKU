package com.example.absenku;

public class Kelas {
    public String nama_kelas;
    public String hari;
    public String kode_kelas;
    public String jam_kelas;
    public String absen;

    public Kelas(){

    }

    public Kelas(String nama_kelas, String hari, String kode_kelas, String jam_kelas, String absen)
    {
        this.nama_kelas = nama_kelas;
        this.hari = hari;
        this.kode_kelas = kode_kelas;
        this.jam_kelas = jam_kelas;
        this.absen = absen;
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

    public String getJam_kelas() {
        return jam_kelas;
    }

    public void setJam_kelas(String jam_kelas) {
        this.jam_kelas = jam_kelas;
    }

    public String getAbsen() {
        return absen;
    }

    public void setAbsen(String absen) {
        this.absen = absen;
    }
}
