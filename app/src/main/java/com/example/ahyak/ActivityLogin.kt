package com.example.ahyak

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.example.ahyak.DB.AuthService
import com.example.ahyak.DB.LoginView
import com.example.ahyak.OCR.OCRprescriptionActivity
import com.example.ahyak.databinding.ActivityLoginBinding
import com.example.ahyak.databinding.ActivityMainBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import java.security.MessageDigest

class ActivityLogin : AppCompatActivity(), LoginView {

    companion object {
        fun newInstance() = ActivityLogin()
    }

    private lateinit var binding: ActivityLoginBinding
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("KakaoLogin", "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i("KakaoLogin", "카카오계정으로 로그인 성공 ${token.accessToken}")
            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //getKeyHash(this)

        var count = 0
        binding.logoLl.setOnClickListener {
            count = count+1
            if(count==5){
                val intent = Intent(this, MainActivity::class.java)
                finish()
                startActivity(intent)
            }
            Log.d("count", "$count")
        }

        binding.loginBtn.setOnClickListener{
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e("KakaoLogin", "카카오톡으로 로그인 실패", error)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        Log.i("KakaoLogin", "카카오톡으로 로그인 성공 ${token.accessToken}")
                        // 사용자 정보 요청
                        UserApiClient.instance.me { user, meError ->
                            if (meError != null) {
                                Log.e("KakaoLogin", "사용자 정보 요청 실패", meError)
                            } else if (user != null) {
                                val nickname = user.kakaoAccount?.profile?.nickname
                                val email = user.kakaoAccount?.email

                                Log.i("KakaoLogin", "사용자 이름: $nickname")
                                Log.i("KakaoLogin", "사용자 이메일: $email")

                                // 여기서 서버에 nickname, email 전달해서 회원가입 or 로그인 처리 가능
                                // signup(nickname ?: "Unknown", email ?: "Unknown")
                                val authService = AuthService(this)
                                authService.setLoginView(this)
                                authService.signup(nickname!!,email!!)

                                // 다음 화면으로 이동 예시
//                                val intent = Intent(this, MainActivity::class.java)
//                                startActivity(intent)
//                                finish()
                            }
                        }
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

    override fun SignupLoading() {

    }

    override fun SignupSuccess() {
        Log.d("signup","회원가입 성공")
    }

    override fun SignupFailure() {
        Log.d("signup","회원가입 실패")
    }

    override fun LoginLoading() {

    }

    override fun LoginSuccess() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun LoginFailure() {

    }
    //Hash Key
    fun getKeyHash(context: Context): String? {
        try {
            val info = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNING_CERTIFICATES
            )
            val signatures = info.signingInfo.apkContentsSigners
            for (signature in signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val keyHash = Base64.encodeToString(md.digest(), Base64.NO_WRAP)
                Log.d("KeyHash", "Friend KeyHash: $keyHash")
                return keyHash
            }
        } catch (e: Exception) {
            Log.e("KeyHash", "Error", e)
        }
        return null
    }
}