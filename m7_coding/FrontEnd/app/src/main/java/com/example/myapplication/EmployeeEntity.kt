package com.example.myapplication

import com.squareup.moshi.Json
import java.io.Serializable

data class EmployeeEntity(
    val id_karyawan: String,
    val nama_karyawan: String,
    val jk_karyawan: String,
    val alamat_karyawan: String,
    val noTelp_karyawan: String,
    val dob_karyawan: String,
    val tgl_masuk: String,
    val status_karyawan: String,
    @Json(name = "deleted_at") val deletedAt: String?
):Serializable