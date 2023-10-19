package proleag.iyl.trimfitapp.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import proleag.iyl.trimfitapp.R
import proleag.iyl.trimfitapp.adapters.ProfileAdapter
import proleag.iyl.trimfitapp.databinding.ActivityProfileBinding
import proleag.iyl.trimfitapp.models.UserProfile

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setups()
        binding.back.setOnClickListener {
            finish()
        }
    }

    private fun setups() {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val currentSteps = sharedPreferences.getInt("current_steps_for_profile", 0)
        val burnedCalories = currentSteps * 0.04f

        val savedGender = sharedPreferences.getString("selected_gender", "")
        val savedBirthday = sharedPreferences.getLong("selected_birthday", 0)
        val currentWeight = sharedPreferences.getInt("selected_weight", 0)
        val currentHeight = sharedPreferences.getInt("selected_height", 0)
        val currentGoalSteps = sharedPreferences.getInt("selected_steps", 0)
        val currentGoalWeight = sharedPreferences.getInt("goal_weight", 0)

        val profiles = listOf(
            UserProfile(
                getString(R.string.goal_steps), "$currentGoalSteps" + getString(R.string.steps)
            ),
            UserProfile(
                getString(R.string.goal_weight), "$currentGoalWeight" + getString(R.string.steps)
            ),
            UserProfile(
                getString(R.string.current_steps), "$currentSteps" + getString(R.string.steps)
            ),
            UserProfile(
                getString(R.string.distance_traveled),
                String.format("%.2f", 0.762f * currentSteps) + getString(R.string.metres)
            ),
            UserProfile(
                getString(R.string.calories_burned), "$burnedCalories"
            ),
            UserProfile(
                getString(R.string.gender), "$savedGender"
            ),
            UserProfile(
                getString(R.string.your_birthday), "$savedBirthday"
            ),
            UserProfile(
                getString(R.string.current_weight), "$currentWeight"
            ),
            UserProfile(
                getString(R.string.your_height), "$currentHeight"
            )
        )
        val adapter = ProfileAdapter(profiles)

        binding.infoRecyclerView.adapter = adapter
        binding.infoRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}