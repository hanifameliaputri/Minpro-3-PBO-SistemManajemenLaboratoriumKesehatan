
package controller;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import view.LaboratoriumView;
import java.util.ArrayList;
import java.util.Scanner;
import model.Analis;
import model.Dokter;
import model.HasilPemeriksaan;
import model.Pasien;
import model.Pemeriksaan;
import model.Petugas;
 
/**
 *
 * @author User
 */
 
public class LaboratoriumController {
 
    private final ArrayList<Pasien> daftarPasien;
    private final ArrayList<Petugas> daftarPetugas; // menampung Analis maupun Dokter (polymorphism)
    private final ArrayList<Pemeriksaan> daftarPemeriksaan;
    private final ArrayList<HasilPemeriksaan> daftarHasil;
 
    private int nextIdPasien;
    private int nextIdPetugas;
    private int nextIdPemeriksaan;
    private int nextIdHasil;
 
    private final LaboratoriumView view;
 
    private Pasien pasienTerdaftar;
    private Pemeriksaan pemeriksaanTerdaftar;
    private Petugas petugasTerdaftar;
 
    public LaboratoriumController() {
        daftarPasien = new ArrayList<>();
        daftarPetugas = new ArrayList<>();
        daftarPemeriksaan = new ArrayList<>();
        daftarHasil = new ArrayList<>();
 
        nextIdPasien = 1;
        nextIdPetugas = 1;
        nextIdPemeriksaan = 1;
        nextIdHasil = 1;
 
        view = new LaboratoriumView();
 
        isiDataAwal();
    }
 
    // Menjalankan program
 
    public void jalankanProgram() {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean berjalan = true;
            
            while (berjalan) {
                view.tampilkanMenuUtama();
                
                int pilihan = bacaInt(scanner);
                
                switch (pilihan) {
                    case 1 -> pendaftaranPemeriksaan(scanner);
                    case 2 -> menuPasien(scanner);
                    case 3 -> menuPetugas(scanner);
                    case 4 -> menuPemeriksaan(scanner);
                    case 5 -> menuHasil(scanner);
                    case 6 -> {
                        view.tampilkanPesan("Program selesai. Terima kasih!");
                        berjalan = false;
                    }
                    default -> view.tampilkanPesan("Pilihan tidak tersedia.");
                }
            }
        }
    }
    public LaboratoriumView getView() {
        return view;
    }
 
// dummy data awal
    private void isiDataAwal() {
    Pasien p1 = tambahPasien( 
            "Aulia Ashylla P ",
            19,
            "Perempuan",
            "Demam dan batuk sejak 3 hari"
    );
 
    Analis a1 = tambahAnalis(
            "Asti Putri",
            29,
            "Laki-Laki",
            "Hematologi"
    );
 
    tambahDokter(
            "dr. Hanif Amelia Putri",
            25,
            "Perempuan",
            "STR-123456789"
    );
 
    Pemeriksaan pm1 = tambahPemeriksaan(
            "Tes Darah Lengkap",
            150000
    );
 
    tambahPemeriksaan(
            "Tes Urine",
            100000
    );
 
    tambahPemeriksaan(
            "Tes Gula Darah",
            75000
    );
 
    tambahHasil(
            p1.getId(),
            pm1.getIdPemeriksaan(),
            a1.getId(),
            "Hemoglobin 13.5 g/dL, Leukosit normal",
            "Normal"
    );
}
 
