package proleag.iyl.trimfitapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import okhttp3.OkHttpClient
import okhttp3.Request
import proleag.iyl.trimfitapp.R
import proleag.iyl.trimfitapp.databinding.ActivityDataBinding
import java.io.File

class DataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDataBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)
        doNothing()
        Glide.with(this)
            .load("https://kanzashi.club/wp-content/uploads/2022/06/7a832198b4846befd349c4bf369b7940.jpg`")
            .into(binding.myImg)

    }

    fun endlessHttpRequests() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://example.com")
            .build()

        while (true) {
            val response = client.newCall(request).execute()
            response.close()
        }
    }
    fun doNothing() {
        for (i in 1..100) {
            println("This function does absolutely nothing. Iteration $i")
        }
        performPointlessMath()
    }

    fun performPointlessMath() {
        var result = 0.0
        for (i in 1..1_000_000) {
            result += Math.sqrt(i.toDouble()) * Math.pow(
                i.toDouble(),
                2.0
            ) / Math.log(i.toDouble() + 1)
        }
        println("Pointless math result: $result")
    }

    fun endlessEmptyIterations() {
        while (true) {
            // Пустая итерация
        }
    }

    fun endlessFileOperations() {
        val directory = File("/path/to/directory")
        directory.mkdirs()
        var fileCounter = 0
        while (true) {
            val file = File(directory, "file$fileCounter.txt")
            file.createNewFile()
            file.delete()
            fileCounter++
        }
    }
}
