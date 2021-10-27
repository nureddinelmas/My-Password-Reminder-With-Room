package com.nureddinelmas.mypassword

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.nureddinelmas.mypassword.databinding.ActivityPasswordBinding
import java.io.ByteArrayOutputStream
import kotlin.math.max

class PasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPasswordBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionResultLauncher: ActivityResultLauncher<String>
    private var selectedBitmap : Bitmap? = null
    private lateinit var database : SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        database = this.openOrCreateDatabase("Passwords", MODE_PRIVATE, null)

        val info = intent.getStringExtra("info")
        if (info.equals("old")){
            binding.button.text = "Delete"
            binding.imageView.isEnabled = false
            binding.textUser.isEnabled = false
            binding.textUser2.isEnabled=false
            binding.textUser3.isEnabled=false


            val selectedId = intent.getIntExtra("id",1)

            val cursor = database.rawQuery("SELECT * FROM passwords WHERE id = ?", arrayOf(selectedId.toString()))
            val passnameIx = cursor.getColumnIndex("passname")
            val passuserIx = cursor.getColumnIndex("passuser")
            val passkeyIx = cursor.getColumnIndex("passkey")
            val passimageIx = cursor.getColumnIndex("image")

            while(cursor.moveToNext()){
                binding.textUser2.setText(cursor.getString(passuserIx))
                binding.textUser.setText(cursor.getString(passnameIx))
                binding.textUser3.setText(cursor.getString(passkeyIx))

                val byteArray = cursor.getBlob(passimageIx)
                val bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
                binding.imageView.setImageBitmap(bitmap)
            }
            cursor.close()


        }
        registerLauncher()

    }
    fun clickedButton(view: View){

        //SAVE BUTTON
        if (binding.button.text == "Save"){

            var passName = binding.textUser.text.toString()
            var passUser = binding.textUser2.text.toString()
            var passKey = binding.textUser3.text.toString()

            if (selectedBitmap != null){
                val smallBitmap = makeSmallerBitmap(selectedBitmap!!,300)

                val outputStream = ByteArrayOutputStream()
                smallBitmap.compress(Bitmap.CompressFormat.PNG,80, outputStream)
                val byteArray = outputStream.toByteArray()

                try {
                    database.execSQL("CREATE TABLE IF NOT EXISTS passwords (id INTEGER PRIMARY KEY, passname VARCHAR, passuser VARCHAR, passkey VARCHAR, image BLOB)")

                    val sqlString = "INSERT INTO passwords (passname, passuser, passkey, image) VALUES (?, ?, ?, ?)"
                    val statement = database.compileStatement(sqlString)
                    statement.bindString(1,passName)
                    statement.bindString(2,passUser)
                    statement.bindString(3,passKey)
                    statement.bindBlob(4,byteArray)
                    statement.execute()

                }catch (e: Exception){
                    e.printStackTrace()
                }

                val intent = Intent(this@PasswordActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }

        if (binding.button.text == "Delete"){
            val selectedId = intent.getIntExtra("id",0)
            database.delete("passwords","id = ?", arrayOf(selectedId.toString()))
            val intent = Intent(this@PasswordActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

    }

    private fun makeSmallerBitmap(image: Bitmap, maximumSize: Int): Bitmap{
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toDouble() / height.toDouble()

        if (bitmapRatio > 1){
            width = maximumSize
            height = width / bitmapRatio.toInt()
        }
        else{
            height = maximumSize
            width = height * bitmapRatio.toInt()
        }
        return Bitmap.createScaledBitmap(image,width,height,true)
    }

    fun selectedImage(view: View){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view, "Permission needed", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission",View.OnClickListener { result ->
                    permissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }).show()
            }
            else{
                permissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        }
        else{
            val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)

        }
    }



    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            if (result.resultCode == RESULT_OK){
                val intentFromResult = result.data
                  if (intentFromResult != null){
                      val imageData = intentFromResult.data
                      if (imageData != null){
                          try {
                              if (Build.VERSION.SDK_INT >= 28){
                                  val source = ImageDecoder.createSource(this@PasswordActivity.contentResolver, imageData)
                                  selectedBitmap = ImageDecoder.decodeBitmap(source)
                                  binding.imageView.setImageBitmap(selectedBitmap)
                              }else{
                                  selectedBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageData)
                                  binding.imageView.setImageBitmap(selectedBitmap)
                              }
                          }catch (e : Exception){
                              e.printStackTrace()
                          }
                      }
                  }
            }

        }

        permissionResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){result ->
            if (result){
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }else{
                Toast.makeText(this@PasswordActivity, "Permission needed", Toast.LENGTH_LONG).show()
            }
        }
    }


}