// pendaftaran pemeriksaan
 
    private void pendaftaranPemeriksaan(Scanner scanner) {
        view.tampilkanJudul("PENDAFTARAN PEMERIKSAAN");
 
        view.tampilkanPilihanPasien();
 
        int pilihanPasien = bacaInt(scanner);
 
        Pasien pasien;
 
        switch (pilihanPasien) {
            case 1 -> {
                view.tampilkanInputPasienBaru();
                System.out.print("Nama (huruf saja): ");
                String nama = bacaNama(scanner);
                System.out.print("Umur (1-120): ");
                int umur = bacaUmur(scanner);
                System.out.print("Jenis Kelamin (Laki-laki/Perempuan): ");
                String jenisKelamin = bacaJenisKelamin(scanner);
                System.out.print("Keluhan: ");
                String keluhan = bacaTeksTidakKosong(scanner);
                pasien = tambahPasien(nama, umur, jenisKelamin, keluhan);
                view.tampilkanPesan(
                        "Pasien berhasil didaftarkan dengan ID: " + pasien.getId());
            }
            case 2 -> {
                tampilkanSemuaPasien();
                System.out.print("Masukkan ID Pasien: ");
                String idPasien = scanner.nextLine();
                pasien = cariPasienById(idPasien);
                if (pasien == null) {
                    view.tampilkanPesan("Pasien tidak ditemukan.");
                    return;
                }
            }
            default -> {
                view.tampilkanPesan("Pilihan tidak tersedia.");
                return;
            }
        }
 
        view.tampilkanPilihanPemeriksaan();
 
        tampilkanSemuaPemeriksaan();
 
        System.out.print("Masukkan ID Pemeriksaan: ");
        String idPemeriksaan = scanner.nextLine();
 
        Pemeriksaan pemeriksaan = cariPemeriksaanById(idPemeriksaan);
 
        if (pemeriksaan == null) {
            view.tampilkanPesan("Pemeriksaan tidak ditemukan.");
            return;
        }
 
        view.tampilkanPilihanPetugas();
 
        tampilkanSemuaPetugas();
 
        System.out.print("Masukkan ID Petugas: ");
        String idPetugas = scanner.nextLine();
 
        Petugas petugas = cariPetugasById(idPetugas);
 
        if (petugas == null) {
            view.tampilkanPesan("Petugas tidak ditemukan.");
            return;
        }
 
        view.tampilkanJudul("KONFIRMASI PENDAFTARAN");
 
        view.tampilkanKonfirmasiPendaftaran(pasien, pemeriksaan, petugas);
        pasienTerdaftar = pasien;
        pemeriksaanTerdaftar = pemeriksaan;
        petugasTerdaftar = petugas;
    }
 
    // menu pasien
 
    private void menuPasien(Scanner scanner) {
        boolean kembali = false;
 
        while (!kembali) {
            view.tampilkanSubMenuPasien();
 
            int pilihan = bacaInt(scanner);
 
            switch (pilihan) {
                case 1 -> tambahPasienDariInput(scanner);
                case 2 -> tampilkanSemuaPasien();
                case 3 -> cariPasienDariInput(scanner);
                case 4 -> hapusPasienDariInput(scanner);
                case 5 -> kembali = true;
                default -> view.tampilkanPesan("Pilihan tidak tersedia.");
            }
        }
    }
    private void tambahPasienDariInput(Scanner scanner) {
        view.tampilkanJudul("TAMBAH PASIEN");
        System.out.print("Nama (huruf saja): ");
        String nama = bacaNama(scanner);
        System.out.print("Umur (1-120): ");
        int umur = bacaUmur(scanner);
        System.out.print("Jenis Kelamin (Laki-laki/Perempuan): ");
        String jenisKelamin = bacaJenisKelamin(scanner);
        System.out.print("Keluhan: ");
        String keluhan = bacaTeksTidakKosong(scanner);
        Pasien pasien = tambahPasien(nama, umur, jenisKelamin, keluhan);
        view.tampilkanPesan(
                "Pasien berhasil ditambahkan dengan ID: " + pasien.getId());
    }
 
    private void cariPasienDariInput(Scanner scanner) {
        System.out.print("Masukkan ID Pasien: ");
        String id = scanner.nextLine();
 
        Pasien pasien = cariPasienById(id);
 
        if (pasien == null) {
            view.tampilkanPesan("Pasien tidak ditemukan.");
        } else {
            view.tampilkanBaris(pasien.tampilkanInfo(true));
        }
    }
 
    private void hapusPasienDariInput(Scanner scanner) {
        System.out.print("Masukkan ID Pasien yang ingin dihapus: ");
        String id = scanner.nextLine();
 
        if (hapusPasien(id)) {
            view.tampilkanPesan("Pasien berhasil dihapus.");
        } else {
            view.tampilkanPesan("Pasien tidak ditemukan.");
        }
    }
 
    // menu petugas
 
    private void menuPetugas(Scanner scanner) {
        boolean kembali = false;
 
        while (!kembali) {
            view.tampilkanSubMenuPetugas();
 
            int pilihan = bacaInt(scanner);
 
            switch (pilihan) {
                case 1 -> tambahAnalisDariInput(scanner);
                case 2 -> tambahDokterDariInput(scanner);
                case 3 -> tampilkanSemuaPetugas();
                case 4 -> kembali = true;
                default -> view.tampilkanPesan("Pilihan tidak tersedia.");
            }
        }
    }
    private void tambahAnalisDariInput(Scanner scanner) {
        view.tampilkanJudul("TAMBAH ANALIS");
        System.out.print("Nama (huruf saja): ");
        String nama = bacaNama(scanner);
        System.out.print("Umur (1-120): ");
        int umur = bacaUmur(scanner);
        System.out.print("Jenis Kelamin (Laki-laki/Perempuan): ");
        String jenisKelamin = bacaJenisKelamin(scanner);
        System.out.print("Spesialisasi/Bidang: ");
        String spesialisasiBidang= bacaTeksTidakKosong(scanner);
        Analis analis = tambahAnalis(
                nama, umur, jenisKelamin, spesialisasiBidang);
        view.tampilkanPesan(
                "Analis berhasil ditambahkan dengan ID: " + analis.getId());
    }
 
    private void tambahDokterDariInput(Scanner scanner) {
       view.tampilkanJudul("TAMBAH DOKTER");
 
       System.out.print("Nama (huruf saja): ");
       String nama = bacaNama(scanner);
       System.out.print("Umur (1-120): ");
       int umur = bacaUmur(scanner);
       System.out.print("Jenis Kelamin (Laki-laki/Perempuan): ");
       String jenisKelamin = bacaJenisKelamin(scanner);
       System.out.print("Nomor STR: ");
       String nomorSTR = bacaTeksTidakKosong(scanner);
 
        Dokter dokter = tambahDokter(
                nama, umur, jenisKelamin, nomorSTR);
 
        view.tampilkanPesan(
                "Dokter berhasil ditambahkan dengan ID: " + dokter.getId());
    }
 
    // menu pemeriksaan 
 
    private void menuPemeriksaan(Scanner scanner) {
        boolean kembali = false;
 
        while (!kembali) {
            view.tampilkanSubMenuPemeriksaan();
 
            int pilihan = bacaInt(scanner);
 
            switch (pilihan) {
                case 1 -> tambahPemeriksaanDariInput(scanner);
                case 2 -> tampilkanSemuaPemeriksaan();
                case 3 -> cariPemeriksaanDariInput(scanner);
                case 4 -> ubahPemeriksaanDariInput(scanner);
                case 5 -> hapusPemeriksaanDariInput(scanner);
                case 6 -> kembali = true;
                default -> view.tampilkanPesan("Pilihan tidak tersedia.");
            }
        }
    }
 
    private void tambahPemeriksaanDariInput(Scanner scanner) {
        view.tampilkanJudul("TAMBAH PEMERIKSAAN");
        System.out.print("Nama Pemeriksaan: ");
        String nama = bacaTeksTidakKosong(scanner);
 
        System.out.print("Biaya (angka, >= 0): ");
        double biaya = bacaDouble(scanner);
 
        Pemeriksaan pemeriksaan = tambahPemeriksaan(nama, biaya);
 
        view.tampilkanPesan(
                "Pemeriksaan berhasil ditambahkan dengan ID: "
                + pemeriksaan.getIdPemeriksaan());
    }
 
    private void cariPemeriksaanDariInput(Scanner scanner) {
        System.out.print("Masukkan ID Pemeriksaan: ");
        String id = scanner.nextLine();
 
        Pemeriksaan pemeriksaan = cariPemeriksaanById(id);
 
        if (pemeriksaan == null) {
            view.tampilkanPesan("Pemeriksaan tidak ditemukan.");
        } else {
            view.tampilkanBaris(
                    "ID: " + pemeriksaan.getIdPemeriksaan()
                    + " | Nama: " + pemeriksaan.getNamaPemeriksaan()
                    + " | Biaya: Rp" + pemeriksaan.getBiaya());
        }
    }
 
    private void ubahPemeriksaanDariInput(Scanner scanner) {
        System.out.print("Masukkan ID Pemeriksaan: ");
        String id = scanner.nextLine();
 
        Pemeriksaan pemeriksaan = cariPemeriksaanById(id);
 
        if (pemeriksaan == null) {
            view.tampilkanPesan("Pemeriksaan tidak ditemukan.");
            return;
        }
 
        view.tampilkanPesan("Tekan Enter saja jika tidak ingin mengubah data tersebut.");
 
        System.out.print("Nama Pemeriksaan Baru (" + pemeriksaan.getNamaPemeriksaan() + "): ");
        String namaBaru = bacaTeksOpsional(scanner, pemeriksaan.getNamaPemeriksaan());
 
        System.out.print("Biaya Baru (" + pemeriksaan.getBiaya() + "): ");
        double biayaBaru = bacaDoubleOpsional(scanner, pemeriksaan.getBiaya());
 
        if (ubahPemeriksaan(id, namaBaru, biayaBaru)) {
            view.tampilkanPesan("Pemeriksaan berhasil diubah.");
        } else {
            view.tampilkanPesan("Gagal mengubah pemeriksaan.");
        }
    }
 
    private void hapusPemeriksaanDariInput(Scanner scanner) {
        System.out.print("Masukkan ID Pemeriksaan: ");
        String id = scanner.nextLine();
 
        if (hapusPemeriksaan(id)) {
            view.tampilkanPesan("Pemeriksaan berhasil dihapus.");
        } else {
            view.tampilkanPesan("Pemeriksaan tidak ditemukan.");
        }
    }
 
    // menu hasil pemeriksaan
 
    private void menuHasil(Scanner scanner) {
        boolean kembali = false;
 
        while (!kembali) {
            view.tampilkanSubMenuHasil();
 
            int pilihan = bacaInt(scanner);
 
            switch (pilihan) {
                case 1 -> tambahHasilDariInput(scanner);
                case 2 -> tampilkanSemuaHasil();
                case 3 -> riwayatHasilDariInput(scanner);
                case 4 -> kembali = true;
                default -> view.tampilkanPesan("Pilihan tidak tersedia.");
            }
        }
    }
 
    private void tambahHasilDariInput(Scanner scanner) {
        view.tampilkanJudul("INPUT HASIL PEMERIKSAAN");
 
        if (pemeriksaanTerdaftar == null || petugasTerdaftar == null) {
            view.tampilkanPesan(
                    "Belum ada pemeriksaan yang terdaftar. "
                    + "Silakan lakukan menu \"1. Pendaftaran Pemeriksaan\" terlebih dahulu.");
            return;
        }
 
        tampilkanSemuaPasien();
 
        System.out.print("ID Pasien: ");
        String idPasien = scanner.nextLine();
 
        if (cariPasienById(idPasien) == null) {
            view.tampilkanPesan("Pasien tidak ditemukan.");
            return;
        }
 
        System.out.print("Hasil Pemeriksaan: ");
        String hasil = bacaTeksTidakKosong(scanner);
 
        System.out.print("Status (Normal/Tidak Normal): ");
        String status = bacaStatus(scanner);
 
        String pesan = tambahHasil(
        idPasien,
        pemeriksaanTerdaftar.getIdPemeriksaan(),
        petugasTerdaftar.getId(),
        hasil,
        status);
 
        view.tampilkanPesan(pesan);
    }
 
    private void riwayatHasilDariInput(Scanner scanner) {
        System.out.print("Masukkan ID Pasien: ");
        String idPasien = scanner.nextLine();
 
        tampilkanRiwayatByPasien(idPasien);
    }
 
    // pasien
 
    
    private String buatId(String prefix, int nomor) {
        return prefix + nomor;
    }
 
    public Pasien tambahPasien(String nama, int umur, String jenisKelamin, String keluhan) {
        Pasien pasien = new Pasien(
                buatId("P", nextIdPasien++), nama, umur, jenisKelamin, keluhan);
        daftarPasien.add(pasien);
        return pasien;
    }
 
    public void tampilkanSemuaPasien() {
        if (daftarPasien.isEmpty()) {
            view.tampilkanPesan("Belum ada data pasien.");
            return;
        }
        for (Pasien p : daftarPasien) {
        view.tampilkanBaris(
            "ID: " + p.getId()
            + " | Nama: " + p.getNama()
            + " | Umur: " + p.getUmur()
            + " | Jenis Kelamin: " + p.getJenisKelamin()
        );
    }
    }
 
    public Pasien cariPasienById(String id) {
        for (Pasien p : daftarPasien) {
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;
    }
 
    public boolean hapusPasien(String id) {
        Pasien pasien = cariPasienById(id);
        if (pasien == null) {
            return false;
        }
        return daftarPasien.remove(pasien);
    }
 
    // PETUGAS (Analis & Dokter)
 
    public Analis tambahAnalis(String nama, int umur, String jenisKelamin, String spesialisasiBidang) {
        Analis analis = new Analis(
                buatId("PT", nextIdPetugas++), nama, umur, jenisKelamin, spesialisasiBidang);
        daftarPetugas.add(analis); // Analis disimpan sebagai Petugas -> polymorphism
        return analis;
    }
 
    public Dokter tambahDokter(String nama, int umur, String jenisKelamin, String nomorSTR) {
        Dokter dokter = new Dokter(
                buatId("PT", nextIdPetugas++), nama, umur, jenisKelamin, nomorSTR);
        daftarPetugas.add(dokter); // Dokter disimpan sebagai Petugas -> polymorphism
        return dokter;
    }
 
    public void tampilkanSemuaPetugas() {
        if (daftarPetugas.isEmpty()) {
            view.tampilkanPesan("Belum ada data petugas.");
            return;
        }
  
        for (Petugas p : daftarPetugas) {
            p.tampilkanInfo(true);
            System.out.println("------------------------------------");
        }
    }
 
    public Petugas cariPetugasById(String id) {
        for (Petugas p : daftarPetugas) {
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;
    }
 
    // pemeriksaan
 
    public Pemeriksaan tambahPemeriksaan(String namaPemeriksaan, double biaya) {
        Pemeriksaan pemeriksaanBaru = new Pemeriksaan(
                buatId("PM", nextIdPemeriksaan++), namaPemeriksaan, biaya);
        daftarPemeriksaan.add(pemeriksaanBaru);
        return pemeriksaanBaru;
    }
 
    public void tampilkanSemuaPemeriksaan() {
        if (daftarPemeriksaan.isEmpty()) {
            view.tampilkanPesan("Belum ada data pemeriksaan.");
            return;
        }
        for (Pemeriksaan p : daftarPemeriksaan) {
            view.tampilkanBaris("ID: " + p.getIdPemeriksaan()
                    + " | Nama: " + p.getNamaPemeriksaan() 
                    + " | Biaya: Rp" + p.getBiaya());
        }
    }
 
    public Pemeriksaan cariPemeriksaanById(String id) {
        for (Pemeriksaan pemeriksaan : daftarPemeriksaan) {
            if (pemeriksaan.getIdPemeriksaan().equalsIgnoreCase(id)) {
                return pemeriksaan;
            }
        }
        return null;
    }
 
    public boolean ubahPemeriksaan(String id, String namaBaru, double biayaBaru) {
        Pemeriksaan pemeriksaan = cariPemeriksaanById(id);
        if (pemeriksaan == null) {
            return false;
        }
        pemeriksaan.setNamaPemeriksaan(namaBaru);
        pemeriksaan.setBiaya(biayaBaru);
        return true;
    }
 
    public boolean hapusPemeriksaan(String id) {
        Pemeriksaan pemeriksaan = cariPemeriksaanById(id);
        if (pemeriksaan == null) {
            return false;
        }
        daftarPemeriksaan.remove(pemeriksaan);
        return true;
    }
 
    // hasil pemeriksaan
 
    public String tambahHasil(String idPasien, String idPemeriksaan, String idPetugas,
            String hasil, String status) {
        if (cariPasienById(idPasien) == null) {
            return "GAGAL: ID Pasien " + idPasien + " tidak ditemukan.";
        }
        if (cariPemeriksaanById(idPemeriksaan) == null) {
            return "GAGAL: ID Pemeriksaan " + idPemeriksaan + " tidak ditemukan.";
        }
        if (cariPetugasById(idPetugas) == null) {
            return "GAGAL: ID Petugas " + idPetugas + " tidak ditemukan.";
        }
        String id = buatId("H", nextIdHasil++);
        HasilPemeriksaan hasilBaru = new HasilPemeriksaan(id, idPasien, idPemeriksaan, idPetugas, hasil, status);
        daftarHasil.add(hasilBaru);
        return "SUKSES: Hasil berhasil ditambahkan dengan ID " + id;
    }
 
    public void tampilkanSemuaHasil() {
        if (daftarHasil.isEmpty()) {
            view.tampilkanPesan("Belum ada data hasil pemeriksaan.");
            return;
        }
        for (HasilPemeriksaan h : daftarHasil) {
            view.tampilkanBaris(formatHasil(h));
        }
    }
 
    public void tampilkanRiwayatByPasien(String idPasien) {
        Pasien pasien = cariPasienById(idPasien);
        if (pasien == null) {
            view.tampilkanPesan("Pasien dengan ID " + idPasien + " tidak ditemukan.");
            return;
        }
        boolean adaHasil = false;
        for (HasilPemeriksaan h : daftarHasil) {
            if (h.getIdPasien().equalsIgnoreCase(idPasien)) {
                view.tampilkanBaris(formatHasil(h));
                adaHasil = true;
            }
        }
        if (!adaHasil) {
            view.tampilkanPesan("Belum ada riwayat pemeriksaan untuk pasien ini.");
        }
    }
 
    private String formatHasil(HasilPemeriksaan h) {
        Pemeriksaan pm = cariPemeriksaanById(h.getIdPemeriksaan());
        Petugas pt = cariPetugasById(h.getIdPetugas());
        Pasien ps = cariPasienById(h.getIdPasien());
        String namaTes = (pm != null) ? pm.getNamaPemeriksaan() : "-";
        String namaPetugas = (pt != null) ? pt.getNama() : "-";
        String namaPasien = (ps != null) ? ps.getNama() : "-";
        return "ID Hasil: " + h.getIdHasil()
                + " | Pasien: " + namaPasien
                + " | Jenis Tes: " + namaTes
                + " | Hasil: " + h.getHasil()
                + " | Status: " + h.getStatus()
                + " | Diperiksa oleh: " + namaPetugas;
    }
        //  METHOD VALIDASI INPUT 
 
    private String bacaNama(Scanner scanner) {
        while (true) {
            String nama = scanner.nextLine().trim();
 
            if (nama.isEmpty()) {
                System.out.print("Nama tidak boleh kosong. Masukkan lagi: ");
            } else if (!nama.matches("[a-zA-Z .]+")) {
                System.out.print("Nama hanya boleh berisi huruf. Masukkan lagi: ");
            } else {
                return nama;
            }
        }
    }
 
    /**
     * Membaca teks yang harus salah satu dari pilihan valid
     * (dipakai bersama oleh jenis kelamin dan status).
     */
    private String bacaPilihanTeks(Scanner scanner, String pesanError, String... pilihanValid) {
        while (true) {
            String input = scanner.nextLine().trim();
 
            for (String pilihan : pilihanValid) {
                if (pilihan.equalsIgnoreCase(input)) {
                    return pilihan;
                }
            }
 
            System.out.print(pesanError);
        }
    }
 
    private String bacaJenisKelamin(Scanner scanner) {
        return bacaPilihanTeks(scanner,
                "Jenis kelamin harus Laki-laki atau Perempuan. Masukkan lagi: ",
                "Laki-laki", "Perempuan");
    }
 
    private String bacaStatus(Scanner scanner) {
        return bacaPilihanTeks(scanner,
                "Status harus Normal atau Tidak Normal. Masukkan lagi: ",
                "Normal", "Tidak Normal");
    }
 
    /**
     * Membaca teks untuk fitur ubah data: kalau user hanya menekan
     * Enter (input kosong), nilai lama dipertahankan.
     */
    private String bacaTeksOpsional(Scanner scanner, String nilaiLama) {
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? nilaiLama : input;
    }
 
    /**
     * Versi angka desimal dari bacaTeksOpsional(): Enter kosong akan
     * mempertahankan nilai lama, begitu juga kalau inputnya tidak valid.
     */
    private double bacaDoubleOpsional(Scanner scanner, double nilaiLama) {
        String input = scanner.nextLine().trim();
 
        if (input.isEmpty()) {
            return nilaiLama;
        }
 
        try {
            double nilai = Double.parseDouble(input);
            if (nilai >= 0) {
                return nilai;
            }
            view.tampilkanPesan("Nilai tidak boleh negatif, nilai lama dipertahankan.");
            return nilaiLama;
        } catch (NumberFormatException e) {
            view.tampilkanPesan("Input bukan angka, nilai lama dipertahankan.");
            return nilaiLama;
        }
    }
 
    private String bacaTeksTidakKosong(Scanner scanner) {
        while (true) {
            String teks = scanner.nextLine().trim();
 
            if (!teks.isEmpty()) {
                return teks;
            }
 
            System.out.print("Input tidak boleh kosong. Masukkan lagi: ");
        }
    }
 
    /**
     * Overload tanpa rentang, dipakai untuk pilihan menu.
     */
    private int bacaInt(Scanner scanner) {
        return bacaInt(scanner, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
 
    /**
     * Overload dengan rentang min-max, dipakai misalnya oleh bacaUmur().
     */
    private int bacaInt(Scanner scanner, int min, int max) {
        while (true) {
            String input = scanner.nextLine().trim();
 
            try {
                int nilai = Integer.parseInt(input);
 
                if (nilai >= min && nilai <= max) {
                    return nilai;
                }
 
                System.out.print(
                        "Masukkan angka antara " + min + " sampai " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("Input harus berupa angka. Masukkan lagi: ");
            }
        }
    }
 
    private int bacaUmur(Scanner scanner) {
        return bacaInt(scanner, 1, 120);
    }
 
    private double bacaDouble(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
 
            try {
                double nilai = Double.parseDouble(input);
 
                if (nilai >= 0) {
                    return nilai;
                }
                        
 
                System.out.print(
                        "Nilai tidak boleh negatif. Masukkan lagi: ");
            } catch (NumberFormatException e) {
                System.out.print(
                        "Input harus berupa angka. Masukkan lagi: ");
            }
        }
    }
}

