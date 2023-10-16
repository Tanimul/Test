package bd.com.media365.rh_android_sdk_test

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import bd.com.media365.ratehammer_sdk.ui.activities.main.LoanActivity
import bd.com.media365.rh_android_sdk_test.databinding.ActivityMainTestBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.writeLocale.setText("en")
        binding.writeToken.setText(
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI5YTQ5Y2QzNC1iYzEzLTQ2MWQtOGFkYi1mZmM3Yzk0MGZjNTgiLCJqdGkiOiIwMzNiZTQxZWViZjRmMTQ4YTVhODg3NzFkZjkzY2E4ZDU0MzkxYzAxZjVhZmQ3ZGI5M2QxOWU2ZmU0ZjhlNTQxZmJkMDE4OTFiY2RmZGUzYiIsImlhdCI6MTY5NzAyNzk0Ni44NTMwNDUsIm5iZiI6MTY5NzAyNzk0Ni44NTMwNDcsImV4cCI6MTY5ODMyMzk0Ni44NDk3NDgsInN1YiI6IjMyIiwic2NvcGVzIjpbXX0.ZQF4Ct1E76M7VvkVDpxW0ywt_J7aPgY3Wq26YC75GeET5loynaC-J6G49bGwujbcQykcmHfLQV5cKPf8PlNZEnCfEcBN0h9LWn_ZWAEpov_fSacKVx5IlxVnrFb9Ds2mjpx0WKzLOqKA-Mb4OsedSH7wrbnax-6X0VniTkkX20C_GLluztUo_AyjQtb_q1nVnHA90meHDgYXrOTVK1i_c0efaofXaQQpxzTEEywNm9IYeNjHGQL05iE-Cd-7k46yGn1nKjQl-sqHni8NvqqnuMjE9MxZiPdvX4uG1T7ZMULavoGn3Wx4RTW28C-DO4uOzFKOvEfz63Y_jXrU1fyYLnVnZwCiqV8ZfOF4VQUVbZrNndgwxh-mUqCchZGLd8XG33JZqbYSjcqDaAJUuE5G_vKxZfQlOBc_IuZdWd7XsQJrN5QIW6zIGJQiIKsLjuLu4Wf9mqSs7osS96NBCh9_YMw-fOpywNDngpGUBLw_NZqmthm_F8N641G0B1JM1z7vPudL-FbHh5Ahu98W86V4OQKj5Gsd3x-B3MgZwbATeH43haNrWEiYC9x-ra-AUeQlKIQDdAx3N-ShjGS_vf_QhqDt7FmeW6fy8pgpYIUCGdhfV1ZJP0BGg8Y2plITIRZdF88Zv7Bw1Z6bkV-7gwEB_MVzycgxHWguoU8HONYcx3c" )


        binding.click.setOnClickListener {
            val locale = binding.writeLocale.text.toString()
            val sessionToken = binding.writeToken.text.toString()
            if (locale == "ar" || locale == "en") {
                startActivity(
                    Intent(this, LoanActivity::class.java).putExtra(
                        "session_token", sessionToken

                    ).putExtra("locale", locale)
                )
            } else {
                Toast.makeText(this, "Locale should be 'ar' or 'en'", Toast.LENGTH_SHORT).show()
            }

        }


    }


}