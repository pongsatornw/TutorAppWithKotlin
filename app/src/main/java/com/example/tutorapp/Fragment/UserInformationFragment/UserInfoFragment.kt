package com.example.tutorapp.Fragment.UserInformationFragment

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.example.tutorapp.*
import com.example.tutorapp.Fragment.MainFragment
import com.example.tutorapp.Utility.FireStoreInitial
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.shashank.sony.fancytoastlib.FancyToast
import com.yarolegovich.lovelydialog.LovelyStandardDialog
import kotlinx.android.synthetic.main.custom_dialog.*
import kotlinx.android.synthetic.main.fragment_self_info.view.*
import java.lang.StringBuilder

class UserInfoFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_self_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val qrCodeWriter = QRCodeWriter()
        val qrMatrix: BitMatrix = qrCodeWriter.encode(
                (activity as MainActivity).getCurrentID(),
                BarcodeFormat.QR_CODE, 360, 360
        )
        val bitmap = Bitmap.createBitmap(qrMatrix.width, qrMatrix.height,
                Bitmap.Config.ARGB_8888
        )
        for (x in 0 until qrMatrix.width) {
            for (y in 0 until qrMatrix.height) {
                bitmap.setPixel(x, y, if (qrMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        view.qr_image.setImageBitmap(bitmap)

        loadData(mOnCallback)

    }

    private fun loadData(mOnCallback: ListCallback<List<*>>) {
        val list = mutableListOf<DocumentSnapshot>()
        Log.d("CURRENT_ID", (context as MainActivity).getCurrentID() )
        FireStoreInitial.initial().collection(Constants.User_Profile.collection_name)
                .whereEqualTo( Constants.User_Profile.user_id, (context as MainActivity).getCurrentID() )
                .limit(1)
                .get()
                .addOnSuccessListener {
                    if(it.size() > 0) {
                        list.add(it.documents[0])
                        mOnCallback.onCallback(data = list)
                    } else {
                        list.clear()
                        mOnCallback.onCallback(data = list)
                    }
                }
                .addOnFailureListener {
                    mOnCallback.onCallback(data = mutableListOf(null))
                }
                .addOnCanceledListener {

                }
    }

    private var mOnCallback = object: ListCallback<List<*>>{
        override fun onCallback(data: List<*>) {
            // If list have data
            val list = data as List<DocumentSnapshot>
            if(!data.isEmpty()) {
                view!!.txt_user_id.text = list[0][Constants.User_Profile.user_id] as String
                view!!.txt_role.text = list[0][Constants.User_Profile.role] as String
                view!!.txt_name.text = StringBuilder().apply{
                    append(list[0][Constants.User_Profile.fname] as String)
                    append(" ")
                    append(list[0][Constants.User_Profile.lname] as String)
                }
                view!!.txt_email.text = list[0][Constants.User_Profile.email] as String
                view!!.txt_gender.text = list[0][Constants.User_Profile.gender] as String
                view!!.txt_tel.text = list[0][Constants.User_Profile.tel_no] as String
                view!!.qr_image.setOnLongClickListener{
                    Dialog(requireContext()).apply {
                        setContentView(R.layout.custom_dialog)
                        setCanceledOnTouchOutside(false)
                        val dialog = this
                        txt_message.text = "ท่านต้องการเปลี่ยนรหัสผ่านของบัญชีเข้าใช้งานใช่หรือไม่"
                        CompatBtn05.setOnClickListener {
                            AlertDialog.Builder(context).apply {
                                val linearLayout =  LinearLayout(context)
                                linearLayout.orientation = LinearLayout.VERTICAL
                                val editText1 = EditText(context)
                                editText1.hint = "รหัสผ่านปัจจุบัน"
                                val editText2 = EditText(context)
                                editText2.hint = "รหัสผ่านใหม่"
                                val editText3 = EditText(context)
                                editText3.hint = "ยืนยันรหัสผ่านใหม่"
                                linearLayout.addView(editText1)
                                linearLayout.addView(editText2)
                                linearLayout.addView(editText3)
                                setView(linearLayout)

                                setPositiveButton("ยืนยัน"){ _, _ ->
                                    if(editText1.text != null && editText2.text != null && editText3.text != null &&
                                            editText1.text.toString() != "" && editText2.text.toString() != "" && editText3.text.toString() != "") {
                                        if(editText2.text.toString() == editText3.text.toString()){
                                            newPassword = editText2.text.toString()
                                            changePassword(editText1.text.toString())
                                            dialog.dismiss()
                                        } else {
                                            Toast.makeText(context, "รหัสผ่าน และ ยืนยันรหัสผ่านต้องไม่แตกต่างกัน", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        Toast.makeText(context, "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                setNegativeButton("ยกเลิก"){ _, _ ->
                                    dismiss()
                                }

                            }.show()
                        }
                        CompatBtn06.setOnClickListener {
                            dismiss()
                        }
                    }.show()
                    return@setOnLongClickListener true
                }
            } else {
                FancyToast.makeText(context, "ไม่พบบัญชีผู้ใช้งานนี้ในฐานข้อมูล", FancyToast.LENGTH_SHORT, FancyToast.WARNING,false).show()


                Thread{
                    Thread.sleep(2000)
                }.start()

                for (i in 0 until fragmentManager!!.backStackEntryCount) {
                    fragmentManager?.popBackStack()
                }

                (context as MainActivity).onBackPressed()

            }
        }
    }

    private fun changePassword(oldpwd: String) {
        FireStoreInitial.initial().collection(Constants.Account.collection_name)
                .limit(1)
                .whereEqualTo(Constants.Account.user_id, (activity as MainActivity).getCurrentID())
                .whereEqualTo(Constants.Account.password, oldpwd)
                .get()
                .addOnSuccessListener {
                    if(!it.isEmpty) {
                        mCallback.onCallback(it.documents[0])
                    } else {
                        mCallback.onEmptyCallback()
                    }
                }
                .addOnFailureListener{
                    mCallback.onErrorCallback(it)
                }
    }

    private var newPassword = ""

    private val mCallback = object: DocumentCallback{
        override fun onCallback(doc: DocumentSnapshot) {
                FireStoreInitial.initial().collection(Constants.Account.collection_name)
                        .document(doc.id)
                        .set(mapOf("password" to newPassword), SetOptions.mergeFields(Constants.Account.password))
                        .addOnSuccessListener {
                            Toast.makeText(context, "การเปลี่ยนแปลงรหัสผ่านสำเร็จ", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener{
                            Toast.makeText(context, "เกิดความผิดพลาดในการเปลี่ยนแปลงรหัสผ่าน", Toast.LENGTH_SHORT).show()
                        }
        }

        override fun onEmptyCallback() {
            Toast.makeText(context, "รหัสผ่านปัจจุบันไม่ถูกต้อง ไม่สามารถเปลี่ยนแปลงรหัสผ่านให้เป็นใหม่ได้", Toast.LENGTH_SHORT).show()
        }

        override fun onErrorCallback(ex: Exception) {

        }
    }

    companion object {
        fun newInstance(): Fragment {
            val fragment = UserInfoFragment()

            return fragment
        }
    }

}