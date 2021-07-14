package com.example.retrofit_ex

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    private var retrofit: Retrofit? = null
    private var retrofitInterface: RetrofitInterface? = null

    //    private String BASE_URL = "http://172.10.18.137:80";
    private val BASE_URL = "http://172.10.18.137:80"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        this.retrofit?.create(RetrofitInterface::class.java).also { retrofitInterface = it }
        findViewById<View>(R.id.login).setOnClickListener { handleLoginDialog() }
        findViewById<View>(R.id.signup).setOnClickListener { handleSignupDialog() }

//        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
//            if (error != null) {
//                when {
//                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
//                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
//                    }
//                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
//                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
//                    }
//                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
//                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
//                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
//                    }
//                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
//                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
//                    }
//                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
//                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                    error.toString() == AuthErrorCause.ServerError.toString() -> {
//                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
//                    }
//                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
//                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
//                    }
//                    else -> { // Unknown
//                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            } else if (token != null) {
//                Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
//                handleSignupDialogforkakao()
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//            }
//        }


        val kakao_login_button = findViewById<Button>(R.id.kakao_login_button) as ImageButton

        kakao_login_button.setOnClickListener {
                val map = HashMap<String, String>()

                //등록된 카카오 계정으로 로그인, 유저 탈퇴 기능 추가시 추가 구현 가능

                map["email"] = "jeff426@naver.com"
                map["password"] = ""
                val call = retrofitInterface!!.executeLogin(map)
            call?.enqueue(object : Callback<LoginResult?> {
                override fun onResponse(
                    call: Call<LoginResult?>,
                    response: Response<LoginResult?>
                ) {
                    if (response.code() == 200) {
                        val result = response.body()
                        val builder1 = AlertDialog.Builder(this@LoginActivity)
                        builder1.setTitle(result!!.name)
                        builder1.setMessage(result.email)
                        builder1.show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("name", result.name)
                        intent.putExtra("email", result.email)
                        startActivity(intent)
                    } else if (response.code() == 404) {
                        Toast.makeText(
                            this@LoginActivity, "Wrong Credentials",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<LoginResult?>, t: Throwable) {
                    Toast.makeText(
                        this@LoginActivity, t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

        }
//            if (LoginClient.instance.isKakaoTalkLoginAvailable(this)) {
//                LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
//            } else {
//                LoginClient.instance.loginWithKakaoAccount(this, callback = callback)
//
//            }
//        }
    }

    private fun handleLoginDialog() {
        val view = layoutInflater.inflate(R.layout.login_dialog, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view).show()
        val loginBtn = view.findViewById<Button>(R.id.login)
        val emailEdit = view.findViewById<EditText>(R.id.emailEdit)
        val passwordEdit = view.findViewById<EditText>(R.id.passwordEdit)
        loginBtn.setOnClickListener {
            val pattern2 = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$"
            if (!Pattern.matches(pattern2, emailEdit.text.toString())) {
                Toast.makeText(this@LoginActivity, "Unvalid Email", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val map = HashMap<String, String>()
            map["email"] = emailEdit.text.toString()
            map["password"] = passwordEdit.text.toString()
            val call = retrofitInterface!!.executeLogin(map)
            call?.enqueue(object : Callback<LoginResult?> {
                override fun onResponse(
                    call: Call<LoginResult?>,
                    response: Response<LoginResult?>
                ) {
                    if (response.code() == 200) {
                        val result = response.body()
                        val builder1 = AlertDialog.Builder(this@LoginActivity)
                        builder1.setTitle(result!!.name)
                        builder1.setMessage(result.email)
                        builder1.show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("name", result.name)
                        intent.putExtra("email", result.email)
                        startActivity(intent)
                    } else if (response.code() == 404) {
                        Toast.makeText(
                            this@LoginActivity, "Wrong Credentials",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<LoginResult?>, t: Throwable) {
                    Toast.makeText(
                        this@LoginActivity, t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
    }

    private fun handleSignupDialog() {
        val view = layoutInflater.inflate(R.layout.signup_dialog, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view).show()
        val signupBtn = view.findViewById<Button>(R.id.signup)
        val nameEdit = view.findViewById<EditText>(R.id.nameEdit)
        val emailEdit = view.findViewById<EditText>(R.id.emailEdit)
        val passwordEdit = view.findViewById<EditText>(R.id.passwordEdit)
        signupBtn.setOnClickListener {
            val pattern2 = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$"
            if (!Pattern.matches(pattern2, emailEdit.text.toString())) {
                Toast.makeText(this@LoginActivity, "Unvalid Email", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val map = HashMap<String, String>()
            map["name"] = nameEdit.text.toString()
            map["email"] = emailEdit.text.toString()
            map["password"] = passwordEdit.text.toString()
            val call = retrofitInterface!!.executeSignup(map)
            call?.enqueue(object : Callback<Void?> {
                override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                    if (response.code() == 200) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Signed up successfully", Toast.LENGTH_LONG
                        ).show()
                    } else if (response.code() == 400) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Already registered", Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Void?>, t: Throwable) {
                    Toast.makeText(
                        this@LoginActivity, t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
    }

//    private fun handleSignupDialogforkakao() {
//        val map = HashMap<String, String>()
//
//        UserApiClient.instance.me { user, error ->
//            if (error != null) {
//                Log.e(ContentValues.TAG, "사용자 정보 요청 실패", error)
//            } else if (user != null) {
//                map["name"] = "${user.kakaoAccount?.profile?.nickname}"
//                map["email"] = "${user.kakaoAccount?.email}"
//                map["password"] = ""
//            }
//
//            val call = retrofitInterface!!.executeSignup(map)
//            call.enqueue(object : Callback<Void?> {
//                override fun onResponse(
//                    call: Call<Void?>,
//                    response: Response<Void?>
//                ) {
//                    if (response.code() == 200) {
//                        Toast.makeText(
//                            this@LoginActivity,
//                            "Signed up successfully", Toast.LENGTH_LONG
//                        ).show()
//                    } else if (response.code() == 400) {
//                        Toast.makeText(
//                            this@LoginActivity,
//                            "Already registered", Toast.LENGTH_LONG
//                        ).show()
//                    }
//                }
//
//                override fun onFailure(call: Call<Void?>, t: Throwable) {
//                    Toast.makeText(
//                        this@LoginActivity, t.message,
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            })
//        }
//    }

}